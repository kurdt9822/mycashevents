package com.example.mycashcalc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBhelper extends SQLiteOpenHelper {

    public String databasePath = "";

    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        databasePath = context.getDatabasePath(name).getAbsolutePath();
        Log.d(Main.LOG_TAG, databasePath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table events ("
                + "event_id integer primary key,"
                + "event_date default current_timestamp,"
                + "event_name text" + ");");

        db.execSQL("create table purchases ("
                + "purchase_id integer primary key,"
//                + "FOREIGN KEY (event_id) REFERENCES events (event_id)"
//                + "ON DELETE CASCADE ON UPDATE NO ACTION"
                + "event_id integer,"
                + "purchase_value text,"
                + "purchase_dir text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
