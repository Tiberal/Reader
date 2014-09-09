package com.polcop.reader;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ExpandableListView drawerExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerExpandableListView = (ExpandableListView)findViewById(R.id.left_drawer);
        setActionBarOptions();
        drawerLayout.setDrawerListener(drawerToggle);
        drawerExpandableListView.setOnChildClickListener(new DrawerItemClickListener());
        drawerExpandableListView.setOnGroupClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,
                R.string.drawer_open,R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            PageInfo.initInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Feed())
                    .commit();
        }
    }

    private void setActionBarOptions(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c8c8c8")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void anotherContent(ArrayList<TagInfo> tagInfos){
        ArrayList<String> temp = new ArrayList<String>();
        for (int i=0;i<tagInfos.size();i++){
            temp.add(tagInfos.get(i).getTagName());
        }
        TagExpandableListAdapter adapter = new TagExpandableListAdapter(tagInfos,this);
        drawerLayout.closeDrawers();
        drawerExpandableListView.setAdapter(adapter);
        drawerExpandableListView.expandGroup(0);
    }

    private class DrawerItemClickListener implements ExpandableListView.OnChildClickListener,
            ExpandableListView.OnGroupClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            //PageInfo.getInstance().resetPager();
            //PageInfo.getInstance().setMaxPageMark(true);
            int loaderId = 103;//getLoaderId();
            switch (loaderId){
                case Constants.IT_HAPPENS_LOADER:
                    //todo click listener
                    break;
                case Constants.ZADOLBALI_LOADER:
                    //zadolbaliClick(groupPosition,childPosition);
                    break;
                case Constants.BASH_LOADER:
                    //bashClick(childPosition);
                    break;
                case Constants.KILL_ME_PLZ_LOADER:
                    //killMePlzClick(groupPosition,childPosition);
                    break;
            }
            return false;
        }

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return false;
        }
    }

}
