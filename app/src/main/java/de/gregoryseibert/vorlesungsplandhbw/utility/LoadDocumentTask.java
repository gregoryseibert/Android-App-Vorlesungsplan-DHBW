package de.gregoryseibert.vorlesungsplandhbw.utility;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import de.gregoryseibert.vorlesungsplandhbw.MainActivity;

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

            return Jsoup.connect(Utility.generateURL(params.base, params.key, params.date.getDay(), params.date.getMonth() + 1, params.date.getYear())).get();
        } catch (Exception e) {
            Log.e("LoadDocumentTask", e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(Document doc) {
        try {
            activity.createLecturePlan(doc);
        } catch(NullPointerException e) {
            Log.e("LoadDocumentTask", e.getMessage());
            Toast.makeText(activity, "Jsoup Error!", Toast.LENGTH_SHORT);
        }
    }
}
