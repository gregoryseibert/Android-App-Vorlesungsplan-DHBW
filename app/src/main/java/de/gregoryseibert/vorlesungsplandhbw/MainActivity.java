package de.gregoryseibert.vorlesungsplandhbw;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import de.gregoryseibert.vorlesungsplandhbw.data_model.LecturePlan;
import de.gregoryseibert.vorlesungsplandhbw.utility.LoadDocumentTask;
import de.gregoryseibert.vorlesungsplandhbw.utility.LoadDocumentTaskParams;
import de.gregoryseibert.vorlesungsplandhbw.utility.Utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private EditText testText, dateText;
    private DatePickerDialog datePickerDialog;
    private String key;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key = "txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhDUd7PDlGaEoMaVfHmMbiow";

        day = 13;
        month = 11;
        year = 2017;

        testText = findViewById(R.id.testText);
        dateText = findViewById(R.id.dateText);
        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    datePickerDialog.show();
                v.clearFocus();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                loadLecturePlan(day, month, year);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        loadLecturePlan(day, month, year);
    }

    private void loadLecturePlan(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;

        dateText.setText("Vorlesungsplan der Woche: " + Utility.formatDateSimple(Utility.getDate(day, month, year, 0, 0)));

        new LoadDocumentTask(this).execute(new LoadDocumentTaskParams(getResources().getString(R.string.base_url), key, day, month, year));
    }

    public void createLecturePlan(Document doc) {
        LecturePlan lecturePlan = new LecturePlan();

        Elements tableRows = doc.select("#calendar .week_table tbody tr");

        Element weekHeader = tableRows.first().getElementsByClass("week_header").first();
        String firstDate = weekHeader.getElementsByTag("nobr").first().text().split(" ")[1];
        int firstDay = Integer.parseInt(firstDate.substring(0, firstDate.length()-1).split("\\.")[0]);

        for(Element tableRow : tableRows) {
            Elements tableRowElements = tableRow.select(":root > td");

            Elements tableBlocks = tableRow.getElementsByClass("week_block");

            if(!tableBlocks.isEmpty()) {
                for(Element tableBlock : tableBlocks) {
                    //Log.e("Test", tableRowElements.toString());
                    int indexOfBlock = tableRowElements.indexOf(tableBlock) / 3;

                    Element link = tableBlock.getElementsByTag("a").first();
                    String linkText = link.text().replace("-", "");
                    String linkContent = linkText.substring(0, linkText.indexOf("erstellt"));

                    boolean lIsExam = linkContent.contains("Klausur");

                    String[] linkSplit = linkContent.split(" ");
                    String[] startTime = linkSplit[0].split(":");
                    String[] endTime = linkSplit[1].split(":");

                    Calendar c = Calendar.getInstance();

                    c.setTime(Utility.getDate(firstDay, month, year, Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1])));
                    c.add(Calendar.DATE, indexOfBlock);
                    long lStartTime = c.getTimeInMillis();

                    c.setTime(Utility.getDate(firstDay, month, year, Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1])));
                    c.add(Calendar.DATE, indexOfBlock);
                    long lEndTime = c.getTimeInMillis();

                    StringBuilder titleBuilder = new StringBuilder();
                    for(int i = 2; i < linkSplit.length; i++) {
                        titleBuilder.append(linkSplit[i]);
                        titleBuilder.append(" ");
                    }
                    if (titleBuilder.length() > 0) {
                        titleBuilder.deleteCharAt(titleBuilder.length()-1);
                    }
                    String lTitle = titleBuilder.toString();

                    String lLecturer = "";
                    if(!lIsExam) {
                        Element person = tableBlock.getElementsByClass("person").first();
                        lLecturer = person.text();
                    }

                    Element resource = tableBlock.getElementsByClass("resource").first();
                    String lRoom = resource.text();

                    lecturePlan.addLecture(lStartTime, lEndTime, lTitle, lLecturer, lRoom, lIsExam);
                }
            }
        }

        lecturePlan.sortLectureList();

        testText.setText(lecturePlan.toString());
    }
}
