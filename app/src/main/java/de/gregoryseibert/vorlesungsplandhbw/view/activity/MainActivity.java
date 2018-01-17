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

        //Initiate Timber
        Timber.plant(new Timber.DebugTree());

        //Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentAdapter fragmentPagerAdapter = new FragmentAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //date = new SimpleDate(14, 10, 2017, 0, 0);
        date = new SimpleDate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repoModule(new RepoModule(getApplication()))
                .build();

        setupDatePicker();

        setupButtons();

        initViewModel();
    }

    public void setEventListDayFragment(EventListDayFragment eventListDayFragment) {
        this.eventListDayFragment = eventListDayFragment;
    }

    public void setEventListWeekFragment(EventListWeekFragment eventListWeekFragment) {
        this.eventListWeekFragment = eventListWeekFragment;
    }

    public void setupDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                setDate(new SimpleDate(day, month, year, 0, 0));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateText = findViewById(R.id.dateText);
        dateText.setText(date.getFormatDate());
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.updateDate(date.getYear(), date.getMonth(), date.getDay());
                datePickerDialog.show();
            }
        });
    }

    public void setupButtons() {
        ImageButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDate newDate = new SimpleDate(date);
                newDate.addDays(1);
                setDate(newDate);
            }
        });

        ImageButton prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDate newDate = new SimpleDate(date);
                newDate.addDays(-1);
                setDate(newDate);
            }
        });
    }

    public void setDate(SimpleDate date) {
        this.date = date;

        dateText.setText(date.getFormatDate());

        viewModel.init(url, date);

        eventListDayFragment.removeAllEvents();
        eventListWeekFragment.removeAllEvents();
    }

    public void initViewModel() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        url = settings.getString(getString(R.string.key_dhbwkey), "");

        if(url.length() > 0) {
            if(validURL(url)) {
                viewModel = appComponent.eventViewModel();
                viewModel.init(url, date);

                viewModel.getEvents().observe(this, eventList -> {
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
