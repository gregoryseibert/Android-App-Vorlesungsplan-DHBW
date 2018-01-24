package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gregory Seibert on 24.01.2018.
 */

@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @AppComponentScope
    public Application application() {
        return application;
    }

    @Provides
    @AppComponentScope
    public SharedPreferences sharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
