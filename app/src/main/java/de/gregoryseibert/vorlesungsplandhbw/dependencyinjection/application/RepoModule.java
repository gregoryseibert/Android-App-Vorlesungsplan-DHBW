package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.service.AppDatabase;
import de.gregoryseibert.vorlesungsplandhbw.service.EventDAO;
import de.gregoryseibert.vorlesungsplandhbw.service.EventRepository;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Module
public class RepoModule {
    @Provides
    @AppComponentScope
    public AppDatabase appDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "event-database").fallbackToDestructiveMigration().build();
    }

    @Provides
    @AppComponentScope
    public EventDAO eventDAO(AppDatabase appDatabase) {
        return appDatabase.eventDao();
    }

    @Provides
    @AppComponentScope
    public EventRepository eventRepository(EventDAO eventDAO, OkHttpClient okHttpClient) {
        return new EventRepository(eventDAO, okHttpClient);
    }

    @Provides
    @AppComponentScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor((String message) -> Timber.i(message));
    }

    @Provides
    @AppComponentScope
    public File file(Application application) {
        return new File(application.getCacheDir(), "okhttp_cache");
    }

    @Provides
    @AppComponentScope
    public Cache cache(File file) {
        return new Cache(file, 10 * 1000 * 1000);
    }

    @Provides
    @AppComponentScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).cache(cache).build();
    }
}
