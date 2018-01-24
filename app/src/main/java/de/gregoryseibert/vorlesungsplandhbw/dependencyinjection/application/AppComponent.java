package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application;

import android.app.Application;

import java.util.concurrent.ExecutorService;

import dagger.Component;
import de.gregoryseibert.vorlesungsplandhbw.service.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModelFactory;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@AppComponentScope
@Component(modules = {AppModule.class, RepoModule.class})
public interface AppComponent {
    Application application();
    EventViewModelFactory eventViewModelFactory();
    ExecutorService executorService();
    EventRepository eventRepository();
}
