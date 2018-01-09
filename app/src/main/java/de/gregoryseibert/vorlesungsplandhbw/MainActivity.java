package de.gregoryseibert.vorlesungsplandhbw;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.gregoryseibert.vorlesungsplandhbw.data_model.Lecture;
import de.gregoryseibert.vorlesungsplandhbw.data_model.LecturePlan;
import de.gregoryseibert.vorlesungsplandhbw.utility.LoadDocumentTask;
import de.gregoryseibert.vorlesungsplandhbw.utility.LoadDocumentTaskParams;
import de.gregoryseibert.vorlesungsplandhbw.utility.Utility;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private ImageButton nextButton, prevButton;
    private String key;
    private int day, month, year;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key = "txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhDUd7PDlGaEoMaVfHmMbiow";

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);

        rv = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rv.addItemDecoration(itemDecoration);

        dateText = findViewById(R.id.dateText);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.updateDate(year, month, day);
                datePickerDialog.show();
            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                loadLecturePlan(day, month, year);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                c.add(Calendar.DAY_OF_MONTH, 1);
                loadLecturePlan(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));
            }
        });

        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                c.add(Calendar.DAY_OF_MONTH, -1);
                loadLecturePlan(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));
            }
        });

        loadLecturePlan(day, month, year);
    }

    @Override
    public void onResume() {
        super.onResume();
        dateText.setText(Utility.formatDateSimple(Utility.getDate(day, month + 1, year, 0, 0)));
    }

    private void loadLecturePlan(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;

        dateText.setText(Utility.formatDateSimple(Utility.getDate(day, month + 1, year, 0, 0)));

        new LoadDocumentTask(this).execute(new LoadDocumentTaskParams(getResources().getString(R.string.base_url), key, day, month + 1, year));
    }

    public void createLecturePlan(Document doc) throws NullPointerException{
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
                    int indexOfBlock = tableRowElements.indexOf(tableBlock) / 3;

                    Element link = tableBlock.getElementsByTag("a").first();
                    String linkText = link.text().replace("-", "");
                    String linkContent = linkText.substring(0, linkText.indexOf("erstellt"));

                    boolean lIsExam = linkContent.contains("Klausur");

                    String[] linkSplit = linkContent.split(" ");
                    String[] startTime = linkSplit[0].split(":");
                    String[] endTime = linkSplit[1].split(":");

                    Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
                    c1.setTime(Utility.getDate(firstDay, month, year, 0, 0));
                    c1.add(Calendar.DATE, indexOfBlock);
                    c2.setTime(Utility.getDate(day, month, year, 0, 0));

                    if(c1.equals(c2)) {
                        c1.setTime(Utility.getDate(firstDay, month + 1, year, Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1])));
                        c1.add(Calendar.DATE, indexOfBlock);
                        long lStartTime = c1.getTimeInMillis();

                        c2.setTime(Utility.getDate(firstDay, month + 1, year, Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1])));
                        c2.add(Calendar.DATE, indexOfBlock);
                        long lEndTime = c2.getTimeInMillis();

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

                            if(lLecturer.endsWith(",")) {
                                lLecturer = lLecturer.substring(0, lLecturer.length() - 1);
                            }
                        }

                        Elements resources = tableBlock.getElementsByClass("resource");

                        String lRoom = "";
                        for(int i = 0; i < resources.size(); i++) {
                            if(resources.get(i).text().contains(".")) {
                                lRoom = resources.get(i).text();
                            }
                        }

                        lecturePlan.addLecture(lStartTime, lEndTime, lTitle, lLecturer, lRoom, lIsExam);
                    }
                }
            }
        }

        lecturePlan.sortLectureList();

        ArrayList<Lecture> lectureList = lecturePlan.getLectureList();
        for (int i = 0; i < lectureList.size(); i++) {
            if(i % 2 == 0 && i < lectureList.size() - 1) {
                lectureList.add(i + 1, new Lecture(lectureList.get(i).getEndTime(), lectureList.get(i + 1).getStartTime()));
            }
        }

        LecturePlanAdapter adapter = new LecturePlanAdapter(lectureList);
        rv.setAdapter(adapter);
    }

    class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, mItemOffset);
        }
    }
}
