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

import com.polcop.reader.UI.PageObserver;
import com.polcop.reader.adapters.TagExpandableListAdapter;
import com.polcop.reader.fragments.Feed;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ExpandableListView drawerExpandableListView;
    private Feed feed;
    private TextView tvCurrentPage;
    private PageObserver pageObserver;
    private String[] quotations;
    private String[] bashTAgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bashTAgs = getResources().getStringArray(R.array.bash_link);
        quotations = getResources().getStringArray(R.array.quotations);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(PageInfo.getInstance().getTagInfos()!=null){
            anotherContent(PageInfo.getInstance().getTagInfos());
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
                if(pageObserver==null){
                    pageObserver=new PageObserver();
                }
                pageObserver.switchPage();
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

    public void showCurrentPageInActionBar(String s){
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

    //обработчик нажатий на Dropdown Navigation
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        setCurrentPageInActionBarClickable(true);
        switch (position){
            case 0:
                Toast.makeText(this,"ItHappens",Toast.LENGTH_SHORT).show();
                PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_LINK);
                showCurrentPageInActionBar("Свежие");
                switchContent(Constants.IT_HAPPENS_LINK, Constants.IT_HAPPENS_LOADER);
                break;
            case 1:
                Toast.makeText(this,"Задолба!ли",Toast.LENGTH_SHORT).show();
                PageInfo.getInstance().setCurrentPage(Constants.ZADOLBALI_LINK);
                showCurrentPageInActionBar("Свежие");
                switchContent(Constants.ZADOLBALI_LINK, Constants.ZADOLBALI_LOADER);
                break;
            case 2:
                Toast.makeText(this,"Bash",Toast.LENGTH_SHORT).show();PageInfo.getInstance().setCurrentPage(Constants.ZADOLBALI_LINK);
                PageInfo.getInstance().setCurrentPage(Constants.BASH_LINK);
                showCurrentPageInActionBar("Новые");
                switchContent(Constants.BASH_LINK, Constants.BASH_LOADER);
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
                    itHappensAndZadolbaliClick(groupPosition,childPosition, Constants.IT_HAPPENS_LOADER);
                    break;
                case Constants.ZADOLBALI_LOADER:
                    itHappensAndZadolbaliClick(groupPosition,childPosition, Constants.ZADOLBALI_LOADER);
                    break;
                case Constants.BASH_LOADER:
                    bashClick(childPosition);
                    break;
                case Constants.KILL_ME_PLZ_LOADER:
                    //killMePlzClick(groupPosition,childPosition);
                    break;
            }
            return true;
        }

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return false;
        }
    }

    private void itHappensAndZadolbaliClick(int groupPosition, int childPosition, int loaderId){
        if(childPosition==0){
            setCurrentPageInActionBarClickable(true);
        }else{
            setCurrentPageInActionBarClickable(false);
        }
        if (groupPosition==0){
            switch (childPosition){
                case 0:
                    //todo if before switch
                    if (loaderId==Constants.IT_HAPPENS_LOADER)
                        PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_LINK);
                    else
                        PageInfo.getInstance().setCurrentPage(Constants.ZADOLBALI_LINK);
                    showCurrentPageInActionBar("Свежие");
                    switchContent(PageInfo.getInstance().getCurrentPage(), loaderId);
                    break;
                case 1:
                    if (loaderId==Constants.IT_HAPPENS_LOADER)
                        PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_BEST);
                    else
                        PageInfo.getInstance().setCurrentPage(Constants.ZADOLBALI_BEST);
                    showCurrentPageInActionBar("Лучшие");
                    switchContent(PageInfo.getInstance().getCurrentPage(), loaderId);
                    break;
                case 2:
                    if (loaderId==Constants.IT_HAPPENS_LOADER)
                        PageInfo.getInstance().setCurrentPage(Constants.IT_HAPPENS_RANDOM);
                    else
                        PageInfo.getInstance().setCurrentPage(Constants.ZADOLBALI_RANDOM);
                    showCurrentPageInActionBar("Случайные");
                    switchContent(PageInfo.getInstance().getCurrentPage(), loaderId);
                    break;
            }
        }else if (groupPosition==1){
            ArrayList<TagInfo> tagInfos = PageInfo.getInstance().getTagInfos();
            PageInfo.getInstance().setCurrentPage(tagInfos.get(childPosition).getTagURL());
            showCurrentPageInActionBar(PageInfo.getInstance().getTagInfos().get(childPosition).getTagName());
            setCurrentPageInActionBarClickable(true);
            switchContent(tagInfos.get(childPosition).getTagURL(), loaderId);
        }
    }

    private  void  bashClick(int childPosition){
        if(childPosition==0||childPosition==3||childPosition==6){
            setCurrentPageInActionBarClickable(true);
        }else{
            setCurrentPageInActionBarClickable(false);
        }
        PageInfo.getInstance().setCurrentPage(PageInfo.getInstance().getTagInfos().get(childPosition).getTagURL());
        showCurrentPageInActionBar(PageInfo.getInstance().getTagInfos().get(childPosition).getTagName());
        switchContent(PageInfo.getInstance().getTagInfos().get(childPosition).getTagURL(),Constants.BASH_LOADER);

    }

    private void switchContent(String link, int id){
       drawerLayout.closeDrawers();
       PageInfo.getInstance().clearStoryInfo();
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
