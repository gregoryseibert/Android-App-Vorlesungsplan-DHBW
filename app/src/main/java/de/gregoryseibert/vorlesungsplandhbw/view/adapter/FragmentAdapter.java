package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListDayFragment;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventListWeekFragment;

/**
 * Created by Gregory Seibert on 17.01.2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private EventListDayFragment mEventListDayFragment;
    private EventListWeekFragment mEventListWeekFragment;

    private final String tabTitles[] = new String[] {
            "Tagesansicht",
            "Wochenansicht"
    };

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public EventListDayFragment getEventListDayFragment() {
        return mEventListDayFragment;
    }

    public EventListWeekFragment getEventListWeekFragment() {
        return mEventListWeekFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            if(mEventListDayFragment == null) {
                mEventListDayFragment = new EventListDayFragment();
            }

            return mEventListDayFragment;
        } else {
            if(mEventListWeekFragment == null) {
                mEventListWeekFragment = new EventListWeekFragment();
            }

            return mEventListWeekFragment;
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
