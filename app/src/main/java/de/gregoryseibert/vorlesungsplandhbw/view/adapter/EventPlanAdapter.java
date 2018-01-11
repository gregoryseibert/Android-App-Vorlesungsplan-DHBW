package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.EmptyEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.Event;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.ExamEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.LectureEvent;
import de.gregoryseibert.vorlesungsplandhbw.service.model.event.PauseEvent;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class EventPlanAdapter extends RecyclerView.Adapter<EventPlanAdapter.EventViewHolder> {
    private ArrayList<Event> eventList;

    public EventPlanAdapter(ArrayList<Event> eventList){
        this.eventList = eventList;
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
        eventViewHolder.titleText.setText(eventList.get(i).title);

        Event event = eventList.get(i);

        if(event instanceof EmptyEvent) {
            ((ViewGroup) eventViewHolder.dateText.getParent()).removeView(eventViewHolder.dateText);
            ((ViewGroup) eventViewHolder.roomText.getParent()).removeView(eventViewHolder.roomText);
            //((ViewGroup) eventViewHolder.lecturerText.getParent()).removeView(eventViewHolder.lecturerText);
        } else {
            eventViewHolder.dateText.setText(event.startDate.getFormatTime() + " - " + event.endDate.getFormatTime());

            if(event instanceof LectureEvent || event instanceof ExamEvent) {
                eventViewHolder.roomText.setText(event.room);
                //eventViewHolder.lecturerText.setText(eventList.get(i).getLecturer());
            } else if(event instanceof PauseEvent) {
                ((ViewGroup) eventViewHolder.roomText.getParent()).removeView(eventViewHolder.roomText);
                //((ViewGroup) eventViewHolder.lecturerText.getParent()).removeView(eventViewHolder.lecturerText);
            }
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
