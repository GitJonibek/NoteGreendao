package com.example.jonib.notegreendao;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.jonib.notegreendao.db.DaoMaster;
import com.example.jonib.notegreendao.db.DaoSession;
import com.example.jonib.notegreendao.db.NoteDao;

/**
 * Created by jonib on 3/26/2018.
 */

public class NoteDaoApp extends Application {

    static NoteDao noteDao;

    public static NoteDao getNoteDao() {
        return noteDao;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "note-db", null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoSession daoSession = daoMaster.newSession();

        noteDao = daoSession.getNoteDao();

    }
}
