package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.database.AppDatabase;
import de.gregoryseibert.vorlesungsplandhbw.database.EventDAO;
import de.gregoryseibert.vorlesungsplandhbw.repository.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 24.01.2018.
 */

@Module
public class ServiceModule {
    private Activity activity;

    public ServiceModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ServiceComponentScope
    EventViewModel eventViewModel(EventRepository eventRepository, ExecutorService executorService) {
        EventViewModel eventViewModel = ViewModelProviders.of((FragmentActivity) activity).get(EventViewModel.class);
        eventViewModel.setEventRepository(eventRepository);
        eventViewModel.setExecutorService(executorService);
        return eventViewModel;
    }

    @Provides
    @ServiceComponentScope
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @ServiceComponentScope
    public EventRepository eventRepository(EventDAO eventDAO, OkHttpClient okHttpClient) {
        return new EventRepository(eventDAO, okHttpClient);
    }

    @Provides
    @ServiceComponentScope
    public AppDatabase appDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "event-database").fallbackToDestructiveMigration().build();
    }

    @Provides
    @ServiceComponentScope
    public EventDAO eventDAO(AppDatabase appDatabase) {
        return appDatabase.eventDao();
    }

    @Provides
    @ServiceComponentScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor((String message) -> Timber.i(message));
    }

    @Provides
    @ServiceComponentScope
    public File file(Application application) {
        return new File(application.getCacheDir(), "okhttp_cache");
    }

    @Provides
    @ServiceComponentScope
    public Cache cache(File file) {
        return new Cache(file, 10 * 1000 * 1000);
    }

    @Provides
    @ServiceComponentScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).cache(cache).build();
    }
}
