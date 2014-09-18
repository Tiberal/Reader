package com.polcop.reader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by oleg on 12.09.14.
 */
public class LoadingDialog extends DialogFragment{

    private ProgressDialog dialog;

    public static LoadingDialog getDialod(){
        LoadingDialog loadingDialod = new LoadingDialog();
        return loadingDialod;
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
