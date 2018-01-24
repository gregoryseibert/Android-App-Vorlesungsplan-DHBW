package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application;

import dagger.Component;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service.ServiceComponent;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service.ServiceModule;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@AppComponentScope
@Component(modules = {AppModule.class})
public interface AppComponent {
    ServiceComponent newServiceComponent(ServiceModule serviceModule);
}