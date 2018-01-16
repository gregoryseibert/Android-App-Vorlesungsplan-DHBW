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

public class EventPlanAdapter extends RecyclerView.Adapter<EventPlanAdapter.EventViewHolder> {
    private ArrayList<Event> eventList;

    public EventPlanAdapter(){
        this.eventList = new ArrayList<>();
    }

    public void changeData(ArrayList<Event> eventList) {
        this.eventList.clear();
        this.eventList.addAll(eventList);

        Timber.i("changeData: " + this.eventList.size());

        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Event event = eventList.get(i);

        eventViewHolder.titleText.setText(event.title);

        if(event.type != EventType.EMPTY) {
            eventViewHolder.dateText.setText(event.startDate.getFormatTime() + " - " + event.endDate.getFormatTime());

            eventViewHolder.roomText.setText(event.room);
        } else {
            ((ViewGroup) eventViewHolder.dateText.getParent()).removeView(eventViewHolder.dateText);
            ((ViewGroup) eventViewHolder.roomText.getParent()).removeView(eventViewHolder.roomText);
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
