package com.example.mycashcalc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBhelper extends SQLiteOpenHelper {

    public String databasePath = "";

    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        databasePath = context.getDatabasePath(name).getAbsolutePath();
        if (Main.DB_PATH != null) Main.DB_PATH = databasePath;
//        Log.d(Main.LOG_TAG, Main.DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+Main.EVENTS_TABLE+" ("
                + "event_id integer primary key,"
                + "event_date default current_timestamp,"
                + "event_name text" + ");");

        db.execSQL("create table "+Main.PURCHASES_TABLE+" ("
                + "purchase_id integer primary key,"
//                + "FOREIGN KEY (event_id) REFERENCES events (event_id)"
//                + "ON DELETE CASCADE ON UPDATE NO ACTION"
                + "event_id integer,"
                + "purchase_name text,"
                + "purchase_value text,"
                + "purchase_dir text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
