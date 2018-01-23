package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.gregoryseibert.vorlesungsplandhbw.view.activity.MainActivity;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListDayFragment;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListWeekFragment;

/**
 * Created by Gregory Seibert on 17.01.2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private final MainActivity MAINACTIVITY;

    private final String tabTitles[] = new String[] {
            "Tagesansicht",
            "Wochenansicht"
    };

    public FragmentAdapter(MainActivity mainActivity, FragmentManager fm) {
        super(fm);

        this.MAINACTIVITY = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            EventListDayFragment eventListDayFragment = new EventListDayFragment();

            MAINACTIVITY.setEventListDayFragment(eventListDayFragment);

            return eventListDayFragment;
        } else {
            EventListWeekFragment eventListWeekFragment = new EventListWeekFragment();

            MAINACTIVITY.setEventListWeekFragment(eventListWeekFragment);

            return eventListWeekFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
