package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service;

import dagger.Subcomponent;
import de.gregoryseibert.vorlesungsplandhbw.view.activity.MainActivity;
import de.gregoryseibert.vorlesungsplandhbw.view.activity.SettingsActivity;

/**
 * Created by Gregory Seibert on 24.01.2018.
 */

@ServiceComponentScope
@Subcomponent(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(MainActivity mainActivity);
    void inject(SettingsActivity settingsActivity);
}