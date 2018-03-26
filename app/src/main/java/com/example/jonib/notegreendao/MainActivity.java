package com.example.jonib.notegreendao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jonib.notegreendao.adapter.RecyclerViewAdapter;
import com.example.jonib.notegreendao.db.DaoMaster;
import com.example.jonib.notegreendao.db.DaoSession;
import com.example.jonib.notegreendao.db.Note;
import com.example.jonib.notegreendao.db.NoteDao;

import org.greenrobot.greendao.Property;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter adapter;
    RecyclerView myRecyclerView;
    List<Note> list;
    NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "note-db", null);
        SQLiteDatabase database = devOpenHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();

        noteDao = daoSession.getNoteDao();
        List<Note> arrayList = noteDao.queryBuilder().list();
        for(int i=0; i<arrayList.size(); i++)
            list.add(new Note(arrayList.get(i).getId(), arrayList.get(i).getTitle(),
                    arrayList.get(i).getDescription(), arrayList.get(i).getImage(), arrayList.get(i).getDate()));
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

    public void initComponents(){
        myRecyclerView = findViewById(R.id.myRecyclerView);
        adapter = new RecyclerViewAdapter(this, list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setAdapter(adapter);
    }

}

