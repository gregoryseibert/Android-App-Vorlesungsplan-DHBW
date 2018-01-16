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

public class EventDayAdapter extends RecyclerView.Adapter<EventDayAdapter.EventViewHolder> {
    private ArrayList<Event> eventList;

    public EventDayAdapter(){
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_day, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Event event = eventList.get(i);

        eventViewHolder.titleText.setText(event.title);

        if(event.type != EventType.EMPTY) {
            eventViewHolder.dateText.setVisibility(View.VISIBLE);
            eventViewHolder.roomText.setVisibility(View.VISIBLE);

            eventViewHolder.dateText.setText(event.startDate.getFormatTime() + " - " + event.endDate.getFormatTime());
            eventViewHolder.roomText.setText(event.room);
        } else {
            eventViewHolder.dateText.setVisibility(View.GONE);
            eventViewHolder.roomText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView titleText;
        TextView dateText;
        TextView roomText;

        EventViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cv);
            titleText = itemView.findViewById(R.id.titleText);
            dateText = itemView.findViewById(R.id.timeText);
            roomText = itemView.findViewById(R.id.roomText);
        }
    }
}
