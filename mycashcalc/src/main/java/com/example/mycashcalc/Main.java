package com.example.mycashcalc;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements MyAdapter.MyCallBack, Dialog1.CallBack {

//    final String ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    static final String LOG_TAG = "myLogs";
//    static final String FILENAME = "myFile";
    static String CURR_ID = null;

    private static final int ED_NAME = 0;
    private static final int ED_DIR = 1;
    private static final int ED_VALUE = 2;
    private static final int ED_ALL = 4;

    private static final int CM_DELETE_ID = 1;
    private static final int CM_ADD_ID = 2;
    private static final int CM_EDIT_ID = 3;

//    static final int DB_VERSION = 1;
//    static final String DB_NAME = "mycashcalc";
//    static final String EVENTS_TABLE = "events";
//    static final String PURCHASES_TABLE = "purchases";
    static final String SWITCH_DIRECTION = "switch_direction";

    // имена атрибутов для Map
    static final String ATTRIBUTE_NAME_VAL = "purchase_value";
    static final String ATTRIBUTE_NAME = "purchase_name";
    static final String ATTRIBUTE_NAME_DIR = "purchase_dir";
    static final String ATTRIBUTE_NAME_POS = "position";
    static final String ATTRIBUTE_NAME_ID = "event_id";
    static final String ATTRIBUTE_NAME_DATE = "event_date";
//    static String DB_PATH = "";
//    static final String ATTRIBUTE_ME = "me_flag";

    private final int REQUEST_CODE_CREATE = 1;
    private final int REQUEST_CODE_EDIT = 2;
    private final int REQUEST_CODE_LIST = 3;


//    private final int F_ME = 0;
//    final int DIALOG_RESULT = 1;

//    private SimpleAdapter sAdapter;
//    private final ArrayList<Map<String, Object>> data_arr = new ArrayList<>();
    private DB db;
    private Cursor cursor;
//    private MyAdapter myAdapter;
    private SimpleCursorAdapter myAdapter;
    private long currentEvent;
//    private final ArrayList<ListItem> data_arr = new ArrayList<>();
//    private Map<String, Object> m;
//    private ListItem item;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // упаковываем данные в понятную для адаптера структуру
//        data_arr = new ArrayList<Map<String, Object>>();
//        for (int i = 1; i < 5; i++) {
//            m = new HashMap<String, Object>();
//            m.put(ATTRIBUTE_NAME_TEXT, "sometext " + i);
//            m.put(ATTRIBUTE_NAME_DIR, "3");
//            data_arr.add(m);
//        }


//        // массив имен атрибутов, из которых будут читаться данные
//        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_DIR };
//        // массив ID View-компонентов, в которые будут вставлять данные
//        int[] to = { R.id.tvText, R.id.tvDir };
//
//        // создаем адаптер
//        sAdapter = new SimpleAdapter(this, data_arr, R.layout.item, from, to);
//
//        // определяем список и присваиваем ему адаптер
//        ListView lvSimple = (ListView) findViewById(R.id.lvSimple);
//        lvSimple.setAdapter(sAdapter);
//        registerForContextMenu(lvSimple);

        db = new DB(this);
        db.open();
        cursor = db.getData(DB.PURCHASES_TABLE, new String[] { "rowid _id", "*" }, DB.EVENT_ID+" = ?", new String[] { "0" }, null, null, null);
        String[] from = new String[] {DB.PURCHASE_DIR, DB.PURCHASE_NAME, DB.PURCHASE_VALUE};
        int[] to = new int[] {R.id.tvDirection, R.id.tvDescr, R.id.tvPrice};

        myAdapter = new SimpleCursorAdapter(this, R.layout.myitem, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

//        myAdapter = new MyAdapter(this, data_arr, this);
        ListView lvSimple = (ListView) findViewById(R.id.lvSimple);
        lvSimple.setAdapter(myAdapter);
//        lvSimple.setBackgroundColor(Color.GRAY);
        registerForContextMenu(lvSimple);
        ContentValues cv = new ContentValues();
        cv.put(DB.EVENT_NAME, "some events");
        currentEvent = db.insertRec(DB.EVENTS_TABLE, cv);
        if (currentEvent == -1) {
            Toast.makeText(this, R.string.error_save_database, Toast.LENGTH_LONG).show();
            onDestroy();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void add_string(String name, String dir, String value) {
        // создаем новый Map
//        m = new HashMap<>(2);
//        m.put(ATTRIBUTE_NAME_TEXT, value);
//        m.put(ATTRIBUTE_NAME_DIR, dir);

//        ListItem item = new ListItem(name, dir, value);
        // добавляем его в коллекцию
//        data_arr.add(item);
        ContentValues cv = new ContentValues();
        db.insertRec(DB.PURCHASES_TABLE, cv);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_ADD_ID, 0, getResources().getString(R.string.add));
        menu.add(0, CM_EDIT_ID, 1, getResources().getString(R.string.edit));
        menu.add(0, CM_DELETE_ID, 2, getResources().getString(R.string.delete));
//        menu.add(0, CM_DELETE_ID, 2, getResources().getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo acmi;
        int _id = -1;
        switch (item.getItemId()) {
            case CM_ADD_ID:
                create_item();
                break;
//                return true;
            case CM_EDIT_ID:
                // получаем инфу о пункте списка
                acmi = (AdapterContextMenuInfo) item.getMenuInfo();
//                ListItem lItem = null;
//                m = new HashMap<>(1);
//                lItem = data_arr.get(acmi.position);
                cursor.moveToPosition(acmi.position);
                _id = cursor.getInt(cursor.getColumnIndex(DB.PURCHASE_ID));
                Intent intent = new Intent(this, Activity_two.class);
                intent.putExtra(DB.PURCHASE_ID, _id);
//                intent.putExtra(ATTRIBUTE_NAME, lItem.name);
//                intent.putExtra(ATTRIBUTE_NAME_DIR, lItem.direction);
//                intent.putExtra(ATTRIBUTE_NAME_VAL, lItem.price);
//                intent.putExtra(ATTRIBUTE_NAME_DIR, m.get(ATTRIBUTE_NAME_DIR).toString());
//                intent.putExtra(ATTRIBUTE_NAME_TEXT, m.get(ATTRIBUTE_NAME_TEXT).toString());
//                intent.putExtra(ATTRIBUTE_NAME_POS, acmi.position);
                intent.putExtra(SWITCH_DIRECTION, false);
//                intent.putExtra(ATTRIBUTE_ME, checkToMeFlag());

                startActivityForResult(intent, REQUEST_CODE_EDIT);
//                m.clear();
                // уведомляем, что данные изменились
                break;
            case CM_DELETE_ID:
                // получаем инфу о пункте списка
                acmi = (AdapterContextMenuInfo) item.getMenuInfo();
                // удаляем Map из коллекции, используя позицию пункта в списке
                cursor.moveToPosition(acmi.position);
                _id = cursor.getInt(cursor.getColumnIndex(DB.PURCHASE_ID));
                db.deleteRec(DB.PURCHASES_TABLE, DB.PURCHASE_ID + "=?", new String[] {String.valueOf(_id)});
//                data_arr.remove(acmi.position);
                // уведомляем, что данные изменились
                myAdapter.notifyDataSetChanged();
                break;
//                return true;
        }
        return super.onContextItemSelected(item);
    }

//    private boolean checkToMeFlag() {
//        Map<String, Object> tmp = new HashMap<>();
//        Map<String, Object> tmp2 = new HashMap<>();
//        tmp.put(ATTRIBUTE_NAME_DIR, getResources().getString(R.string.to_me));
//        tmp2.put(ATTRIBUTE_NAME_DIR, getResources().getString(R.string.in_half_to_me));
//        if (data_arr.contains(tmp) || data_arr.contains(tmp2))
//            return true;
//        else return false;
//    }

    private void create_item() {
        Intent intent = new Intent(this, Activity_two.class);
//        intent.putExtra(ATTRIBUTE_ME, checkToMeFlag());
        Switch sw = (Switch) findViewById(R.id.switch1);
        intent.putExtra(SWITCH_DIRECTION, sw.isChecked());
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CREATE:
                    add_string(data.getStringExtra(ATTRIBUTE_NAME), data.getStringExtra(ATTRIBUTE_NAME_DIR), data.getStringExtra(ATTRIBUTE_NAME_VAL));
                    break;
                case REQUEST_CODE_EDIT:
                    edit_item(data.getIntExtra(ATTRIBUTE_NAME_POS, -1), data.getStringExtra(ATTRIBUTE_NAME), data.getStringExtra(ATTRIBUTE_NAME_DIR), data.getStringExtra(ATTRIBUTE_NAME_VAL), ED_ALL);
                    break;
                case REQUEST_CODE_LIST:
//                    Log.d(LOG_TAG, "REQUEST_CODE_LIST");
                    if (deleteAllItems()) {
                        if (loadList(data.getStringExtra(ATTRIBUTE_NAME_ID))) myAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
//        else
//            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
    }

    private void edit_item(int position, String name, String dir, String value, int editMode) {
        ListItem li;
        switch (editMode) {
            case ED_NAME:
                li = new ListItem(name, data_arr.get(position).direction, data_arr.get(position).price);
                break;
            case ED_DIR:
                li = new ListItem(data_arr.get(position).name, dir, data_arr.get(position).price);
                break;
            case ED_VALUE:
                li = new ListItem(data_arr.get(position).name, data_arr.get(position).direction, value);
                break;
//            case ED_ALL:
//                li = new ListItem(name, dir, value);
//                break;
            default:
                li = new ListItem(name, dir, value);
                break;
        }
        data_arr.set(position, li);
        myAdapter.notifyDataSetChanged();
    }

    public void onAddClick(View view) {
        create_item();
    }

    public void onCalcClick(View view) {

//        m = new HashMap<>(1);
        ListItem li;
        float f;
        float f_prev = 0;
        for (int i = 0; i < data_arr.size(); i++) {
            li = data_arr.get(i);
            f = CalcValue.calcPolishForm(li.price);

//            f = Float.parseFloat(m.get(ATTRIBUTE_NAME_TEXT).toString());
            if (getResources().getString(R.string.to_me).equals(li.direction)) {
//                f = f * 1;
            } else
                if (getResources().getString(R.string.to_other).equals(li.direction)) {
                f = f *  (-1);
            } else
                if (getResources().getString(R.string.in_half_to_me).equals(li.direction)) {
                f = f / 2;
            } else
                if (getResources().getString(R.string.in_half_im).equals(li.direction)) {
                f = f / (-2);
            }
             f_prev = f_prev + f;
        }
//        m.clear();
        saveList();
        DialogFragment dlg1 = new Dialog1();
        ((Dialog1) dlg1).registerCallback(this);
        Bundle args = new Bundle(1);
        args.putString("message", String.valueOf(f_prev));
        args.putString("title", getResources().getString(R.string.result));
        args.putString("p_button", getResources().getString(R.string.close));
        args.putString("n_button", getResources().getString(R.string.upload_to_ftp));
        dlg1.setArguments(args);
        dlg1.show(getFragmentManager(), "result");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_one, true);
        menu.setGroupVisible(R.id.group_two, false);
        return super.onPrepareOptionsMenu(menu);
    }


//    private String getDbName() {
//        DBhelper dbh = new DBhelper(this, DB_NAME, null, DB_VERSION);
//        String str =  dbh.databasePath;
//        dbh.close();
//        return str;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAdd:
                create_item();
                break;
//            case R.id.mnDell:
//                break;
            case R.id.mnDellAll:
                if (deleteAllItems()) myAdapter.notifyDataSetChanged();
                break;
            case R.id.mnCalc:
                onCalcClick(null);
                break;
            case R.id.mnLoad:
                showListEvents();
                break;
            case R.id.mnSave:
                saveList();
                break;
            case R.id.mnUploadToFtp:
                uploadToFTP();
                break;
            case R.id.mnLoadFromFtp:

                DialogFragment dlg1 = new Dialog1();
                ((Dialog1) dlg1).registerCallback(this);
                Bundle args = new Bundle(1);
                args.putString("message", getResources().getString(R.string.dialog_question));
                args.putString("title", getResources().getString(R.string.dialog_question));
                args.putString("p_button", getResources().getString(R.string.ok));
                args.putString("n_button", getResources().getString(R.string.exit));
                dlg1.setArguments(args);
                dlg1.show(getFragmentManager(), "loadfromftp");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadToFTP(){
//        int res = 0;
//        if (DB_PATH.isEmpty()) {
//            DB_PATH = getDbName();
//        }
//        MyAsyncTask mt = new MyAsyncTask(this);
//        mt.execute("1", DB_PATH);
    }

    private void loadFromFTP() {
//        if (DB_PATH.isEmpty()) {
//            DB_PATH = getDbName();
//        }
//        MyAsyncTask mt = new MyAsyncTask(this);
//        mt.execute("2", DB_PATH);
    }

    private void showListEvents() {
        Intent intent = new Intent(this, Activity_list.class);
        startActivityForResult(intent, REQUEST_CODE_LIST);
    }

    private boolean deleteAllItems() {
        try {
            data_arr.clear();
        }
        catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
//        sAdapter.notifyDataSetChanged();
//        Log.d(LOG_TAG, "deleteAllItems");
        return true;
    }

    private Integer checkList(String id){
        int i = -1;
//        if (id != null) {
//                DBhelper dbh = new DBhelper(this, DB_NAME, null, DB_VERSION);
//                SQLiteDatabase db = dbh.getWritableDatabase();
//                String selection = "event_id = ?";
//                String[] selectionArgs = new String[]{CURR_ID};
//                Cursor c = db.query(EVENTS_TABLE, null, selection, selectionArgs, null, null, null, null);
//                if (c != null) {
//                    i = c.getCount();
//                    c.close();
//                }
//                dbh.close();
//        }
        return i;
    }

    private void updateList() {
//        if (CURR_ID != null){
////        if (Integer.parseInt(CURR_ID) > 0){
////            m = new HashMap<>();
//            ContentValues cv = new ContentValues();
//            DBhelper dbh = new DBhelper(this, DB_NAME, null, DB_VERSION);
//            SQLiteDatabase db = dbh.getWritableDatabase();
//            db.beginTransaction();
//            cv.put("event_name", "sometext");
//            String where = "event_id = ?";
//            String[] whereArgs = new String[] {CURR_ID};
//            db.update(EVENTS_TABLE, cv, where, whereArgs);
//            db.delete(PURCHASES_TABLE, where, whereArgs);
//            try {
//                insertListView(db, PURCHASES_TABLE, data_arr, Long.parseLong(CURR_ID));
//            } catch (NumberFormatException e) {
//                Log.e(LOG_TAG, e.getMessage());
//                dbh.close();
//                return;
//            }
//            db.setTransactionSuccessful();
////            currID = ll;
//            db.endTransaction();
//            dbh.close();
//        }
    }

    private void insertList() {
//        ContentValues cv = new ContentValues();
//        DBhelper dbh = new DBhelper(this, DB_NAME, null, DB_VERSION);
//        SQLiteDatabase db = dbh.getWritableDatabase();
//        db.beginTransaction();
//        cv.put("event_name", "sometext");
//        Long id = db.insert(EVENTS_TABLE, null, cv);
//        if (id != -1){
//            insertListView(db, PURCHASES_TABLE, data_arr, id);
//            db.setTransactionSuccessful();
//            CURR_ID = String.valueOf(id);
//        }
//        db.endTransaction();
//        dbh.close();
    }

    private void insertListView(SQLiteDatabase db, String tabName, ArrayList<ListItem> arr, Long id){
        ContentValues cv = new ContentValues();
//        m = new HashMap<>(1);
        ListItem li = null;
        for (int i = 0; i < arr.size(); i++) {
//            m.clear();
            li = arr.get(i);
            cv.clear();
            cv.put(ATTRIBUTE_NAME_ID, id);
            cv.put(ATTRIBUTE_NAME, li.name);
            cv.put(ATTRIBUTE_NAME_VAL, li.price);
            cv.put(ATTRIBUTE_NAME_DIR, li.direction);
            db.insert(tabName, null, cv);
        }
//        m.clear();
    }

    private boolean loadList(String id) {
//        try {
//            DBhelper dbh = new DBhelper(this, DB_NAME, null, DB_VERSION);
//            SQLiteDatabase db = dbh.getWritableDatabase();
//            Cursor c;
//            String selection = "event_id = ?";
//            String[] selectionArgs = new String[] { id };
//            c = db.query(PURCHASES_TABLE, null, selection, selectionArgs, null, null, null);
//            if (c != null){
//                if (c.moveToFirst()){
//                    do {
//                        ListItem li = new ListItem(c.getString(c.getColumnIndex(ATTRIBUTE_NAME)),
//                                c.getString(c.getColumnIndex(ATTRIBUTE_NAME_DIR)),
//                                c.getString(c.getColumnIndex(ATTRIBUTE_NAME_VAL)));
//                        data_arr.add(li);
//                    } while (c.moveToNext());
//                }
//                c.close();
//            }
//            dbh.close();
//        } catch (Exception e) {
//            Log.e(LOG_TAG, e.getMessage());
//            return false;
//        }
//        CURR_ID = id;
        return true;
    }

    private void saveList() {
        if (data_arr.size() > 0) {
            if (checkList(CURR_ID) > 0) updateList();
            else insertList();
        }
    }

    @Override
    public void itemButtonCallingBack(int itemID, int position) {
        int i = 0;
        switch (itemID) {
            case R.id.m_in_half_im:
                i = R.string.in_half_im;
                break;
            case R.id.m_in_half_to_me:
                i = R.string.in_half_to_me;
                break;
            case R.id.m_to_me:
                i = R.string.to_me;
                break;
            case R.id.m_to_other:
                i = R.string.to_other;
                break;
        }
        ((TextView) findViewById(R.id.tvDirection)).setText(i);
        edit_item(position, "", getResources().getString(i), "", ED_DIR);
    }

    @Override
    public void longTapCallingBack(int position) { }

    @Override
    public void p_buttonClick(String tag) {
        switch (tag) {
            case "result": break;
            case "loadfromftp":
                loadFromFTP();
                break;
            default: break;
        }
    }

    @Override
    public void n_buttonClick(String tag) {
        switch (tag) {
            case "result":
                // upload_to_ftp
                uploadToFTP();
                break;
            case "loadfromftp":
                break;
            default: break;
        }
    }
}



