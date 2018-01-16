package de.gregoryseibert.vorlesungsplandhbw.service.dagger.module;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.scope.AppComponentScope;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Module
public class AppModule {
    private final Application APPLICATION;

    public AppModule(Application application) {
        this.APPLICATION = application;
    }

    @Provides
    @AppComponentScope
    public Application application() {
        return APPLICATION;
    }

    @Provides
    @AppComponentScope
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }
}
