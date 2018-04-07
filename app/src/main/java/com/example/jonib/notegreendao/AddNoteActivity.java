package com.example.jonib.notegreendao;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.jonib.notegreendao.db.Note;
import com.example.jonib.notegreendao.db.NoteDao;
import com.example.jonib.notegreendao.service.AlarmReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class AddNoteActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, OnTimeSetListener{

    EditText title, description, date_time_set;
    FloatingActionButton btnChooseImage, btnAddNote;
    ImageView imageView;
    LinearLayout upToDown, downToUp;
    Animation animationUp, animationDown;
    BootstrapButton date_time_picker;
    TextView date_and_time_text_id;
    Switch alarm_switch_on_off;
    Calendar calendar;

    private final int REQUEST_CODE_GALLERY = 999;
    private int day, month, year, hour, minute;
    private int dayFinal, monthFinel, yearFinel, hourFinel, minuteFinel;
    private boolean reminderIsSet;
    private String fileName;
    private String AM_PM = "", hourStr = "", minuteStr = "", yearStr = "", monthStr = "", dayStr = "";

    public AddNoteActivity(String fileName){ this.fileName = fileName; }
    public AddNoteActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initComponents();

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        final String date = df.format(Calendar.getInstance().getTime());

        final int[] array = new int[]{
                getResources().getColor(R.color.red), getResources().getColor(R.color.purple),
                getResources().getColor(R.color.indigo), getResources().getColor(R.color.grey),
                getResources().getColor(R.color.green), getResources().getColor(R.color.dark_blue),
                getResources().getColor(R.color.cyan), getResources().getColor(R.color.brown), getResources().getColor(R.color.blue_grey)};

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(bundle.containsKey("ID")){
                long id = (long) getIntent().getExtras().get("ID");
                final Note note = NoteDaoApp.getNoteDao().queryBuilder().where(NoteDao.Properties.Id.eq(id)).unique();

                final Bitmap bitmap = loadImageFromStorage(note.getImagePath(), note.getImageName());
                RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                rounded.setCircular(true);

                imageView.setImageDrawable(rounded);
                title.setText(note.getTitle());
                description.setText(note.getDescription());

                btnAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        note.setTitle(title.getText().toString());
                        note.setDescription(description.getText().toString());
                        note.setImagePath(saveToInternalStorage(getImageBitmap(imageView)));
                        note.setImageName(fileName);
                        note.setItemColor(array[new Random().nextInt(array.length)]);
                        note.setDate(date);
                        note.setReminderNotif(reminderIsSet);

                        NoteDaoApp.getNoteDao().update(note);

                        setResult(RESULT_OK);
                        Toast.makeText(getApplicationContext(), "Updated Successfully!", Toast.LENGTH_LONG).show();

                        clearComponents();
                    }
                });
            }

        }else{

            btnAddNote.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setDescription(description.getText().toString());
                    note.setImagePath(saveToInternalStorage(((RoundedBitmapDrawable) imageView.getDrawable()).getBitmap()));
                    note.setImageName(fileName);
                    note.setItemColor(array[new Random().nextInt(array.length)]);
                    note.setDate(date.toString());
                    note.setReminderNotif(reminderIsSet);

                    NoteDaoApp.getNoteDao().insert(note);

                    setResult(RESULT_OK);
                    Toast.makeText(getApplicationContext(), "Added Successfully!", Toast.LENGTH_LONG).show();



                    clearComponents();
                }
            });
        }

        reminderIsSet = alarm_switch_on_off.onCheckIsTextEditor();
        alarm_switch_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if(Objects.equals(yearStr, "") || Objects.equals(monthStr, "") || Objects.equals(dayStr, "")) {
                        alarm_switch_on_off.setChecked(false);
                        reminderIsSet = false;
                        Toast.makeText(getApplicationContext(), "Please, set the date & time!--> " + reminderIsSet, Toast.LENGTH_LONG).show();
                    }else{
                        date_and_time_text_id.setText("Reminder set on " + yearStr + "." + monthStr + "." + dayStr +
                                ", " + hourStr + ":" + minuteStr + " " + AM_PM);



                    }
                } else if(!isChecked) {

                    alarm_switch_on_off.setChecked(false);
                    reminderIsSet = false;

                    date_time_set.setText("");
                    yearStr = "";
                    monthStr = "";
                    dayStr = "";

                    Toast.makeText(getApplicationContext(), "--> " + reminderIsSet, Toast.LENGTH_LONG).show();
                    date_and_time_text_id.setText("Reminder is not set yet!");
                }
            }
        });

        upToDown.setAnimation(animationUp);
        downToUp.setAnimation(animationDown);

    }

    public void initComponents(){
        title = findViewById(R.id.note_title);
        description = findViewById(R.id.note_desc);
        btnChooseImage = findViewById(R.id.add_image_fab);
        btnAddNote = findViewById(R.id.save_note_fab);
        imageView = findViewById(R.id.image_icon);
        alarm_switch_on_off = findViewById(R.id.switch_alarm_on_off);
        calendar = Calendar.getInstance();

        date_time_set = findViewById(R.id.date_time_field_id);
        date_time_picker = findViewById(R.id.date_time_picker);
        date_and_time_text_id = findViewById(R.id.date_and_time_text_id);

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

    public void clearComponents(){
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.default_background_image));
        title.setText("");
        description.setText("");
    }

    public void pickTheDateAndTime(View view) {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setReminder(long timeInMillis){

        final Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);

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

    private String getPictureName(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new java.util.Date());
        return  "Image_" + timestamp + ".jpg";
    }

    private Bitmap getImageBitmap(ImageView imageView){
        return ((RoundedBitmapDrawable) imageView.getDrawable()).getBitmap();
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinel = datePicker.getYear();
        monthFinel = datePicker.getMonth() + 1;
        dayFinal = datePicker.getDayOfMonth();

        yearStr = String.valueOf(yearFinel);
        if (monthFinel < 10) monthStr = "0" + monthFinel;
        else monthStr = String.valueOf(monthFinel);
        if (dayFinal < 10) dayStr = "0" + dayFinal;
        else dayStr = String.valueOf(dayFinal);

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinel = timePicker.getHour();
        minuteFinel = timePicker.getMinute();

        Calendar c = Calendar.getInstance();
        c.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                timePicker.getHour(),
                timePicker.getMinute(),
                0);

        calendar.set(
                yearFinel,
                monthFinel,
                dayFinal,
                hourFinel,
                minuteFinel,
                0);

        setReminder(calendar.getTimeInMillis());

        hourStr = String.valueOf(hourFinel);
        minuteStr = String.valueOf(minuteFinel);

        if(hourFinel > 12) {
            hourStr = String.valueOf(hourFinel - 12);
            if((hourFinel-12) < 10) {
                hourStr = "0" + hourStr;
            }
            AM_PM = "PM";

        } else {
            AM_PM = "AM";
            if(hourFinel < 10) {
                hourStr = "0"+hourStr;
            }
            if(minuteFinel < 10) {
                minuteStr = "0"+minuteStr;
            }
        }

        date_time_set.setText(yearStr+ "." +monthStr+ "." +dayStr+", " +hourStr+ ":" +minuteStr+ " " +AM_PM);
        alarm_switch_on_off.setChecked(true);
        reminderIsSet = true;

        Toast.makeText(this, "Time set successfully!", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = null;
                if (uri != null) { inputStream = getContentResolver().openInputStream(uri); }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                rounded.setCircular(true);
                imageView.setImageDrawable(rounded);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

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

}
