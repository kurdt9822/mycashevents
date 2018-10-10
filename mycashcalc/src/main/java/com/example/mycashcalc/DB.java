package com.example.mycashcalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DB {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "mycashcalc";
    static final String EVENTS_TABLE = "events";
    static final String PURCHASES_TABLE = "purchases";
//    static String DB_PATH = "";

//    static final String EVENT_ID = "event_id";
    static final String EVENT_ID = "event_id";
    static final String EVENT_DATE = "event_date";
    static final String EVENT_NAME = "event_name";

    static final String PURCHASE_ID = "purchase_id";
    static final String PURCHASE_NAME = "purchase_name";
    static final String PURCHASE_VALUE = "purchase_value";
    static final String PURCHASE_DIR = "purchase_dir";

    public DB(Context ctx){
        myCtx = ctx;
//        DB_PATH = ctx.getDatabasePath(DB_NAME).getAbsolutePath();
    }

    private final Context myCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public void open(){
        if (mDBHelper == null){
            mDBHelper = new DBHelper(myCtx, DB_NAME, null, DB_VERSION);
            mDB = mDBHelper.getWritableDatabase();
        } else {
            if (mDB == null){
                mDB = mDBHelper.getWritableDatabase();
            }
        }
    }

    public String getDbPath(){
        return myCtx.getDatabasePath(DB_NAME).getAbsolutePath();
    }

    public Cursor getData(String tabName, String[] columns, String whereStr, String[] whereArgs, String groupBy, String having, String orderBy){
        return mDB.query(tabName, columns, whereStr, whereArgs, groupBy, having, orderBy);
    }

    public void close(){
        if (mDBHelper != null) mDBHelper.close();
    }

    public long insertRec(String tabName, ContentValues cv){
        return mDB.insert(tabName, null, cv);
    }

    public int updateRec(String tabName, ContentValues cv, String whereStr, String[] whereArgs){
        return mDB.update(tabName, cv, whereStr, whereArgs);
    }

    public int deleteRec(String tabName, String whereStr, String[] whereArgs){
        return mDB.delete(tabName, whereStr, whereArgs);
    }

    class DBHelper extends SQLiteOpenHelper {

//        public String databasePath = "";
//        private Context ctx;

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
//            databasePath = context.getDatabasePath(name).getAbsolutePath();
//            if (DB_PATH != null) DB_PATH = databasePath;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table "+EVENTS_TABLE+" ("
                    +EVENT_ID+" integer primary key autoincrement,"
                    +EVENT_DATE+" default current_timestamp,"
                    +EVENT_NAME+" text" + ");");

            db.execSQL("create table "+PURCHASES_TABLE+" ("
                    +PURCHASE_ID+" integer primary key autoincrement,"
                    +EVENT_ID+" integer,"
                    +PURCHASE_NAME+" text,"
                    +PURCHASE_VALUE+" text,"
                    +PURCHASE_DIR+" text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }



}
