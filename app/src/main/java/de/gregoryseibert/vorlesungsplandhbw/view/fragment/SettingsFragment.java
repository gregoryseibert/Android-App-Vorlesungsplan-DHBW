package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.view.activity.AppCompatSettingsActivity;

/**
 * Created by Gregory Seibert on 18.01.2018.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(R.style.AppTheme);

        if (getArguments() != null) {
            String page = getArguments().getString("page");
            if (page != null)
                switch (page) {
                    case "page1":
                        addPreferencesFromResource(R.xml.settings_page1);
                        break;
                }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_settings, container, false);
        if (layout != null) {
            AppCompatSettingsActivity activity = (AppCompatSettingsActivity) getActivity();
            Toolbar toolbar = layout.findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);

            ActionBar bar = activity.getSupportActionBar();
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.arrow_left);
            bar.setTitle(getPreferenceScreen().getTitle());
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() != null) {
            View frame = (View) getView().getParent();
            if (frame != null)
                frame.setPadding(0, 0, 0, 0);
        }
    }
}
