package com.example.jonib.notegreendao;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jonib.notegreendao.adapter.CustomViewAdapter;
import com.example.jonib.notegreendao.db.Note;
import com.example.jonib.notegreendao.db.NoteDao;
import com.example.jonib.notegreendao.model.RecyclerItemClickListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CustomViewAdapter adapter;
    RecyclerView myRecyclerView;
    List<Note> list;
    Animation upToDown;
    CoordinatorLayout mainL;
    FloatingActionButton edit_fab, delete_fab;
    AlertDialog dialog;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.LayoutManager layoutManager;
    MenuItem layoutChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        mainL.setAnimation(upToDown);


        list = NoteDaoApp.getNoteDao().queryBuilder().where(NoteDao.Properties.Id.gt(0L)).list();

        adapter = new CustomViewAdapter(MainActivity.this, list);
        myRecyclerView.setLayoutManager(staggeredGridLayoutManager);
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
                    public void onLongItemClick(View view, final int position) {
                        showPopUpActionWindow();
                        edit_fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                                intent.putExtra("ID", list.get(position).getId());
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                        delete_fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                NoteDaoApp.getNoteDao().delete(list.get(position));
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                    }
                }));

    }

    public void initComponents(){
        myRecyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        mainL = findViewById(R.id.mainLayoutId);
        upToDown = AnimationUtils.loadAnimation(this, R.anim.main_anim);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        linearLayoutManager = new LinearLayoutManager(this);
        layoutChanger = findViewById(R.id.layoutManagerChanger);
    }

    private void showPopUpActionWindow() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        @SuppressLint("InflateParams")
        View myView = getLayoutInflater().inflate(R.layout.onlong_item_listener_popup, null);
        edit_fab = myView.findViewById(R.id.edit_action_fab);
        delete_fab = myView.findViewById(R.id.delete_action_fab);
        mBuilder.setView(myView);
        dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addNoteFunction(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.layoutManagerChanger:
                Toast.makeText(this, "Hello, I've to change it!", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

