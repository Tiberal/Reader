package com.polcop.reader;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ExpandableListView drawerExpandableListView;
    private Feed feed;
    private TextView tvCurrentPage;
    private PageSelectionFragment pageSelectionFragment;
    private String[] quotations = {"ItHappens","Задолба!ли","Bash","KillMePlz"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerExpandableListView = (ExpandableListView)findViewById(R.id.left_drawer);
        setActionBarOptions();
        drawerLayout.setDrawerListener(drawerToggle);
        DrawerItemClickListener drawerItemClickListener = new DrawerItemClickListener(this);
        drawerExpandableListView.setOnChildClickListener(drawerItemClickListener);
        drawerExpandableListView.setOnGroupClickListener(drawerItemClickListener);
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
            //первый запуск
            PageInfo.initInstance(this);
            showCurrentPageInActionBar("Свежее");
        }
    }

    private void setActionBarOptions(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c8c8c8")));
        //добавление кастомного элемента
        actionBar.setCustomView(R.layout.action_bar_text_view);
        tvCurrentPage = (TextView)actionBar.getCustomView().findViewById(R.id.action_bar_text_view_tag);
        tvCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"action",Toast.LENGTH_SHORT).show();
                if(pageSelectionFragment==null){
                    pageSelectionFragment = new PageSelectionFragment();
                }
                pageSelectionFragment.show(getSupportFragmentManager(),null);
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        //добавление списка цитатников
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_item,quotations);
        actionBar.setListNavigationCallbacks(adapter, this);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
   }

    private void showCurrentPageInActionBar(String s){
        tvCurrentPage.setText(s);
    }

    private void setCurrentPageInActionBarClickable(boolean b){
        tvCurrentPage.setClickable(b);
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

    //обработчик нажатий на список
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        switch (position){
            case 0:
                Toast.makeText(this,"ItHappens",Toast.LENGTH_SHORT).show();
                PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_LINK);
                //PageInfo.getInstance().setStoryInfos(null);
                //Utils.setLoaderId(this,Constants.IT_HAPPENS_LOADER);
                switchContent(Constants.IT_HAPPENS_LINK, Constants.IT_HAPPENS_LOADER);
                break;
            case 1:
                Toast.makeText(this,"Задолба!ли",Toast.LENGTH_SHORT).show();
                PageInfo.getInstance().setCurrentPage(Constants.ZADOLBALI_LINK);
                //PageInfo.getInstance().setStoryInfos(null);
                //Utils.setLoaderId(this,Constants.ZADOLBALI_LOADER);
                switchContent(Constants.ZADOLBALI_LINK, Constants.ZADOLBALI_LOADER);
                break;
            case 2:
                Toast.makeText(this,"Bash",Toast.LENGTH_SHORT).show();
                //switchContent(Constants.BASH_LINK, Constants.BASH_LOADER);
                break;
            case 3:
                Toast.makeText(this,"KillMePlz",Toast.LENGTH_SHORT).show();
                //switchContent(Constants.KILL_ME_PLZ_LINK, Constants.KILL_ME_PLZ_LOADER);
                break;
        }
        return true;
    }

    //обработчик нажатий NavigationDrawer
    private class DrawerItemClickListener implements ExpandableListView.OnChildClickListener,
            ExpandableListView.OnGroupClickListener {

        private Context context;

        public DrawerItemClickListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            switch (Utils.getLoaderId()){
                case Constants.IT_HAPPENS_LOADER:
                    itHappensClick(groupPosition,childPosition);
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

    private void itHappensClick(int groupPosition, int childPosition){
        if (groupPosition==0){
            switch (childPosition){
                case 0:
                    PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_LINK);
                    showCurrentPageInActionBar("Свежие");
                    setCurrentPageInActionBarClickable(true);
                    switchContent(Constants.IT_HAPPENS_LINK, Constants.IT_HAPPENS_LOADER);
                    break;
                case 1:
                    PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_BEST);
                    showCurrentPageInActionBar("Лучшие");
                    setCurrentPageInActionBarClickable(false);
                    switchContent(Constants.IT_HAPPENS_BEST, Constants.IT_HAPPENS_LOADER);
                    break;
                case 2:
                    PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_RANDOM);
                    showCurrentPageInActionBar("Случайные");
                    setCurrentPageInActionBarClickable(false);
                    switchContent(Constants.IT_HAPPENS_RANDOM, Constants.IT_HAPPENS_LOADER);
                    break;
            }
        }else if (groupPosition==1){
            ArrayList<TagInfo> tagInfos = PageInfo.getInstance().getTagInfos();
            PageInfo.getInstance().setCurrentPage(tagInfos.get(childPosition).getTagURL());
            showCurrentPageInActionBar(PageInfo.getInstance().getTagInfos().get(childPosition).getTagName());
            setCurrentPageInActionBarClickable(true);
            switchContent(tagInfos.get(childPosition).getTagURL(), Constants.IT_HAPPENS_LOADER);
        }
    }

    private void switchContent(String link, int id){
       drawerLayout.closeDrawers();
       if (feed!=null){
            getSupportFragmentManager().beginTransaction().remove(feed);
        }
        Bundle arg = new Bundle();
        arg.putString(Constants.LINK, link);
        arg.putInt(Constants.LOADER_ID,id);
       if(Utils.isOnline()){
            feed = new Feed();
            feed.setArguments(arg);
            Utils.setLoaderId(id);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, feed, Constants.FEED_TAG)
                    .commit();
       }else{
           Utils.showNoConnectionFragment(arg,this);
       }
    }
}
