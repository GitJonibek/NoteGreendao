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
import android.widget.Toast;

import com.example.jonib.notegreendao.R;
import com.example.jonib.notegreendao.db.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by jonib on 3/26/2018.
 */

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.CustomViewHolder> {

    List<Note> list;
    Context context;

    public CustomViewAdapter(Context context, List<Note> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_items_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Note note = list.get(position);

        holder.title.setText(note.getTitle());
        if(note.getDescription() != null)
            holder.description.setText(note.getDescription());
        else
            holder.description.setText("No Description!");
        holder.imageView.setImageBitmap(loadImageFromStorage(note.getImagePath(), note.getImageName()));
        holder.date.setText(note.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Bitmap loadImageFromStorage(String path, String fileName) {
        Bitmap bitmap = null;
        try {
            File file = new File(path, fileName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e){
            Toast.makeText(context, "Couldn't load image!", Toast.LENGTH_LONG).show();
        }
        return bitmap;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description, date;
        public ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            date = itemView.findViewById(R.id.itemDate);
        }
    }

}
