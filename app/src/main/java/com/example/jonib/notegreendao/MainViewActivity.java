package com.example.jonib.notegreendao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jonib.notegreendao.db.Note;
import com.example.jonib.notegreendao.db.NoteDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MainViewActivity extends AppCompatActivity {

    private NoteDaoApp noteDaoApp;
    private TextView title, description;
    private ImageView imageView;
    private long id = 0L;
    private List<Note> list;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        initComponents();

        list = noteDaoApp.getNoteDao().queryBuilder().where(NoteDao.Properties.Id.eq(id)).list();
        setMainViews(list);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainViewActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_image_popup, null);
                ImageView imageView = view1.findViewById(R.id.popup_imageView);
                imageView.setImageBitmap(loadImageFromStorage(list.get(0).getImagePath(), list.get(0).getImageName()));
                mBuilder.setView(view1);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    public void initComponents(){
        noteDaoApp = new NoteDaoApp();
        title = findViewById(R.id.mainViewTitle);
        description = findViewById(R.id.mainViewDescription);
        imageView = findViewById(R.id.mainViewImage);

        id = (long) getIntent().getExtras().get("ID");
    }

    public void setMainViews(List<Note> list){
        bitmap = loadImageFromStorage(list.get(0).getImagePath(), list.get(0).getImageName());
        title.setText(list.get(0).getTitle());
        description.setText(list.get(0).getDescription());
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap loadImageFromStorage(String path, String fileName) {
        Bitmap bitmap = null;
        try {
            File file = new File(path, fileName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(), "Couldn't load image!", Toast.LENGTH_LONG).show();
        }
        return bitmap;
    }

}
