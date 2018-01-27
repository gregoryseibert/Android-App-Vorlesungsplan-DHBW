package de.gregoryseibert.vorlesungsplandhbw.view.util;

/**
 * Created by Gregory Seibert on 23.01.2018.
 */

public abstract class UrlUtil {
    public static boolean validate(String url) {
        if(!url.startsWith("https://rapla.dhbw-stuttgart.de/rapla?key=")) {
            return false;
        }

        return true;
    }

    public static String sanitize(String url) {
        if(url.contains("&")) {
            return url.split("&")[0];
        }

        return url;
    }
}
