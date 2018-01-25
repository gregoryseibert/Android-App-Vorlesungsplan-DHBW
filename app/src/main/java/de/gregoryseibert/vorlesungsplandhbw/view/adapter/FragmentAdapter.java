package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventDayFragment;
import de.gregoryseibert.vorlesungsplandhbw.view.fragment.EventWeekFragment;

/**
 * Created by Gregory Seibert on 17.01.2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private boolean hideWeekend;
    private EventDayFragment mEventDayFragment;
    private EventWeekFragment mEventWeekFragment;

    private final String tabTitles[] = new String[] {
            "Tagesansicht",
            "Wochenansicht"
    };

    public FragmentAdapter(FragmentManager fm, boolean hideWeekend) {
        super(fm);
        this.hideWeekend = hideWeekend;
    }

    public EventDayFragment getEventListDayFragment() {
        return (EventDayFragment) getItem(0);
    }

    public EventWeekFragment getEventListWeekFragment() {
        return (EventWeekFragment) getItem(1);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            if(mEventDayFragment == null) {
                mEventDayFragment = new EventDayFragment();
            }

            return mEventDayFragment;
        } else {
            if(mEventWeekFragment == null) {
                mEventWeekFragment = new EventWeekFragment();

                if(hideWeekend) {
                    mEventWeekFragment.hideWeekend();
                }
            }

            return mEventWeekFragment;
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
