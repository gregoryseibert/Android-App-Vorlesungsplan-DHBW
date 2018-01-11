package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LoadDocumentTaskParams {
    String base;
    String key;
    SimpleDate date;

    public LoadDocumentTaskParams(String base, String key, SimpleDate date) {
        this.base = base;
        this.key = key;
        this.date = date;
    }
}
