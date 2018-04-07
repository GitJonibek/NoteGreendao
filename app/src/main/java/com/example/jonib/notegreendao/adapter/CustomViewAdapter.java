package com.example.jonib.notegreendao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonib.notegreendao.R;
import com.example.jonib.notegreendao.db.Note;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by jonib on 3/26/2018.
 */

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.CustomViewHolder> {

    private List<Note> list;
    private Context context;
    private final LayoutInflater inflater;

    public CustomViewAdapter(Context context, List<Note> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_items_layout, parent, false);
        return new CustomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Note note = list.get(position);

        String title = note.getTitle();
        char t;
        if(title == null || Objects.equals(title, "")) {
            t = 'N';
            title += "No Title!";
        }else {
            t = title.charAt(0);
        }

        holder.title.setText(title);
        holder.description.setText(note.getDescription());
        if(note.getReminderNotif())
            holder.circleText.setBackgroundResource(R.drawable.alarm_icon);
        holder.date.setText(note.getDate());

        holder.cardView.setCardBackgroundColor(note.getItemColor());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, date, circleText;
        RoundedLetterView circle;
        CardView cardView;

        CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemReminder);
            circleText = itemView.findViewById(R.id.circle_textview_id);
            date = itemView.findViewById(R.id.itemDate);
            cardView = itemView.findViewById(R.id.my_cardview_id);
        }
    }

}
