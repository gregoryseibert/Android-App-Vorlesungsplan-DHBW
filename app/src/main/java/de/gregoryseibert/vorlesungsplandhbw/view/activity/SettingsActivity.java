package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import de.gregoryseibert.vorlesungsplandhbw.MyApplication;
import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service.ServiceComponent;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.service.ServiceModule;
import de.gregoryseibert.vorlesungsplandhbw.repository.EventRepository;
import de.gregoryseibert.vorlesungsplandhbw.view.util.Toaster;

public class SettingsActivity extends AppCompatSettingsActivity {
    private ServiceComponent serviceComponent;

    @Inject EventRepository eventRepository;
    @Inject ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getServiceComponent().inject(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getTitle());

        findViewById(R.id.emptyDatabaseButton).setOnClickListener((View view) -> {
            executorService.execute(() -> {
                int preSize = eventRepository.getAllEvents().size();
                eventRepository.emptyDatabase();
                int postSize = eventRepository.getAllEvents().size();

                Toaster.toast(this, "Es wurden " + (preSize - postSize) + " Events gelöscht.");
            });
        });

        getFragmentManager().beginTransaction().replace(R.id.content, new MainPreferenceFragment()).commit();
    }

    public ServiceComponent getServiceComponent() {
        if (serviceComponent == null) {
            serviceComponent = ((MyApplication) getApplication()).getAppComponent().newServiceComponent(new ServiceModule(this));
        }

        return serviceComponent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if(preference instanceof ListPreference) {
                stringValue = ((ListPreference) preference).getEntry().toString();
            }

            preference.setSummary(stringValue);

            return true;
        }
    };

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_main);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_dhbwurl)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_refresh)));
        }
    }
}
