package de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceComponentScope {
}
