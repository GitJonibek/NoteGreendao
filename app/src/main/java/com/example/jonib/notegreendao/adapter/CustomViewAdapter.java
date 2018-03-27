package com.example.jonib.notegreendao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonib.notegreendao.R;
import com.example.jonib.notegreendao.db.Note;

import java.util.List;

/**
 * Created by jonib on 3/26/2018.
 */

public class CustomViewAdapter extends BaseAdapter {

    List<Note> list;
    Context context;
    private int layout;

    public CustomViewAdapter(Context context, int layout, List<Note> list){
        this.list = list;
        this.context = context;
        this.layout = layout;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        CustomViewHolder viewHolder = new CustomViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            viewHolder.title = row.findViewById(R.id.itemTitle);
            viewHolder.description = row.findViewById(R.id.itemDescription);
            viewHolder.imageView = row.findViewById(R.id.itemImage);
            viewHolder.date = row.findViewById(R.id.itemDate);
            row.setTag(viewHolder);

        }else{
            viewHolder = (CustomViewHolder) row.getTag();
        }

        Note note = list.get(i);
        viewHolder.title.setText(note.getTitle().toString());
        if(note.getDescription() == null)
            viewHolder.description.setText("No description!");
        else
            viewHolder.description.setText(note.getDescription().toString());

        byte[] noteImage = note.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(noteImage, 0, noteImage.length);
        viewHolder.imageView.setImageBitmap(bitmap);

        viewHolder.date.setText(note.getDate().toString());

        return view;
    }

    public class CustomViewHolder {
        public TextView title, description, date;
        public ImageView imageView;
    }
}
