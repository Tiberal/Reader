package com.polcop.reader.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.polcop.reader.R;

/**
 * Created by oleg on 12.09.14.
 */
public class LoadingDialog extends DialogFragment{

    private ProgressDialog dialog;
    private static LoadingDialog instance;

    public static LoadingDialog getDialod(){
        if(instance==null)
            instance = new LoadingDialog();
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle(R.string.loading_data);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.loading_wait));
        setCancelable(false);
        return dialog;
    }

}
