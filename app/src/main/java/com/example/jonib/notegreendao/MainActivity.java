package com.example.jonib.notegreendao;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import com.example.jonib.notegreendao.adapter.CustomViewAdapter;
import com.example.jonib.notegreendao.db.DaoMaster;
import com.example.jonib.notegreendao.db.DaoSession;
import com.example.jonib.notegreendao.db.Note;
import com.example.jonib.notegreendao.db.NoteDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    CustomViewAdapter adapter;
    ListView myListView;
    List<Note> list;

    private NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "note-db", null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();
        noteDao = daoSession.getNoteDao();

        myListView = findViewById(R.id.myGridView);
        list = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(1)).list();
        adapter = new CustomViewAdapter(this, R.layout.list_items_layout, list);
        myListView.setAdapter(adapter);

        Log.d(noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(1)).toString(), " -------------------------------");

        List<Note> myList = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(1)).list();

        list.add(new Note(
                myList.get(0).getId(),
                myList.get(0).getTitle(),
                myList.get(0).getDescription(),
                myList.get(0).getImage(),
                myList.get(0).getDate()));

        adapter.notifyDataSetChanged();

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

