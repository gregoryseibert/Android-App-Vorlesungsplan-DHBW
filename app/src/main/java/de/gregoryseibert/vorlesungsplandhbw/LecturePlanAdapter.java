package de.gregoryseibert.vorlesungsplandhbw;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.gregoryseibert.vorlesungsplandhbw.data_model.Lecture;

/**
 * Created by Gregory Seibert on 09.01.2018.
 */

public class LecturePlanAdapter extends RecyclerView.Adapter<LecturePlanAdapter.LectureViewHolder> {
    private ArrayList<Lecture> lectureList;

    LecturePlanAdapter( ArrayList<Lecture> lectureList){
        this.lectureList = lectureList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        LectureViewHolder lvh = new LectureViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(LectureViewHolder lectureViewHolder, int i) {
        lectureViewHolder.titleText.setText(lectureList.get(i).getTitle());
        lectureViewHolder.dateText.setText(lectureList.get(i).getCombinedDate());
        lectureViewHolder.roomText.setText(lectureList.get(i).getRoom());
        lectureViewHolder.lecturerText.setText(lectureList.get(i).getLecturer());
    }

    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView titleText;
        TextView dateText;
        TextView roomText;
        TextView lecturerText;

        LectureViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);

            titleText = itemView.findViewById(R.id.titleText);
            dateText = itemView.findViewById(R.id.dateText);
            roomText = itemView.findViewById(R.id.roomText);
            lecturerText = itemView.findViewById(R.id.lecturerText);
        }
    }
}
