package com.example.myapplicationinst;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {

    Activity activity;
    AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    void loadingDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog() {
        alertDialog.dismiss();
    }

    Boolean isShow() {
        return alertDialog.isShowing();
    }
}
