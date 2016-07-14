package com.example.mrh.newsclientdemo.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.MainTab;
import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private Toolbar tb_main;
    private FrameLayout fl_main;
    private FragmentTabHost mTabHost;
    private ServiceConnection mConnection;
    private DrawerLayout dl_main;
    private LinearLayout ll_main_right;
    private ActionBarDrawerToggle mDrawerToggle;
    long[] mHits = new long[2]; //返回键双击事件记录数组
    private RelativeLayout rl_mian_left;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        setContentView(R.layout.activity_main);
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected (ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected (ComponentName name) {

            }
        };
        Utils.bindToService(this, mConnection);
        initView();
    }

    private void initView () {
        dl_main = (DrawerLayout) findViewById(R.id.dl_main);
        ll_main_right = (LinearLayout) findViewById(R.id.ll_main_right);
        rl_mian_left = (RelativeLayout) findViewById(R.id.rl_mian_left);
        tb_main = (Toolbar) findViewById(R.id.tb_main);
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

//        tb_main.setNavigationIcon(R.drawable.ic_navigation_icon);
        tb_main.setTitle(R.string.title);
        tb_main.setTitleTextAppearance(this, R.style.menuStyle);
        setSupportActionBar(tb_main);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_main);
        mTabHost.getTabWidget().setShowDividers(0);
        MainTab[] mainTabs = MainTab.values();
        for (int i = 0; i < mainTabs.length; i++){
            View view = View.inflate(this, R.layout.main_tab_indicator, null);
            ImageView iv_indicator = (ImageView) view.findViewById(R.id.iv_indicator);
            TextView tv_indicator = (TextView) view.findViewById(R.id.tv_indicator);
            iv_indicator.setImageResource(mainTabs[i].getImageID());
            tv_indicator.setText(mainTabs[i].getTitle());
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mainTabs[i].getTitle()).setIndicator
                    (view);
            mTabHost.addTab(tabSpec, mainTabs[i].getFragment(), null);
        }
        //设置第一页为当前页
        mTabHost.setCurrentTab(0);
        setListener();

    }


    public void setListener () {
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged (String tabId) {
                if (tabId.equals(MainTab.NEWS)){

                } else if (tabId.equals(MainTab.TWEET)){

                } else if (tabId.equals(MainTab.DISCOVER)){

                } else if (tabId.equals(MainTab.MY)){

                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, dl_main, tb_main, R.string.open, R
                .string.close) {
            @Override
            public void onDrawerOpened (View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed (View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        dl_main.addDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
        case R.id.search:

            break;
        case R.id.popuWindow:
            showPopuWindow();
            break;
        default:
            break;
        }
        return true;
    }

    private void showPopuWindow () {
        View popu = View.inflate(MainActivity.this, R.layout.more_popuwindow, null);
        LinearLayout ll_popuwindow_first =  (LinearLayout) popu.findViewById(R.id
                .ll_popuwindow_first);
        LinearLayout ll_popuwindow_seconde = (LinearLayout) popu.findViewById(R.id
                .ll_popuwindow_seconde);
        mPopupWindow = new PopupWindow(popu, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        //设置popuwindow动画
        mPopupWindow.setAnimationStyle(R.style.CustomPopuAnimation);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popu_window_bg));
        //设定popuwindow的位置
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int measuredWidth = popu.getMeasuredWidth();
        int offX = widthPixels - measuredWidth;
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(tb_main, offX, 0);

        ll_popuwindow_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mPopupWindow.dismiss();
                Toast.makeText(MainActivity.this, R.string.nothing, Toast.LENGTH_SHORT).show();
            }
        });
        ll_popuwindow_seconde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mPopupWindow.dismiss();
                Toast.makeText(MainActivity.this, R.string.nothing, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        Utils.unBindToService(this, mConnection);
        ActivityManager.getActivityManager().removeActivity(this);
    }

    //双击返回键退出程序
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //每点击一次 实现左移一格数据
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //给数组的最后赋当前时钟值
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            //当0出的值大于当前时间-1500时  证明在1500秒内点击了2次
            if (mHits[0] > SystemClock.uptimeMillis() - 1500){
                ActivityManager.getActivityManager().exitApp();
            } else{
                Toast.makeText(this, R.string.twiceclick_exit, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
