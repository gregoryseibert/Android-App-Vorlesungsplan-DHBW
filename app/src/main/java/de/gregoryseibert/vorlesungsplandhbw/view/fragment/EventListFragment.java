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

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.service.dagger.component.AppComponent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event.EventType;
import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.view.activity.MainActivity;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventPlanAdapter;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventListFragment extends Fragment {
    private AppComponent appComponent;

    private RecyclerView rv;

    private EventPlanAdapter eventPlanAdapter;

    private SimpleDate date;
    private EventViewModel viewModel;

    private String url;

    public EventListFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appComponent = ((MainActivity) getActivity()).getAppComponent();

        date = (SimpleDate) getArguments().getSerializable("date");
        Timber.i(date.getFormatDate());

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        url = settings.getString(getString(R.string.key_dhbwkey), "");
        Timber.i(url);

        if(url.length() > 0) {
            viewModel = appComponent.eventViewModel();
            viewModel.init(url, date);

            viewModel.getEvents().observe(this, eventList -> {
                ArrayList<Event> events = new ArrayList<>(eventList);

                if(events.size() == 0) {
                    events.add(new Event(date, date, "Es sind keine Vorlesungen vorhanden" , "", "", EventType.EMPTY));
                    Timber.i("events.size=0, adding empty event");
                }

                eventPlanAdapter.changeData(events);
            });
        } else {
            ArrayList<Event> events = new ArrayList<>();

            events.add(new Event(date, date, "Die URL deines Vorlesungsplans muss in den Einstellungen dieser App gespeichert werden." , "", "", EventType.EMPTY));

            eventPlanAdapter.changeData(events);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list, container, false);

        rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.addItemDecoration(new ItemOffsetDecoration(this.getContext(), R.dimen.item_offset));

        eventPlanAdapter = new EventPlanAdapter();
        rv.setAdapter(eventPlanAdapter);

        return view;
    }
}
