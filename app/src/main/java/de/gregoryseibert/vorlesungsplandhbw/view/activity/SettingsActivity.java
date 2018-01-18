package de.gregoryseibert.vorlesungsplandhbw.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatSettingsActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings, target);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.arrow_left);
        bar.setTitle(getTitle());
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
