package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.Day;
import de.gregoryseibert.vorlesungsplandhbw.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.model.Week;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventWeekAdapter;
import de.gregoryseibert.vorlesungsplandhbw.view.util.Animator;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

public class EventListWeekFragment extends Fragment {
    private ArrayList<View> itemWeeks;

    private ArrayList<EventWeekAdapter> eventWeekAdapters;

    private int numberOfDays;

    public EventListWeekFragment() {
        numberOfDays = 6;
    }

    public void hideWeekend() {
        numberOfDays = 5;
    }

    public void setEvents(Week week) {
        if(itemWeeks.size() > 0) {
            SimpleDate firstDate = week.getFirstDate();

            for(int i = 0; i < numberOfDays; i++) {
                Day day = week.getDays().get(i);

                eventWeekAdapters.get(i).removeAllEvents();

                List<Event> events = day.getEvents();

                TextView dayText = itemWeeks.get(i).findViewById(R.id.dayText);
                dayText.setText(firstDate.getFormatDateShort());

                TextView eventCounter = itemWeeks.get(i).findViewById(R.id.eventCounter);

                if(events != null && events.size() > 0) {
                    eventCounter.setText(String.format("%d", events.size()));
                } else {
                    eventCounter.setText("0");
                }

                eventWeekAdapters.get(i).addEvents(events);

                firstDate.addDays(1);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_week_list, container, false);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean maximizedWeekly = settings.getBoolean(getString(R.string.key_weeklyexpanded), true);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        itemWeeks = new ArrayList<>();
        eventWeekAdapters = new ArrayList<>();

        for(int i = 0; i < numberOfDays; i++) {
            View itemWeek = inflater.inflate(R.layout.item_week, linearLayout, false);
            linearLayout.addView(itemWeek);

            RecyclerView itemWeekRecyclerView = itemWeek.findViewById(R.id.recyclerView);
            itemWeekRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            ToggleButton toggleButton = itemWeek.findViewById(R.id.toggleButton);
            toggleButton.setChecked(maximizedWeekly);
            toggleButton.setClickable(false);

            FrameLayout containerBottom = itemWeek.findViewById(R.id.week_container_bottom);

            if(!maximizedWeekly) {
                containerBottom.setVisibility(View.GONE);
            }

            itemWeek.findViewById(R.id.week_container_top).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(toggleButton.isChecked()) {
                        Animator.collapse(containerBottom);
                        toggleButton.setChecked(false);
                    } else {
                        Animator.expand(containerBottom);
                        toggleButton.setChecked(true);
                    }
                }
            });

            EventWeekAdapter eventWeekAdapter = new EventWeekAdapter(getContext());

            itemWeekRecyclerView.setAdapter(eventWeekAdapter);

            itemWeeks.add(itemWeek);
            eventWeekAdapters.add(eventWeekAdapter);
        }

        return view;
    }
}
