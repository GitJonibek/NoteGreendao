package com.example.jonib.notegreendao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private List<Note> list;
    private Context context;
    private final LayoutInflater inflater;
    private int layout;

    public CustomViewAdapter(Context context, int layout, List<Note> list){
        this.layout = layout;
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(layout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;

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

        /*Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;
        holder.imageView.setTag(position);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description, date;
        public ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            imageView = itemView.findViewById(R.id.itemImage);
            date = itemView.findViewById(R.id.itemDate);
        }
    }

}
