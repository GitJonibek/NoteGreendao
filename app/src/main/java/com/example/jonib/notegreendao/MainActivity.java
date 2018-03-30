package com.example.jonib.notegreendao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
    Animation upToDown;
    CoordinatorLayout mainL;
    FloatingActionButton edit_fab, delete_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        mainL.setAnimation(upToDown);
        list = NoteDaoApp.getNoteDao().queryBuilder().where(NoteDao.Properties.Id.gt(0L)).list();

        adapter = new CustomViewAdapter(MainActivity.this, R.layout.list_items_layout, list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        myRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                myRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
                        intent.putExtra("ID", list.get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        showPopUpActionWindow();
                    }
                }));


    }

    private void showPopUpActionWindow() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        @SuppressLint("InflateParams")
        View myView = getLayoutInflater().inflate(R.layout.onlong_item_listener_popup, null);
        edit_fab = myView.findViewById(R.id.edit_action_fab);
        delete_fab = myView.findViewById(R.id.delete_action_fab);
        mBuilder.setView(myView);
        AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void initComponents(){
        myRecyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        mainL = findViewById(R.id.mainLayoutId);
        upToDown = AnimationUtils.loadAnimation(this, R.anim.main_anim);
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

    public void addNoteFunction(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}

