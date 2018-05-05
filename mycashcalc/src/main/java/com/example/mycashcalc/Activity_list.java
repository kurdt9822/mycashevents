package com.example.mycashcalc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_list extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    Map<String, Object> m;
//    final String ATTRIBUTE_NAME_ID = "event_id";
//    final String ATTRIBUTE_NAME_DATE = "event_date";
    ListView lvMain;
//    ArrayList<String> data;
    SimpleAdapter sAdapter;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//    final String ATTRIBUTE_NAME_ID = "id";
//    final String ATTRIBUTE_NAME_TEXT = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lvMain = (ListView) findViewById(R.id.lvMain);
        // устанавливаем режим выбора пунктов списка
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getList();

        String[] from = { Main.ATTRIBUTE_NAME_ID, Main.ATTRIBUTE_NAME_DATE };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tvDir, R.id.tvText };

        // создаем адаптер
        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, data);
        lvMain.setAdapter(sAdapter);
        registerForContextMenu(lvMain);
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lvMain.getCheckedItemPosition() != -1) {
                    Intent intent = new Intent();
                    m = new HashMap<String, Object>();
                    m = data.get(lvMain.getCheckedItemPosition());
                    Log.d(LOG_TAG, "putExtra id = " + m.get(Main.ATTRIBUTE_NAME_ID).toString());
                    intent.putExtra(Main.ATTRIBUTE_NAME_ID, m.get(Main.ATTRIBUTE_NAME_ID).toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_one, false);
        menu.setGroupVisible(R.id.group_two, true);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnDellAllEvent:
                if (delete_all_events()) {
                    data.clear();
                    sAdapter.notifyDataSetChanged();
                    Main.CURR_ID = null;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean delete_all_events() {
        boolean res = false;
        DBhelper dbh = new DBhelper(this,"mycashcalc", null, 1);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.beginTransaction();
        db.delete("purchases", null, null);
        db.delete("events", null, null);
        db.setTransactionSuccessful();
        db.endTransaction();
        dbh.close();
        res = true;
        return res;
    }

    private void getList() {
        //ArrayList<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        DBhelper dbh = new DBhelper(this,"mycashcalc", null, 1);
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c;
//        String sqlQuery = "select event_id, event_date from events";
        c = db.query("events", null, null, null, null, null, null);
        if (c != null){
            if (c.moveToFirst()){
                do {
                    m = new HashMap<String, Object>();
//                    m.clear();
                    m.put(Main.ATTRIBUTE_NAME_ID, c.getString(c.getColumnIndex(Main.ATTRIBUTE_NAME_ID)));
                    m.put(Main.ATTRIBUTE_NAME_DATE, c.getString(c.getColumnIndex(Main.ATTRIBUTE_NAME_DATE)));
                    // добавляем его в коллекцию
                    data.add(m);
                } while (c.moveToNext());
            }
        }
        c.close();
        dbh.close();
//        return res;
    }

}
