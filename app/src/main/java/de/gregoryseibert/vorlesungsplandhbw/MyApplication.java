package de.gregoryseibert.vorlesungsplandhbw;

import android.app.Application;

import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application.AppComponent;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application.AppModule;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application.DaggerAppComponent;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 24.01.2018.
 */

public class MyApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
