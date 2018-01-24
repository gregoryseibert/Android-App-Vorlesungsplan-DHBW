package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.MyApplication;
import de.gregoryseibert.vorlesungsplandhbw.service.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModelFactory;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Module
public class AppModule {
    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @AppComponentScope
    public Application application() {
        return application;
    }

    @Provides
    @AppComponentScope
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @AppComponentScope
    public EventViewModelFactory eventViewModelFactory(EventRepository eventRepository, ExecutorService executorService) {
        return new EventViewModelFactory(eventRepository, executorService);
    }
}
