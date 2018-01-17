package de.gregoryseibert.vorlesungsplandhbw.service.dagger.module;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.scope.AppComponentScope;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.view.activity.MainActivity;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Module
public class AppModule {
    private final MainActivity MAINACTIVITY;

    public AppModule(MainActivity mainActivity) {
        this.MAINACTIVITY = mainActivity;
    }

    @Provides
    @AppComponentScope
    public Application application() {
        return MAINACTIVITY.getApplication();
    }

    @Provides
    @AppComponentScope
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @AppComponentScope
    public EventViewModel eventViewModel(EventRepository eventRepository, ExecutorService executorService) {
        EventViewModel viewModel = ViewModelProviders.of(MAINACTIVITY).get(EventViewModel.class);
        viewModel.setEventRepository(eventRepository);
        viewModel.setExecutorService(executorService);
        return viewModel;
    }
}
