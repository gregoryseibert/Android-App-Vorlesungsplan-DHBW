package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.Event.EventType;
import timber.log.Timber;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class EventWeekAdapter extends RecyclerView.Adapter<EventWeekAdapter.EventViewHolder> {
    private ArrayList<Event> eventList;

    public EventWeekAdapter(){
        this.eventList = new ArrayList<>();
    }

    public void addEvents(ArrayList<Event> eventList) {
        removeAllEvents();

        this.eventList.addAll(eventList);

        Timber.i("changeData: " + this.eventList.size());

        notifyDataSetChanged();
    }

    public void removeAllEvents() {
        eventList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_week_day, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Event event = eventList.get(i);

        if(event.type != EventType.EMPTY) {
            eventViewHolder.timeText.setText(event.startDate.getFormatTime() + " - " + event.endDate.getFormatTime());
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;

        EventViewHolder(View itemView) {
            super(itemView);

            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
