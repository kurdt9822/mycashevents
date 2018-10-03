package com.example.mycashcalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class Activity_two extends AppCompatActivity {

    private EditText et_val;
    private EditText et_name;
    private int position;
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

        et_val = (EditText) findViewById(R.id.etValue);
        et_name = (EditText) findViewById(R.id.etName);
//        Button btnSave = findViewById(R.id.btnSave);
//        Button btnClose = findViewById(R.id.btnClose);
        RadioButton rbMe = (RadioButton) findViewById(R.id.rbMe);
        RadioButton rbOther = (RadioButton) findViewById(R.id.rbOther);
        RadioButton rbhalf_to_me = (RadioButton) findViewById(R.id.rbHalf_to_me);
        RadioButton rbhalf_im = (RadioButton) findViewById(R.id.rbHalf_im);

        Intent intent = getIntent();
        String dir = intent.getStringExtra(Main.ATTRIBUTE_NAME_DIR);
        String name = intent.getStringExtra(Main.ATTRIBUTE_NAME);
        String value = intent.getStringExtra(Main.ATTRIBUTE_NAME_VAL);
        boolean swd = intent.getBooleanExtra(Main.SWITCH_DIRECTION, true);
        position = intent.getIntExtra(Main.ATTRIBUTE_NAME_POS, -1);

//        if (intent.getBooleanExtra(Main.ATTRIBUTE_ME, false)){
//            rbhalf_im.setVisibility(View.INVISIBLE);
//            rbOther.setVisibility(View.INVISIBLE);
//        }

        if (swd) rbhalf_to_me.setChecked(true);
        else rbhalf_im.setChecked(true);

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

        et_val.setText(value);
        et_name.setText(name);

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
        RadioButton rbMe = (RadioButton) findViewById(R.id.rbMe);
        RadioButton rbOther = (RadioButton) findViewById(R.id.rbOther);
        RadioButton rbhalf_to_me = (RadioButton) findViewById(R.id.rbHalf_to_me);
        RadioButton rbhalf_im = (RadioButton) findViewById(R.id.rbHalf_im);
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
        intent.putExtra(Main.ATTRIBUTE_NAME_VAL, et_val.getText().toString());
        intent.putExtra(Main.ATTRIBUTE_NAME, et_name.getText().toString());
        intent.putExtra(Main.ATTRIBUTE_NAME_DIR, dir);
        intent.putExtra(Main.ATTRIBUTE_NAME_POS, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onClose(View view) {
        finish();
    }
}
