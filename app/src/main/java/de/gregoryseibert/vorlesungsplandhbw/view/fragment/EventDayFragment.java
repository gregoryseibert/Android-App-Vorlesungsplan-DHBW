package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventDayAdapter;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventDayFragment extends Fragment {
    private EventDayAdapter eventDayAdapter;
    private ProgressBar progressBar;

    public EventDayFragment() {

    }

    public void setEvents(List<Event> events) {
        eventDayAdapter.addEvents(events);

        stopLoading();
    }

    public void setLoading() {
        eventDayAdapter.removeAllEvents();

        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void stopLoading() {
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_day_list, container, false);

        eventDayAdapter = new EventDayAdapter();

        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //rv.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset, false));
        rv.setAdapter(eventDayAdapter);

        progressBar = view.findViewById(R.id.progressBar);

        return view;
    }
}
