package de.gregoryseibert.vorlesungsplandhbw.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_day, viewGroup, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Event event = eventList.get(i);

        eventViewHolder.titleText.setText(event.title);

        if(event.type != Event.EventType.EMPTY) {
            eventViewHolder.timeTextStart.setVisibility(View.VISIBLE);
            eventViewHolder.timeTextEnd.setVisibility(View.VISIBLE);
            eventViewHolder.roomText.setVisibility(View.VISIBLE);
            //eventViewHolder.dividerLayout.setVisibility(View.VISIBLE);

            eventViewHolder.timeTextStart.setText(String.format("%s", event.startDate.getFormatTime()));
            eventViewHolder.timeTextEnd.setText(String.format("%s", event.endDate.getFormatTime()));
            eventViewHolder.roomText.setText(event.room);
        } else {
            eventViewHolder.timeTextStart.setVisibility(View.GONE);
            eventViewHolder.timeTextEnd.setVisibility(View.GONE);
            eventViewHolder.roomText.setVisibility(View.GONE);
            //eventViewHolder.dividerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView timeTextStart, timeTextEnd;
        TextView roomText;

        EventViewHolder(View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.titleText);
            timeTextStart = itemView.findViewById(R.id.timeTextStart);
            timeTextEnd = itemView.findViewById(R.id.timeTextEnd);
            roomText = itemView.findViewById(R.id.roomText);
        }
    }
}
