package com.example.jonib.notegreendao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.jonib.notegreendao.adapter.CustomViewAdapter;
import com.example.jonib.notegreendao.db.Note;
import com.example.jonib.notegreendao.db.NoteDao;
import com.example.jonib.notegreendao.model.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CustomViewAdapter adapter;
    RecyclerView myRecyclerView;
    List<Note> list;

    private NoteDaoApp noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        list = noteDao.getNoteDao().queryBuilder().where(NoteDao.Properties.Id.gt(0L)).list();

        adapter = new CustomViewAdapter(MainActivity.this, R.layout.list_items_layout, list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        myRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, myRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
                        intent.putExtra("ID", list.get(position).getId());
                        /*intent.putExtra("title", list.get(position).getTitle());
                        intent.putExtra("description", list.get(position).getDescription());
                        intent.putExtra("imagePath", list.get(position).getImagePath());
                        intent.putExtra("imageName", list.get(position).getImageName());
                        intent.putExtra("date", list.get(position).getDate());*/
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

    }

    public void initComponents(){
        myRecyclerView = findViewById(R.id.recycler_view);
        noteDao = new NoteDaoApp();
        list = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        onStart();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        if(id == R.id.addNoteId){
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}

