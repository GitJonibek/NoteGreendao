package com.example.jonib.notegreendao.popup_notifier;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jonib.notegreendao.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PopUpActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        imageView = findViewById(R.id.popup_imageView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int wigth = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        String path = getIntent().getExtras().getString("path");
        String name = getIntent().getExtras().getString("name");

        getWindow().setLayout((int) (wigth*.8), (int) (height*.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = 0;
        params.y = -20;
        params.gravity = Gravity.CENTER;

        getWindow().setAttributes(params);
        imageView.setImageBitmap(loadImageFromStorage(path, name));

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
