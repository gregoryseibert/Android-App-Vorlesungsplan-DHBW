package de.gregoryseibert.vorlesungsplandhbw.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.EventType;
import de.gregoryseibert.vorlesungsplandhbw.service.model.SimpleDate;
import de.gregoryseibert.vorlesungsplandhbw.view.adapter.EventPlanAdapter;
import de.gregoryseibert.vorlesungsplandhbw.viewmodel.EventViewModel;

/**
 * Created by Gregory Seibert on 11.01.2018.
 */

public class EventListFragment extends Fragment {
    private RecyclerView rv;

    private SimpleDate date;
    private EventViewModel viewModel;

    private String url;

    public EventListFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        date = (SimpleDate) getArguments().getSerializable("date");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        url = settings.getString(getString(R.string.key_dhbwkey), "");

        Log.e("onActivityCreated", date.getFormatDate());

        viewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        viewModel.init(this.getContext(), url, date);

        viewModel.getEvents().observe(this, eventList -> {
            if(eventList != null) {
                ArrayList<Event> events = new ArrayList<>(eventList);

                if(events.size() == 0) {
                    events.add(new Event(date, date, "Es sind keine Vorlesungen vorhanden" , "", "", EventType.EMPTY));
                }

                EventPlanAdapter adapter = new EventPlanAdapter(events);
                rv.setAdapter(adapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list, container, false);

        rv = view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this.getContext(), R.dimen.item_offset);
        rv.addItemDecoration(itemDecoration);

        return view;
    }

    class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, mItemOffset);
        }
    }
}
