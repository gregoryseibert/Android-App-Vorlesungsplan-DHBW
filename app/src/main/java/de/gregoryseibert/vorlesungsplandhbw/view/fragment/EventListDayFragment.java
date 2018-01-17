package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventDayAdapter;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventListDayFragment extends Fragment {
    private RecyclerView rv;

    private EventDayAdapter eventDayAdapter;

    public EventListDayFragment() {

    }

    public void removeAllEvents() {
        eventDayAdapter.removeAllEvents();
    }

    public void setEvents(ArrayList<Event> events) {
        eventDayAdapter.addEvents(events);

        //Timber.i("events.size=" + events.size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_day_list, container, false);

        rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.addItemDecoration(new ItemOffsetDecoration(this.getContext(), R.dimen.item_offset, false));

        eventDayAdapter = new EventDayAdapter();
        rv.setAdapter(eventDayAdapter);

        return view;
    }
}
