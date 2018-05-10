package com.example.mycashcalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class Activity_two extends AppCompatActivity {

    private EditText et;
    private String position;
//    final String ATTRIBUTE_NAME_TEXT = "purchase_value";
//    final String ATTRIBUTE_NAME_DIR = "purchase_dir";
//    final String ATTRIBUTE_NAME_POS = "position";
//    String value;
//    String dir;
//    Button btnSave;
//    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        et = findViewById(R.id.etValue);
//        Button btnSave = findViewById(R.id.btnSave);
//        Button btnClose = findViewById(R.id.btnClose);
        RadioButton rbMe = findViewById(R.id.rbMe);
        RadioButton rbOther = findViewById(R.id.rbOther);
        RadioButton rbhalf_to_me = findViewById(R.id.rbHalf_to_me);
        RadioButton rbhalf_im = findViewById(R.id.rbHalf_im);

        Intent intent = getIntent();
        String dir = intent.getStringExtra(Main.ATTRIBUTE_NAME_DIR);
        String value = intent.getStringExtra(Main.ATTRIBUTE_NAME_TEXT);
        position = intent.getStringExtra(Main.ATTRIBUTE_NAME_POS);
        if (getResources().getString(R.string.to_me).equals(dir)) {
            rbMe.setChecked(true);
        } else
            if (getResources().getString(R.string.to_other).equals(dir)) {
            rbOther.setChecked(true);
        } else
            if (getResources().getString(R.string.in_half_to_me).equals(dir)) {
            rbhalf_to_me.setChecked(true);
        } else
            if (getResources().getString(R.string.in_half_im).equals(dir)) {
            rbhalf_im.setChecked(true);
        }

        et.setText(value);
//        showInputMethod();
//        et.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


//    protected void showInputMethod() {
//        InputMethodManager imm = (InputMethodManager) (this).getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.showSoftInput((et), 0);
//        }
//    }

    public void onSave(View view) {
        String dir = "";
        RadioButton rbMe = findViewById(R.id.rbMe);
        RadioButton rbOther = findViewById(R.id.rbOther);
        RadioButton rbhalf_to_me = findViewById(R.id.rbHalf_to_me);
        RadioButton rbhalf_im = findViewById(R.id.rbHalf_im);
        if (rbMe.isChecked()) {
            dir = getResources().getString(R.string.to_me);
        } else
            if (rbOther.isChecked()) {
                dir = getResources().getString(R.string.to_other);
            } else
                if (rbhalf_to_me.isChecked()) {
                    dir = getResources().getString(R.string.in_half_to_me);
                } else
                    if (rbhalf_im.isChecked()) {
                        dir = getResources().getString(R.string.in_half_im);
                    }
        Intent intent = new Intent();
        intent.putExtra(Main.ATTRIBUTE_NAME_TEXT, et.getText().toString());
        intent.putExtra(Main.ATTRIBUTE_NAME_DIR, dir);
        intent.putExtra(Main.ATTRIBUTE_NAME_POS, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onClose(View view) {
        finish();
    }
}
