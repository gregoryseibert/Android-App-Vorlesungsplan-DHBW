package de.gregoryseibert.vorlesungsplandhbw.utility;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

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

            return Jsoup.connect(Utility.generateURL(params.base, params.key, params.day, params.month, params.year)).get();
        } catch (Exception e) {
            Log.e("LoadDocumetnTask", e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(Document doc) {
        activity.createLecturePlan(doc);
    }
}
