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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonib.notegreendao.R;
import com.example.jonib.notegreendao.db.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonib on 3/26/2018.
 */

public class CustomViewAdapter extends BaseAdapter {

    private List<Note> list;
    private Context context;
    private LayoutInflater inflater;
    private int lastPosition = -1;

    public CustomViewAdapter(Context context, List<Note> list){
        this.context = context;
        this.list = list;
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
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Note note = list.get(position);

        CustomViewHolder holder;
        final View result;

        if(view == null){
            holder = new CustomViewHolder();
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_items_layout, viewGroup, false);

            holder.title = view.findViewById(R.id.itemTitle);
            holder.description = view.findViewById(R.id.itemDescription);
            holder.imageView = view.findViewById(R.id.itemImage);
            holder.date = view.findViewById(R.id.itemDate);

            result = view;

            view.setTag(holder);

        }else{
            holder = (CustomViewHolder) view.getTag();
            result = view;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        holder.title.setText(note.getTitle());
        if(note.getDescription() != null)
            holder.description.setText(note.getDescription());
        else
            holder.description.setText("No Description!");
        holder.imageView.setImageBitmap(loadImageFromStorage(note.getImagePath(), note.getImageName()));
        holder.date.setText(note.getDate());

        return result;
    }

    public class CustomViewHolder {
        public TextView title, description, date;
        public ImageView imageView;
    }

}
