package com.example.mycashcalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

public class Dialog1 extends DialogFragment  {

    interface CallBack{
        void p_buttonClick(String tag);
        void n_buttonClick(String tag);
    }

    CallBack callback;

    void registerCallback(CallBack cback){
        callback = cback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        String title = Objects.requireNonNull(getArguments().getString("title"));
        String message = Objects.requireNonNull(getArguments().getString("message"));
        String p_button = Objects.requireNonNull(getArguments().getString("p_button"));
        String n_button = Objects.requireNonNull(getArguments().getString("n_button"));
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton(p_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tag = getTag();
                callback.p_buttonClick(tag);
            }
        });
        adb.setNegativeButton(n_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tag = getTag();
                callback.n_buttonClick(tag);
            }
        });
        return adb.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
