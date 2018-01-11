package de.gregoryseibert.vorlesungsplandhbw.service.repo;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import de.gregoryseibert.vorlesungsplandhbw.view.activity.MainActivity;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LoadDocumentTask extends AsyncTask<LoadDocumentTaskParams, Void, Document> {
    private MainActivity activity;

    public LoadDocumentTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Document doInBackground(LoadDocumentTaskParams... paramsArr) {
        try {
            LoadDocumentTaskParams params = paramsArr[0];

            if (params.base.length() == 0 || params.key.length() == 0) {
                return null;
            }

            //Log.d("loadLecturePlan", params.date.getDay() + " " + params.date.getMonth() + " " +params.date.getYear());
            //Log.d("URL:", Utility.generateURL(params.base, params.key, params.date.getDay(), params.date.getMonth(), params.date.getYear()));

            return Jsoup.connect(generateURL(params.base, params.key, params.date.getDay(), params.date.getMonth() + 1, params.date.getYear())).get();
        } catch (IOException e) {
            Log.e("LoadDocumentTask", e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(Document doc) {
        activity.createLecturePlan(doc);
    }

    private String generateURL(String base, String key, int day, int month, int year) {
        String url = base + key;

        if(day != 0 && month != 0 && year != 0) {
            url += "&day=" + day;
            url += "&month=" + month;
            url += "&year=" + year;
        }

        return url;
    }
}
