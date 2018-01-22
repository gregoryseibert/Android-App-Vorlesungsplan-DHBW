package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
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
import de.gregoryseibert.vorlesungsplandhbw.view.util.ZoomOutAndSlideTransformer;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;

    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener settingsListener;

    private EventListDayFragment eventListDayFragment;
    private EventListWeekFragment eventListWeekFragment;

    public static AppComponent appComponent;

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

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsListener = (SharedPreferences sharedPreferences, String key) -> {
            /*if(key.equals(getString(R.string.key_datepickertop))) {
                initLayout();
            } else */if (key.equals(getString(R.string.key_dhbwurl))) {
                initViewModel();
            }
        };
        settings.registerOnSharedPreferenceChangeListener(settingsListener);

        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutAndSlideTransformer());

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repoModule(new RepoModule(getApplication()))
                .build();

        date = new SimpleDate();

        setupDatePicker();

        initViewModel();

        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
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

        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme, (DatePicker view, int year, int month, int day) -> {
            SimpleDate date = new SimpleDate(day, month, year, 0, 0);
            setDate(date);
            horizontalCalendar.selectDate(date.getCalendar(), true);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                SimpleDate simpleDate = new SimpleDate(date.getTimeInMillis());
                setDate(simpleDate);
            }

            @Override
            public boolean onDateLongClicked(Calendar cal, int position) {
                SimpleDate date = new SimpleDate(cal.getTimeInMillis());
                datePickerDialog.updateDate(date.getYear(), date.getMonth(), date.getDay());
                datePickerDialog.show();

                return true;
            }
        });
    }

    public void setDate(SimpleDate date) {
        if(!date.equals(this.date)) {
            this.date = date;

            viewModel.init(url, date);
        }
    }

    public void initViewModel() {
        url = settings.getString(getString(R.string.key_dhbwurl), "");

        if(url.length() > 0) {
            if(validURL(url)) {
                viewModel = appComponent.eventViewModel();
                viewModel.init(url, date);

                viewModel.getEvents().observe(this, week -> {
                    //Timber.i("observed");
                    eventListDayFragment.setEvents(week.getEventsOfDay(date.getDayOfWeek()));
                    eventListWeekFragment.setEvents(week);
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

    public void initLayout() {
        if(!settings.getBoolean(getString(R.string.key_datepickertop), true)) {
            HorizontalCalendarView calendarView = findViewById(R.id.calendarView);
            ((ViewGroup) calendarView.getParent()).removeView(calendarView);
            ((ViewGroup) findViewById(R.id.relativeLayout)).addView(calendarView);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) calendarView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.topMargin = 0;
            calendarView.setLayoutParams(params);

            params = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            params.addRule(RelativeLayout.ABOVE, R.id.calendarView);
            viewPager.setLayoutParams(params);
        }
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
