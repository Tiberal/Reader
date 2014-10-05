package com.polcop.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.polcop.reader.fragments.NoConnectionFragment;

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
        if(link.equals(Constants.ZADOLBALI_LINK)) return true;
        if(link.equals(Constants.ZADOLBALI_BEST)) return true;
        if(link.equals(Constants.ZADOLBALI_RANDOM)) return true;
        if (link.equals(Constants.KILL_ME_PLZ_LINK)) return true;
        if(link.equals(Constants.KILL_ME_PLZ_RANDOM)) return true;
        if(link.equals(Constants.KILL_ME_PLZ_TOP)) return true;
        return false;
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) PageInfo.getInstance().getContext().getSystemService(PageInfo.getInstance().getContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static int getLoaderId (){
        SharedPreferences preferences = ((MainActivity)PageInfo.getInstance().getContext()).getPreferences(((MainActivity)PageInfo.getInstance().getContext()).MODE_PRIVATE);
        return preferences.getInt(Constants.LOADER_ID,-1);
    }

    public static void setLoaderId(int loaderId){
        SharedPreferences preferences = ((MainActivity)PageInfo.getInstance().getContext()).getPreferences(((MainActivity)PageInfo.getInstance().getContext()).MODE_PRIVATE);
        preferences.edit().putInt(Constants.LOADER_ID, loaderId).commit();
    }

    public static void showNoConnectionFragment(Bundle bundle, Context context){
        Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_LONG).show();
        NoConnectionFragment noConnection = new NoConnectionFragment();
        noConnection.setArguments(bundle);
        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.container, noConnection).commit();
    }

    public static void clearBackStack(){
        FragmentManager manager = ((MainActivity)PageInfo.getInstance().getContext()).getSupportFragmentManager();
        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        }
    }

}
