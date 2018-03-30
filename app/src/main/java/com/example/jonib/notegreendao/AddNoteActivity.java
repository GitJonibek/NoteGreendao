package com.example.jonib.notegreendao;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.jonib.notegreendao.db.Note;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    EditText title, description;
    FloatingActionButton btnChooseImage, btnAddNote;
    ImageView imageView;
    LinearLayout upToDown, downToUp;
    Animation animationUp, animationDown;

    private final int REQUEST_CODE_GALLERY = 999;
    private String fileName;

    public AddNoteActivity(String fileName){ this.fileName = fileName; }

    public AddNoteActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initComponents();

        upToDown.setAnimation(animationUp);
        downToUp.setAnimation(animationDown);

    }

    public void initComponents(){
        title = findViewById(R.id.note_title);
        description = findViewById(R.id.note_desc);
        btnChooseImage = findViewById(R.id.add_image_fab);
        btnAddNote = findViewById(R.id.save_note_fab);
        imageView = findViewById(R.id.image_icon);
        // Layouts
        upToDown = findViewById(R.id.upToDownLayout);
        downToUp = findViewById(R.id.downToUpLayout);
        // Animation
        animationUp = AnimationUtils.loadAnimation(this, R.anim.up_down_layout_anim);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.down_up_layout_anim);
    }

    public void chooseImage(View view) {
        ActivityCompat.requestPermissions(
                AddNoteActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
    }

    //*** Fix some issues here!!!!!!!
    public void addNoteFunction(View view) {

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        Note note = new Note();
        note.setTitle(title.getText().toString());
        note.setDescription(description.getText().toString());
        note.setImagePath(saveToInternalStorage(getImageBitmap(imageView)));
        note.setImageName(fileName);
        note.setDate(date.toString());

        NoteDaoApp.getNoteDao().insert(note);

        setResult(RESULT_OK);
        Toast.makeText(this, "Added Successfully!", Toast.LENGTH_LONG).show();

        clearComponents();

    }

    public void clearComponents(){
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.default_background_image));
        title.setText("");
        description.setText("");
    }

    private String saveToInternalStorage(Bitmap bitmapImage){

        fileName = getPictureName();
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap getImageBitmap(ImageView imageView){
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    private String getPictureName(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new java.util.Date());
        return  "Image_" + timestamp + ".jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have a permission to access file location!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {

                InputStream inputStream = null;
                if (uri != null) {
                    inputStream = getContentResolver().openInputStream(uri);
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] imageToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}
