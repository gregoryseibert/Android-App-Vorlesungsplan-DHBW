package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.component.AppComponent;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.component.DaggerAppComponent;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.module.AppModule;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.module.RepoModule;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.FragmentAdapter;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListDayFragment;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListWeekFragment;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;

    private EventListDayFragment eventListDayFragment;
    private EventListWeekFragment eventListWeekFragment;

    private TextView dateText;

    private AppComponent appComponent;

    private SimpleDate date;
    private EventViewModel viewModel;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repoModule(new RepoModule(getApplication()))
                .build();

        date = new SimpleDate();

        setupDatePicker();

        initViewModel();
    }

    public void setEventListDayFragment(EventListDayFragment eventListDayFragment) {
        this.eventListDayFragment = eventListDayFragment;
    }

    public void setEventListWeekFragment(EventListWeekFragment eventListWeekFragment) {
        this.eventListWeekFragment = eventListWeekFragment;
    }

    public void setupDatePicker() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -3);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 3);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                SimpleDate simpleDate = new SimpleDate(date.getTimeInMillis());
                setDate(simpleDate);
            }
        });
    }

    public void setDate(SimpleDate date) {
        if(!date.equals(this.date)) {
            this.date = date;

            viewModel.init(url, date);

            eventListDayFragment.removeAllEvents();
            eventListWeekFragment.removeAllEvents();
        }
    }

    public void initViewModel() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        url = settings.getString(getString(R.string.key_dhbwurl), "");

        if(url.length() > 0) {
            if(validURL(url)) {
                viewModel = appComponent.eventViewModel();
                viewModel.init(url, date);

                viewModel.getEvents().observe(this, eventList -> {
                    Timber.i("observed");
                    eventListDayFragment.setEvents(eventList.get(date.getDayOfWeek()).getAllEvents());
                    eventListWeekFragment.setEvents(eventList, date.getFirstDayOfWeek());
                });
            } else {
                Toast.makeText(this, "Die in den Einstellungen gespeicherte URL deines Vorlesungsplans ist fehlerhaft.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Die URL deines Vorlesungsplans muss in den Einstellungen dieser App gespeichert werden.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validURL(String url) {
        if(!url.startsWith("https://rapla.dhbw-stuttgart.de/rapla?key=")) {
            return false;
        }

        return true;
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
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }

        return true;
    }
}
