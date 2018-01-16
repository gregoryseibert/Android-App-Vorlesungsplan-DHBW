package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.component.AppComponent;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.component.DaggerAppComponent;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.module.AppModule;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.module.RepoModule;
import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.BaseFragment;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListDayFragment;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListWeekFragment;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAB_1_TAG = "dayly";
    private static final String TAB_2_TAG = "weekly";
    private FragmentTabHost tabHost;

    private AppComponent appComponent;

    private EventListDayFragment eventListDayFragment;
    private EventListWeekFragment eventListWeekFragment;

    private TextView dateText;

    private SimpleDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate Timber
        Timber.plant(new Timber.DebugTree());

        //Setup Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        date = new SimpleDate(14, 10, 2017, 0, 0);

        Bundle bundle = new Bundle();
        bundle.putSerializable("date", date);

        eventListDayFragment = new EventListDayFragment();
        eventListDayFragment.setArguments(bundle);

        eventListWeekFragment = new EventListWeekFragment();
        eventListWeekFragment.setArguments(bundle);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this.getApplication()))
                .repoModule(new RepoModule(getApplication()))
                .build();

        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            getSupportFragmentManager().beginTransaction().add(R.id.container, eventListDayFragment).commit();
        }

        setupTabHost();

        setupDatePicker();

        setupButtons();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void setupTabHost() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", date);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.container);

        tabHost.addTab(tabHost.newTabSpec(TAB_1_TAG).setIndicator("Tagesansicht"), EventListDayFragment.class, bundle);
        tabHost.addTab(tabHost.newTabSpec(TAB_2_TAG).setIndicator("Wochenansicht"), EventListWeekFragment.class, bundle);

        /* Increase tab height programatically
         * tabs.getTabWidget().getChildAt(1).getLayoutParams().height = 150;
         */

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            final TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            if (tv == null)
                continue;
            else
                tv.setTextSize(10);
        }
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

        eventListDayFragment.setDate(date);
    }

    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String currentTabTag = tabHost.getCurrentTabTag();

        if (currentTabTag.equals(TAB_1_TAG)) {
            isPopFragment = ((BaseFragment)getSupportFragmentManager().findFragmentByTag(TAB_1_TAG)).popFragment();
        } else if (currentTabTag.equals(TAB_2_TAG)) {
            isPopFragment = ((BaseFragment)getSupportFragmentManager().findFragmentByTag(TAB_2_TAG)).popFragment();
        }

        if (!isPopFragment) {
            finish();
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
            default:
                break;
        }

        return true;
    }
}
