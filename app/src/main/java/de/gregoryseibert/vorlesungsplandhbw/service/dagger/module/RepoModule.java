package de.gregoryseibert.vorlesungsplandhbw.service.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import java.io.File;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.scope.AppComponentScope;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.AppDatabase;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventDAO;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListFragment;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Module
public class RepoModule {
    private final AppDatabase APPDATABASE;
    private final Application APPLICATION;

    public RepoModule(Application application) {
        this.APPDATABASE = Room.databaseBuilder(application, AppDatabase.class, "event-database").build();
        this.APPLICATION = application;
    }

    @Provides
    @AppComponentScope
    public AppDatabase appDatabase() {
        return APPDATABASE;
    }

    @Provides
    @AppComponentScope
    public EventDAO eventDAO() {
        return APPDATABASE.eventDao();
    }

    @Provides
    @AppComponentScope
    public EventRepository eventRepository(EventDAO eventDAO, OkHttpClient okHttpClient) {
        return new EventRepository(eventDAO, okHttpClient);
    }

    @Provides
    @AppComponentScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });
    }

    @Provides
    @AppComponentScope
    public File file() {
        return new File(APPLICATION.getCacheDir(), "okhttp_cache");
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