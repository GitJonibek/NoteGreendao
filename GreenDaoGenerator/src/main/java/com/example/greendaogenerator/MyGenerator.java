package com.example.greendaogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {

    public static void main(String[] args) throws Exception{

        Schema schema = new Schema(1, "com.example.jonib.notegreendao.db");
        schema.enableKeepSectionsByDefault();

        addTable(schema);

        try{
            new DaoGenerator().generateAll(schema, "../app/src/main/java");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void addTable(final Schema schema){
        addNoteEntiries(schema);
    }

    private static Entity addNoteEntiries(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("Title").notNull();
        note.addStringProperty("Description");
        note.addByteArrayProperty("Image");
        note.addStringProperty("Date");
        return note;
    }

}
