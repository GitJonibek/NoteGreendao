package com.example.jonib.notegreendao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonib.notegreendao.R;
import com.example.jonib.notegreendao.db.Note;

import java.util.List;

/**
 * Created by jonib on 3/26/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    List<Note> list;
    Context context;

    public RecyclerViewAdapter(Context context, List<Note> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_layout, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Note note = list.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.date.setText(note.getDate());

        byte[] noteImage = note.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(noteImage, 0, noteImage.length);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, date;
        public ImageView imageView;

        public CustomViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.itemTitle);
            description = view.findViewById(R.id.itemDescription);
            date = view.findViewById(R.id.itemDate);
            imageView = view.findViewById(R.id.itemImage);
        }
    }
}
