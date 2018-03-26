package com.example.jonib.notegreendao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTE".
*/
public class NoteDao extends AbstractDao<Note, Long> {

    public static final String TABLENAME = "NOTE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "Title", false, "TITLE");
        public final static Property Description = new Property(2, String.class, "Description", false, "DESCRIPTION");
        public final static Property Image = new Property(3, byte[].class, "Image", false, "IMAGE");
        public final static Property Date = new Property(4, String.class, "Date", false, "DATE");
    }


    public NoteDao(DaoConfig config) {
        super(config);
    }
    
    public NoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TITLE\" TEXT NOT NULL ," + // 1: Title
                "\"DESCRIPTION\" TEXT," + // 2: Description
                "\"IMAGE\" BLOB," + // 3: Image
                "\"DATE\" TEXT);"); // 4: Date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(3, Description);
        }
 
        byte[] Image = entity.getImage();
        if (Image != null) {
            stmt.bindBlob(4, Image);
        }
 
        String Date = entity.getDate();
        if (Date != null) {
            stmt.bindString(5, Date);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(3, Description);
        }
 
        byte[] Image = entity.getImage();
        if (Image != null) {
            stmt.bindBlob(4, Image);
        }
 
        String Date = entity.getDate();
        if (Date != null) {
            stmt.bindString(5, Date);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Note readEntity(Cursor cursor, int offset) {
        Note entity = new Note( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // Title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Description
            cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3), // Image
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // Date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Note entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImage(cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Note entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Note entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Note entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}