package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
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

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.service.model.EventDay;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.EmptyEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.ExamEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.LectureEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.EventPlan;
import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.LoadDocumentTask;
import de.gregoryseibert.vorlesungsplandhbw.service.repo.LoadDocumentTaskParams;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventPlanAdapter;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private EditText dateText;
    private DatePickerDialog datePickerDialog;
    private ImageButton nextButton, prevButton;
    private String key;
    private EventPlan eventPlan;
    private SimpleDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        rv = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rv.addItemDecoration(itemDecoration);

        dateText = findViewById(R.id.dateText);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.updateDate(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
                datePickerDialog.show();
            }
        });

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        key = settings.getString(getString(R.string.key_dhbwkey), "");

        currentDate = new SimpleDate();
        loadLecturePlan(true);

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                SimpleDate newDate = new SimpleDate(day, month, year, 0, 0);

                if (currentDate.isSameWeek(newDate)) {
                    currentDate = new SimpleDate(newDate);
                    loadLecturePlan(false);
                } else {
                    currentDate = new SimpleDate(newDate);
                    loadLecturePlan(true);
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationButtonClicked(1);
            }
        });

        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationButtonClicked(-1);
            }
        });
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
        /*
        dateText.setText(currentDate.getFormatDate());
        Log.e("Counter", "onResume executed!");
        loadLecturePlan();
        */
    }

    public void navigationButtonClicked(int days) {
        SimpleDate newDate = new SimpleDate(currentDate);
        newDate.addDays(days);
        if (currentDate.isSameWeek(newDate)) {
            currentDate = newDate;
            loadLecturePlan(false);
        } else {
            currentDate = newDate;
            loadLecturePlan(true);
        }
    }

    private void loadLecturePlan(boolean reload) {
        boolean startDocumentTask = false;
        dateText.setText(currentDate.getFormatDate());

        if(key.length() > 0) {
            if(eventPlan != null && !reload) {
                EventDay eventDay = eventPlan.getEventDay(currentDate);

                if(eventDay != null) {
                    ArrayList<Event> eventsOfDay = eventDay.eventList;

                    if (eventsOfDay.size() > 0) {
                        EventPlanAdapter adapter = new EventPlanAdapter(eventsOfDay);
                        rv.setAdapter(adapter);
                    } else {
                        startDocumentTask = true;
                    }
                } else {
                    startDocumentTask = true;
                }
            } else {
                startDocumentTask = true;
            }
        } else {
            Toast.makeText(this, "Du musst zuerst den DHBW-Key deiner Stundenplanseite in den Einstellungen hinzufügen.", Toast.LENGTH_LONG).show();
        }

        if(startDocumentTask) {
            new LoadDocumentTask(this).execute(new LoadDocumentTaskParams(getResources().getString(R.string.base_url), key, currentDate));
        }
    }

    public void createLecturePlan(Document doc) {
        Elements tableRows = doc.select("#calendar .week_table tbody tr");

        Element weekHeader = tableRows.first().getElementsByClass("week_header").first();
        String firstDateStr = weekHeader.getElementsByTag("nobr").first().text().split(" ")[1];
        int firstDay = Integer.parseInt(firstDateStr.substring(0, firstDateStr.length()-1).split("\\.")[0]);

        SimpleDate firstDate = new SimpleDate(currentDate);
        firstDate.setDay(firstDay);
        eventPlan = new EventPlan(firstDate);

        for(Element tableRow : tableRows) {
            Elements tableRowElements = tableRow.select(":root > td");

            Elements tableBlocks = tableRow.getElementsByClass("week_block");

            if(!tableBlocks.isEmpty()) {
                for(Element tableBlock : tableBlocks) {
                    int indexOfBlock = tableRowElements.indexOf(tableBlock) / 3;

                    Element link = tableBlock.getElementsByTag("a").first();
                    String linkText = link.text().replace("-", "");
                    String linkContent = linkText.substring(0, linkText.indexOf("erstellt"));

                    String[] linkSplit = linkContent.split(" ");
                    String[] startTime = linkSplit[0].split(":");
                    String[] endTime = linkSplit[1].split(":");

                    SimpleDate lStartDate = new SimpleDate(firstDay, currentDate.getMonth(), currentDate.getYear(), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]));
                    lStartDate.addDays(indexOfBlock);

                    SimpleDate lEndDate = new SimpleDate(firstDay, currentDate.getMonth(), currentDate.getYear(), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
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

                    Event event;
                    if(linkContent.contains("Klausur")) {
                        event = new ExamEvent(lStartDate, lEndDate, lTitle, lRoom);
                    } else {
                        event = new LectureEvent(lStartDate, lEndDate, lTitle, lRoom, lLecturer);
                    }

                    eventPlan.addEvent(indexOfBlock, event);
                }
            }
        }

        eventPlan.sortEventList();

        Log.d("MA | createLecturePlan", eventPlan.toString());

        EventPlanAdapter adapter = new EventPlanAdapter(eventPlan.getEventDay(currentDate).eventList);
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
