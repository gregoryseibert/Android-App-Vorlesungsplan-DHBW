package de.gregoryseibert.vorlesungsplandhbw.view.util;

/**
 * Created by Gregory Seibert on 23.01.2018.
 */

public abstract class Validator {
    public static boolean validateURL(String url) {
        if(!url.startsWith("https://rapla.dhbw-stuttgart.de/rapla?key=")) {
            return false;
        }

        return true;
    }
}
