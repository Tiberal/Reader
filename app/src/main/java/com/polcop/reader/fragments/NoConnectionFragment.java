package com.polcop.reader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.polcop.reader.R;
import com.polcop.reader.Utils;

/**
 * Created by oleg on 16.09.14.
 */
public class NoConnectionFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_connection_layout,null);
        Button tryAgain = (Button)view.findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(Utils.isOnline()){
            Feed feed = new Feed();
            feed.setArguments(getArguments());
            (getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,feed,null).commit();

        }else
            Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_LONG).show();
    }
}