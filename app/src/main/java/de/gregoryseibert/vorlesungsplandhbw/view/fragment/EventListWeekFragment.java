package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.Day;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.model.Week;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventWeekAdapter;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

public class EventListWeekFragment extends Fragment {
    private ArrayList<TextView> weekTextViews;

    private ArrayList<RecyclerView> recyclerViews;

    private ArrayList<EventWeekAdapter> eventWeekAdapters;

    public EventListWeekFragment() {

    }

    public void setEvents(Week week) {
        if(weekTextViews.size() > 0) {
            SimpleDate firstDate = week.getFirstDate();

            for(Day day : week.getDays()) {
                int index = week.getDays().indexOf(day);

                eventWeekAdapters.get(index).removeAllEvents();

                TextView dayText = weekTextViews.get(index);
                dayText.setText(firstDate.getFormatDateShort());

                eventWeekAdapters.get(index).addEvents(day.getEvents());

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

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        weekTextViews = new ArrayList<>();
        recyclerViews = new ArrayList<>();
        eventWeekAdapters = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            View itemWeek = inflater.inflate(R.layout.item_week, linearLayout, false);
            linearLayout.addView(itemWeek);

            RecyclerView itemWeekRecyclerView = itemWeek.findViewById(R.id.recyclerView);
            itemWeekRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
            itemWeekRecyclerView.addItemDecoration(new ItemOffsetDecoration(this.getContext(), R.dimen.item_offset, true));

            EventWeekAdapter eventWeekAdapter = new EventWeekAdapter();

            itemWeekRecyclerView.setAdapter(eventWeekAdapter);

            weekTextViews.add(itemWeek.findViewById(R.id.dayText));
            recyclerViews.add(itemWeekRecyclerView);
            eventWeekAdapters.add(eventWeekAdapter);
        }

        return view;
    }
}
