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
import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.EventHolder;
import de.gregoryseibert.vorlesungsplandhbw.model.SimpleDate;
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

    public void removeAllEvents() {
        if(weekTextViews.size() > 0) {
            for(int i = 0; i < 7; i++) {
                eventWeekAdapters.get(i).removeAllEvents();
            }
        }
    }

    public void setEvents(List<EventHolder> events, SimpleDate firstDate) {
        if(weekTextViews.size() > 0) {
            for(int i = 0; i < 7; i++) {
                TextView dayText = weekTextViews.get(i);
                dayText.setText(firstDate.getFormatDateShort());

                eventWeekAdapters.get(i).addEvents(events.get(i).getAllEvents());

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
