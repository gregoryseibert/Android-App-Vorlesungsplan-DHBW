package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application;

import android.app.Application;

import java.util.concurrent.ExecutorService;

import dagger.Component;
import de.gregoryseibert.vorlesungsplandhbw.service.EventDAO;
import de.gregoryseibert.vorlesungsplandhbw.service.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@AppComponentScope
@Component(modules = {AppModule.class, RepoModule.class})
public interface AppComponent {
    Application application();
    EventViewModel eventViewModel();
    ExecutorService executorService();
    EventRepository eventRepository();
    EventDAO eventDAO();
    OkHttpClient okHttpClient();
    HttpLoggingInterceptor httpLoggingInterceptor();
    Cache cache();
}
