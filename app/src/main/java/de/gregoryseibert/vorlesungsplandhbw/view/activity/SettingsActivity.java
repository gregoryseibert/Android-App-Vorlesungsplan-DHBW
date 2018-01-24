package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import de.gregoryseibert.vorlesungsplandhbw.MyApplication;
import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.dependencyinjection.application.AppComponent;
import de.gregoryseibert.vorlesungsplandhbw.view.util.Toaster;

public class SettingsActivity extends AppCompatSettingsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getTitle());

        AppComponent appComponent = ((MyApplication) getApplication()).getAppComponent();

        findViewById(R.id.emptyDatabaseButton).setOnClickListener((View view) -> {
            appComponent.executorService().execute(() -> {
                int preSize = appComponent.eventRepository().getAllEvents().size();
                appComponent.eventRepository().emptyDatabase();
                int postSize = appComponent.eventRepository().getAllEvents().size();

                Toaster.toast(this, "Es wurden " + (preSize - postSize) + " Events gelöscht.");
            });
        });

        getFragmentManager().beginTransaction().replace(R.id.content, new MainPreferenceFragment()).commit();
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
        }
    }
}
