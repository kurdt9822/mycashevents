package com.example.mycashcalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class Dialog1 extends DialogFragment implements OnClickListener {

//    String result = getResources().getString(R.string.empty);

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Float ff = getArguments().getFloat("result");
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.result)).setPositiveButton(R.string.ok, this)
                .setMessage(String.format("%(.2f", ff));
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                i = R.string.ok;
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }
}
