package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;

import javax.inject.Inject;

import de.gregoryseibert.vorlesungsplandhbw.MyApplication;
import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service.ServiceComponent;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service.ServiceModule;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.FragmentAdapter;
import de.gregoryseibert.vorlesungsplandhbw.view.util.Toaster;
import de.gregoryseibert.vorlesungsplandhbw.view.util.Validator;
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
    private ServiceComponent serviceComponent;

    @Inject SharedPreferences sharedPreferences;
    @Inject EventViewModel eventViewModel;

    private FragmentAdapter fragmentPagerAdapter;

    private ViewPager viewPager;

    private ProgressBar progressBar;

    private SimpleDate date;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getServiceComponent().inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = new SimpleDate();

        progressBar = findViewById(R.id.progressBar);

        setLoading();

        setupViewPager();

        setupDatePicker();

        setupDatePickerLayout();

        url = sharedPreferences.getString(getString(R.string.key_dhbwurl), "");
        if(url.length() == 0 || !Validator.validateURL(url)) {
            showUrlDialog();
        } else {
            setupViewModel();
        }
    }

    public ServiceComponent getServiceComponent() {
        if (serviceComponent == null) {
            serviceComponent = ((MyApplication) getApplication()).getAppComponent().newServiceComponent(new ServiceModule(this));
        }

        return serviceComponent;
    }

    public void setLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void stopLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showUrlDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_dhbwurl)
                .content(R.string.popup_message_dhbwurl)
                .inputType(InputType.TYPE_TEXT_VARIATION_URI)
                .input(getString(R.string.popup_hint_dhbwurl), "",(MaterialDialog dialog, CharSequence input) -> {
                    url = input.toString();
                    if(url.length() > 0 || Validator.validateURL(url)) {
                        sharedPreferences.edit().putString(getString(R.string.key_dhbwurl), url).apply();
                    }
                    setupViewModel();
                })
                .cancelable(false)
                .show();
    }

    private void setupViewPager() {
        fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager(), sharedPreferences.getBoolean(getResources().getString(R.string.key_hideweekend), true));

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutAndSlideTransformer());

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
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

    public void setupViewModel() {
        if(url.length() > 0) {
            if(Validator.validateURL(url)) {
                eventViewModel.init(url, date);

                eventViewModel.getEvents().observe(this, week -> {
                    if(week != null) {
                        fragmentPagerAdapter.getEventListDayFragment().setEvents(week.getEventsOfDay(date.getDayOfWeek()));
                        fragmentPagerAdapter.getEventListWeekFragment().setEvents(week);
                        stopLoading();
                    }
                });
            } else {
                Toaster.toast(this, "Die in den Einstellungen gespeicherte URL deines Vorlesungsplans ist fehlerhaft.", true);
            }
        }
    }

    public void setupDatePickerLayout() {
        if(!sharedPreferences.getBoolean(getString(R.string.key_datepickertop), true)) {
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

    public void setDate(SimpleDate date) {
        if(!date.equals(this.date)) {
            this.date = date;

            setLoading();

            if(url.length() > 0 && Validator.validateURL(url)) {
                eventViewModel.init(url, date);
            }
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
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                break;
            default:
                break;
        }

        if(intent != null) {
            startActivity(intent);
        }

        return true;
    }
}
