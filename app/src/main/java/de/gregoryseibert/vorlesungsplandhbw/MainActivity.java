package de.gregoryseibert.vorlesungsplandhbw;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;

import de.gregoryseibert.vorlesungsplandhbw.data_model.Lecture;
import de.gregoryseibert.vorlesungsplandhbw.data_model.LecturePlan;
import de.gregoryseibert.vorlesungsplandhbw.data_model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.utility.LoadDocumentTask;
import de.gregoryseibert.vorlesungsplandhbw.utility.LoadDocumentTaskParams;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private ImageButton nextButton, prevButton;
    private String key;
    private LecturePlan lecturePlan;
    private SimpleDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        key = settings.getString(getString(R.string.key_dhbwkey), "");

        date = new SimpleDate();

        rv = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rv.addItemDecoration(itemDecoration);

        dateText = findViewById(R.id.dateText);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.updateDate(date.getYear(), date.getMonth(), date.getDay());
                datePickerDialog.show();
            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                SimpleDate newDate = new SimpleDate(day, month, year, 0, 0);

                //Log.d("datepicker", newDate.getFormatDateTime());
                //Log.d("datepicker", date.getFormatDateTime());
                //Log.d("datepicker", ""+date.isSameWeek(newDate));

                if (date.isSameWeek(newDate)) {
                    date.copy(newDate);
                    loadLecturePlan(date, true);
                } else {
                    date.copy(newDate);
                    loadLecturePlan(date, false);
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDate newDate = new SimpleDate();
                newDate.copy(date);
                newDate.addDays(1);
                if (date.isSameWeek(newDate)) {
                    date.copy(newDate);
                    loadLecturePlan(newDate, true);
                } else {
                    date.copy(newDate);
                    loadLecturePlan(newDate, false);
                }
            }
        });

        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDate newDate = new SimpleDate();
                newDate.copy(date);
                newDate.addDays(-1);
                if (date.isSameWeek(newDate)) {
                    date = newDate;
                    loadLecturePlan(newDate, true);
                } else {
                    date = newDate;
                    loadLecturePlan(newDate, false);
                }
            }
        });

        loadLecturePlan(date, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        dateText.setText(date.getFormatDate());
        loadLecturePlan(date, false);
    }

    private void loadLecturePlan(SimpleDate date, boolean isSameWeek) {
        dateText.setText(date.getFormatDate());

        Log.e("loadLecturePlan", "Key length: " + key.length());

        if(key.length() > 0) {
            if (lecturePlan != null) {
                ArrayList<Lecture> lectureListOfDate = lecturePlan.getLectureListOfDate(date);

                if (lectureListOfDate.size() != 0 && isSameWeek) {
                    lectureListOfDate = addPauses(lectureListOfDate);

                    LecturePlanAdapter adapter = new LecturePlanAdapter(lectureListOfDate);
                    rv.setAdapter(adapter);

                    Log.d("loadLecturePlanOfDate", "loaded old data");
                } else {
                    new LoadDocumentTask(this).execute(new LoadDocumentTaskParams(getResources().getString(R.string.base_url), key, date));
                }
            } else {
                new LoadDocumentTask(this).execute(new LoadDocumentTaskParams(getResources().getString(R.string.base_url), key, date));
            }
        } else {
            Toast.makeText(this, "Du musst zuerst den DHBW-Key deiner Stundenplanseite in den Einstellungen hinzufügen.", Toast.LENGTH_LONG).show();
        }
    }

    public void createLecturePlan(Document doc) throws NullPointerException {
        lecturePlan = new LecturePlan();

        Elements tableRows = doc.select("#calendar .week_table tbody tr");

        Element weekHeader = tableRows.first().getElementsByClass("week_header").first();
        String firstDate = weekHeader.getElementsByTag("nobr").first().text().split(" ")[1];
        int firstDay = Integer.parseInt(firstDate.substring(0, firstDate.length()-1).split("\\.")[0]);

        //Log.d("createLecturePlan", "first day: " + firstDay);

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

                    SimpleDate lStartDate = new SimpleDate(firstDay, date.getMonth(), date.getYear(), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
                    lStartDate.addDays(indexOfBlock);

                    SimpleDate lEndDate = new SimpleDate(firstDay, date.getMonth(), date.getYear(), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
                    lEndDate.addDays(indexOfBlock);

                    StringBuilder titleBuilder = new StringBuilder();
                    for(int i = 2; i < linkSplit.length; i++) {
                        titleBuilder.append(linkSplit[i]);
                        titleBuilder.append(" ");
                    }
                    if (titleBuilder.length() > 0) {
                        titleBuilder.deleteCharAt(titleBuilder.length()-1);
                    }
                    String lTitle = titleBuilder.toString();

                    Log.d("createLecturePlan", date.getFormatDateTime() + " | " + lTitle + ": " + indexOfBlock);

                    Elements person = tableBlock.getElementsByClass("person");

                    String lLecturer = "";
                    if(person.size() > 0) {
                        lLecturer = person.first().text();
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

                    lecturePlan.addLecture(lStartDate, lEndDate, lTitle, lLecturer, lRoom, lIsExam);
                }
            }
        }

        lecturePlan.sortLectureList();

        Log.d("createLecturePlan", lecturePlan.toString());

        ArrayList<Lecture> lectureListOfDate = addPauses(lecturePlan.getLectureListOfDate(date));

        LecturePlanAdapter adapter = new LecturePlanAdapter(lectureListOfDate);
        rv.setAdapter(adapter);
    }

    private ArrayList<Lecture> addPauses(ArrayList<Lecture> lectureList) {
        for (int i = 0; i < lectureList.size(); i++) {
            if(i % 2 == 0 && i < lectureList.size() - 1) {
                lectureList.add(i + 1, new Lecture(lectureList.get(i).getEndDate(), lectureList.get(i + 1).getStartDate()));
            }
        }

        return lectureList;
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
