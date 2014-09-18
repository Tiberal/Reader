package com.polcop.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by oleg on 16.09.14.
 */
public class Utils {

    public static boolean isMainLink(String link, ArrayList<TagInfo> tagInfos){
        for(TagInfo tagInfo: tagInfos){
            if(link.equals(tagInfo.getTagURL())){
                return true;
            }
        }
        if (link.equals(Constants.IT_HAPPENS_LINK)) return true;
        if(link.equals(Constants.IT_HAPPENS_BEST)) return true;
        if(link.equals(Constants.IT_HAPPENS_RANDOM)) return true;
        return false;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static int getLoaderId (Context context){
        SharedPreferences preferences = ((MainActivity)context).getPreferences(((MainActivity)context).MODE_PRIVATE);
        return preferences.getInt(Constants.LOADER_ID,-1);
    }

    public static void setLoaderId(Context context, int loaderId){
        SharedPreferences preferences = ((MainActivity)context).getPreferences(((MainActivity)context).MODE_PRIVATE);
        preferences.edit().putInt(Constants.LOADER_ID, loaderId).commit();
    }

    public static void  dismissLoadingDialog(Context context){
        LoadingDialog loadingDialod = (LoadingDialog) ((MainActivity) context).getSupportFragmentManager().findFragmentByTag(Constants.LOADING_DIALOG_TAG);
        if(loadingDialod!=null){
            ((MainActivity) context).getSupportFragmentManager().beginTransaction().remove(loadingDialod).commitAllowingStateLoss();
        }
    }

    public static void showNoConnectionFragment(Bundle bundle, Context context){
        Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_LONG).show();
        NoConnectionFragment noConnection = new NoConnectionFragment();
        noConnection.setArguments(bundle);
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, noConnection).commit();
    }

    public static void clearBackStack(Context context){
        FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        }
    }

}
