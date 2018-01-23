package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.gregoryseibert.vorlesungsplandhbw.R;
import de.gregoryseibert.vorlesungsplandhbw.model.Event;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class EventWeekAdapter extends RecyclerView.Adapter<EventWeekAdapter.EventViewHolder> {
    private Context context;
    private ArrayList<Event> eventList;

    public EventWeekAdapter(Context context){
        this.context = context;
        this.eventList = new ArrayList<>();
    }

    public void addEvents(List<Event> newEventList) {
        removeAllEvents();

        if(newEventList.size() > 0) {
            eventList.addAll(newEventList);
        } else {
            eventList.add(new Event("Freier Tag", Event.EventType.EMPTY));
        }

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

        eventViewHolder.titleText.setText(event.getTitle());

        if(event.type != Event.EventType.EMPTY) {
            eventViewHolder.timeText.setText(event.startDate.getFormatTime() + " - " + event.endDate.getFormatTime());

            if(event.type == Event.EventType.EXAM) {
                eventViewHolder.divider.setBackgroundColor(context.getResources().getColor(R.color.colorDividerExam));
            }
        } else {
            eventViewHolder.timeText.setText("");
            eventViewHolder.divider.setBackgroundColor(context.getResources().getColor(R.color.colorDividerFree));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;
        TextView titleText;
        View divider;

        EventViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            titleText = itemView.findViewById(R.id.titleText);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
