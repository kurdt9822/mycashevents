package com.example.mycashcalc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_list extends AppCompatActivity {

private Map<String, Object> m;
    private SimpleAdapter sAdapter;
    private final ArrayList<Map<String, Object>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
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
//        Button btnOk = findViewById(R.id.btnOk);
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickItem(lvMain.getCheckedItemPosition());
//            }
//        });
//        Button btnCancel = findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickItem(i);
//                view.setBackgroundColor(Color.RED);
            }
        });
    }

    private void clickItem(int pos){
        if (pos != -1) {
            Intent intent = new Intent();
            m = new HashMap<>();
            m = data.get(pos);
//            Log.d(Main.LOG_TAG, "putExtra id = " + m.get(Main.ATTRIBUTE_NAME_ID).toString());
            intent.putExtra(Main.ATTRIBUTE_NAME_ID, m.get(Main.ATTRIBUTE_NAME_ID).toString());
            setResult(RESULT_OK, intent);
            finish();
        }
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
//        try {
//            DBhelper dbh = new DBhelper(this, Main.DB_NAME, null, Main.DB_VERSION);
//            SQLiteDatabase db = dbh.getWritableDatabase();
//            db.beginTransaction();
//            db.delete(Main.PURCHASES_TABLE, null, null);
//            db.delete(Main.EVENTS_TABLE, null, null);
//            db.setTransactionSuccessful();
//            db.endTransaction();
//            dbh.close();
//        } catch (Exception e) {
//            Log.e(Main.LOG_TAG, e.getMessage());
//            return false;
//        }
        return true;
    }

    private void getList() {
//        DBhelper dbh = new DBhelper(this, Main.DB_NAME, null, Main.DB_VERSION);
//        SQLiteDatabase db = dbh.getWritableDatabase();
//        Cursor c = db.query(Main.EVENTS_TABLE, null, null, null, null, null, null);
//        if (c != null){
//            if (c.moveToFirst()){
//                do {
//                    m = new HashMap<>();
//                    m.put(Main.ATTRIBUTE_NAME_ID, c.getString(c.getColumnIndex(Main.ATTRIBUTE_NAME_ID)));
//                    m.put(Main.ATTRIBUTE_NAME_DATE, c.getString(c.getColumnIndex(Main.ATTRIBUTE_NAME_DATE)));
//                    data.add(m);
//                } while (c.moveToNext());
//            }
//            c.close();
//        }
//        dbh.close();
    }

}
