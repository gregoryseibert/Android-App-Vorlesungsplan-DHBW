package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventDayAdapter;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventDayViewModel;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventListDayFragment extends BaseFragment {
    private AppComponent appComponent;

    private RecyclerView rv;

    private EventDayAdapter eventDayAdapter;

    private SimpleDate date;
    private EventDayViewModel viewModel;

    private String url;

    public EventListDayFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appComponent = ((MainActivity) getActivity()).getAppComponent();

        date = (SimpleDate) getArguments().getSerializable("date");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        url = settings.getString(getString(R.string.key_dhbwkey), "");

        if(url.length() > 0) {
            viewModel = ViewModelProviders.of(this).get(EventDayViewModel.class);
            viewModel.setEventRepository(appComponent.eventRepository());
            viewModel.setExecutorService(appComponent.executorService());
            viewModel.init(url, date);

            viewModel.getEvents().observe(this, eventList -> {
                ArrayList<Event> events = new ArrayList<>(eventList);

                if(events.size() == 0) {
                    events.add(new Event("Es sind keine Vorlesungen vorhanden" , EventType.EMPTY));
                    Timber.i("events.size=0, adding empty event");
                }

                eventDayAdapter.addEvents(events);
            });
        } else {
            ArrayList<Event> events = new ArrayList<>();

            events.add(new Event("Die URL deines Vorlesungsplans muss in den Einstellungen dieser App gespeichert werden." , EventType.EMPTY));

            eventDayAdapter.addEvents(events);
        }
    }

    public void setDate(SimpleDate date) {
        this.date = date;
        eventDayAdapter.removeAllEvents();
        viewModel.init(url, date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_day_list, container, false);

        rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.addItemDecoration(new ItemOffsetDecoration(this.getContext(), R.dimen.item_offset));

        eventDayAdapter = new EventDayAdapter();
        rv.setAdapter(eventDayAdapter);

        return view;
    }
}
