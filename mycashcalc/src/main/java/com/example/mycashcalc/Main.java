package com.example.mycashcalc;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main extends AppCompatActivity {

    static final int DB_VERSION = 1;

    static final String LOG_TAG = "myLogs";
    static String CURR_ID = null;

    private static final int CM_DELETE_ID = 1;
    private static final int CM_ADD_ID = 2;
    private static final int CM_EDIT_ID = 3;

    // имена атрибутов для Map
    static final String ATTRIBUTE_NAME_TEXT = "purchase_value";
    static final String ATTRIBUTE_NAME_DIR = "purchase_dir";
    static final String ATTRIBUTE_NAME_POS = "position";
    static final String ATTRIBUTE_NAME_ID = "event_id";
    static final String ATTRIBUTE_NAME_DATE = "event_date";


    private final int REQUEST_CODE_CREATE = 1;
    private final int REQUEST_CODE_EDIT = 2;
    private final int REQUEST_CODE_LIST = 3;
//    final int DIALOG_RESULT = 1;

    private SimpleAdapter sAdapter;
    private final ArrayList<Map<String, Object>> data_arr = new ArrayList<>();
    private Map<String, Object> m;

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

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_DIR };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvText, R.id.tvDir };

        // создаем адаптер
        sAdapter = new SimpleAdapter(this, data_arr, R.layout.item, from, to);

        // определяем список и присваиваем ему адаптер
        ListView lvSimple = findViewById(R.id.lvSimple);
        lvSimple.setAdapter(sAdapter);
        registerForContextMenu(lvSimple);
    }

    private void add_string(String value, String dir) {
        // создаем новый Map
        m = new HashMap<>();
        m.put(ATTRIBUTE_NAME_TEXT, value);
        m.put(ATTRIBUTE_NAME_DIR, dir);
        // добавляем его в коллекцию
        data_arr.add(m);
        // уведомляем, что данные изменились
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_ADD_ID, 0, getResources().getString(R.string.add));
        menu.add(0, CM_EDIT_ID, 1, getResources().getString(R.string.edit));
        menu.add(0, CM_DELETE_ID, 2, getResources().getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo acmi;
        switch (item.getItemId()) {
            case CM_ADD_ID:
                create_item();
                break;
//                return true;
            case CM_EDIT_ID:
                // получаем инфу о пункте списка
                acmi = (AdapterContextMenuInfo) item.getMenuInfo();
                m = new HashMap<>();
                m = data_arr.get(acmi.position);
                Intent intent = new Intent(this, Activity_two.class);
                intent.putExtra(ATTRIBUTE_NAME_DIR, m.get(ATTRIBUTE_NAME_DIR).toString());
                intent.putExtra(ATTRIBUTE_NAME_TEXT, m.get(ATTRIBUTE_NAME_TEXT).toString());
                intent.putExtra(ATTRIBUTE_NAME_POS, acmi.position);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
                // уведомляем, что данные изменились
                break;
            case CM_DELETE_ID:
                // получаем инфу о пункте списка
                acmi = (AdapterContextMenuInfo) item.getMenuInfo();
                // удаляем Map из коллекции, используя позицию пункта в списке
                data_arr.remove(acmi.position);
                // уведомляем, что данные изменились
                sAdapter.notifyDataSetChanged();
                break;
//                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void create_item() {
        Intent intent = new Intent(this, Activity_two.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CREATE:
                    add_string(data.getStringExtra(ATTRIBUTE_NAME_TEXT), data.getStringExtra(ATTRIBUTE_NAME_DIR));
                    break;
                case REQUEST_CODE_EDIT:
                    edit_item(data.getStringExtra(ATTRIBUTE_NAME_POS), data.getStringExtra(ATTRIBUTE_NAME_TEXT), data.getStringExtra(ATTRIBUTE_NAME_DIR));
                    break;
                case REQUEST_CODE_LIST:
                    Log.d(LOG_TAG, "REQUEST_CODE_LIST");
                    if (deleteAllItems()) {
                        if (loadList(data.getStringExtra("event_id"))) sAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
//        else
//            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
    }

    private void edit_item(String position, String value, String dir) {
        m = new HashMap<>();
        m.put(ATTRIBUTE_NAME_TEXT, value);
        m.put(ATTRIBUTE_NAME_DIR, dir);
        data_arr.set(Integer.parseInt(position), m);
        sAdapter.notifyDataSetChanged();
    }

    public void onAddClick(View view) {
        create_item();
    }

    public void onCalcClick(View view) {
        m = new HashMap<>();
        String tmp_str = null;
        float f;
        float f_prev = 0;
        for (int i = 0; i < data_arr.size(); i++) {
            m = data_arr.get(i);

            tmp_str = m.get(ATTRIBUTE_NAME_TEXT).toString();
            tmp_str = tmp_str.replaceAll("[^A-Za-zА-Яа-я0-9]", "");
            StringTokenizer st = new StringTokenizer(tmp_str, "*/+-");
            while(st.hasMoreTokens()) {
                String key = st.nextToken();
                String val = st.nextToken();
                //System.out.println(key + " : " + val);
            }


            f = Float.parseFloat(m.get(ATTRIBUTE_NAME_TEXT).toString());
            if (getResources().getString(R.string.to_me).equals(m.get(ATTRIBUTE_NAME_DIR).toString())) {
                f = f * 1;
            } else
                if (getResources().getString(R.string.to_other).equals(m.get(ATTRIBUTE_NAME_DIR).toString())) {
                f = f *  (-1);
            } else
                if (getResources().getString(R.string.in_half_to_me).equals(m.get(ATTRIBUTE_NAME_DIR).toString())) {
                f = f / 2;
            } else
                if (getResources().getString(R.string.in_half_im).equals(m.get(ATTRIBUTE_NAME_DIR).toString())) {
                f = f / (-2);
            }
             f_prev = f_prev + f;
        }
        DialogFragment dlg1 = new Dialog1();
        Bundle args = new Bundle(1);
        args.putFloat("result", f_prev);
        dlg1.setArguments(args);
        dlg1.show(getFragmentManager(), "dlg1");
        saveList();
//        Toast.makeText(this, Float.toString(f), Toast.LENGTH_SHORT).show();
    }


    public void onCloseClick(View view) {
        finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAdd:
                create_item();
                break;
//            case R.id.mnDell:
//                break;
            case R.id.mnDellAll:
                if (deleteAllItems()) sAdapter.notifyDataSetChanged();
                break;
            case R.id.mnExit:
                finish();
                break;
            case R.id.mnLoad:
                showListEvents();
                break;
            case R.id.mnSave:
                saveList();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        Log.d(LOG_TAG, "deleteAllItems");
        return true;
    }

    private Integer checkList(String id){
        int i = -1;
        if (id != null) {
                DBhelper dbh = new DBhelper(this, "mycashcalc", null, DB_VERSION);
                SQLiteDatabase db = dbh.getWritableDatabase();
                String selection = "event_id = ?";
                String[] selectionArgs = new String[]{CURR_ID};
                Cursor c = db.query("events", null, selection, selectionArgs, null, null, null, null);
                if (c != null) {
                    i = c.getCount();
                    c.close();
                }
                dbh.close();
        }
        return i;
    }

    private void updateList() {
        if (CURR_ID != null){
//        if (Integer.parseInt(CURR_ID) > 0){
            m = new HashMap<>();
            ContentValues cv = new ContentValues();
            DBhelper dbh = new DBhelper(this,"mycashcalc", null, DB_VERSION);
            SQLiteDatabase db = dbh.getWritableDatabase();
            db.beginTransaction();
            cv.put("event_name", "sometext");
            String where = "event_id = ?";
            String[] whereArgs = new String[] {CURR_ID};
            db.update("events", cv, where, whereArgs);
            db.delete("purchases", where, whereArgs);
            for (int i = 0; i < data_arr.size(); i++) {
                m = data_arr.get(i);
                cv.clear();
                cv.put("event_id", CURR_ID);
                cv.put("purchase_value", Float.parseFloat(m.get(ATTRIBUTE_NAME_TEXT).toString()));
                cv.put("purchase_dir", m.get(ATTRIBUTE_NAME_DIR).toString());
                db.insert("purchases", null, cv);
            }
            db.setTransactionSuccessful();
//            currID = ll;
            db.endTransaction();
            dbh.close();
        }

    }

    private void insertList() {
//        Long ll;
        m = new HashMap<>();
        ContentValues cv = new ContentValues();
        DBhelper dbh = new DBhelper(this,"mycashcalc", null, DB_VERSION);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.beginTransaction();
//        cv.put("event_id", "");
        cv.put("event_name", "sometext");
        Long id = db.insert("events", null, cv);
        if (id != -1){
            for (int i = 0; i < data_arr.size(); i++) {
//                m = new HashMap<String, Object>();
                m = data_arr.get(i);
                cv.clear();
                cv.put("event_id", CURR_ID);
                cv.put("purchase_value", Float.parseFloat(m.get(ATTRIBUTE_NAME_TEXT).toString()));
                cv.put("purchase_dir", m.get(ATTRIBUTE_NAME_DIR).toString());
                db.insert("purchases", null, cv);
            }
            db.setTransactionSuccessful();
//            currID = ll;
            CURR_ID = String.valueOf(id);
        }
        db.endTransaction();
        dbh.close();
    }

    private boolean loadList(String id) {
//        boolean res = false;
        try {
            DBhelper dbh = new DBhelper(this,"mycashcalc", null, DB_VERSION);
            SQLiteDatabase db = dbh.getWritableDatabase();
            Cursor c;
//            String sqlQuery = "select purchase_value, purchase_dir from purchases where event_id = ?";
            Log.d(LOG_TAG, "return id = " + id);
            String selection = "event_id = ?";
            String[] selectionArgs = new String[] { id };
            c = db.query("purchases", null, selection, selectionArgs, null, null, null);
            if (c != null){
                if (c.moveToFirst()){
                    do {
                        m = new HashMap<>();
                        m.put(ATTRIBUTE_NAME_TEXT, c.getString(c.getColumnIndex(ATTRIBUTE_NAME_TEXT)));
                        m.put(ATTRIBUTE_NAME_DIR, c.getString(c.getColumnIndex(ATTRIBUTE_NAME_DIR)));
                        data_arr.add(m);
//                        Log.d(LOG_TAG, m.get("purchase_value").toString() + m.get("purchase_dir").toString());
                    } while (c.moveToNext());
//                sAdapter.notifyDataSetChanged();
                }
                c.close();
            }
            dbh.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
        CURR_ID = id;
        return true;
    }

    private void saveList() {
            if (checkList(CURR_ID) > 0){
                updateList();
            }
            else{
                insertList();
            }
        }

}



