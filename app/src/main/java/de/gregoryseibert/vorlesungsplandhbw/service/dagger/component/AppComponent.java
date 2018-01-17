package de.gregoryseibert.vorlesungsplandhbw.service.dagger.component;

import android.app.Application;

import java.util.concurrent.ExecutorService;

import dagger.Component;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.module.AppModule;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.module.RepoModule;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.scope.AppComponentScope;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventDAO;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;
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
