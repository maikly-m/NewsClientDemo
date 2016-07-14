package com.example.mrh.newsclientdemo.ui;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.R;

/**
 * Created by MR.H on 2016/7/12 0012.
 */
public class SearchResultActivity extends AppCompatActivity {

    private String mSearchContent;
    private WebView wv_network;
    private WebSettings mSettings;
    private ProgressBar pb_network_search;
    private LinearLayout ll_network_search;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mSearchContent = getIntent().getStringExtra(SearchManager.QUERY);
        setContentView(R.layout.network_search);
        initView();

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }

    private void initView () {
        wv_network = (WebView) findViewById(R.id.wv_network);
        pb_network_search = (ProgressBar) findViewById(R.id.pb_network_search);
        ll_network_search = (LinearLayout) findViewById(R.id.ll_network_search);

        pb_network_search.setMax(100);
        mSettings = wv_network.getSettings();
        //启用支持javascript
        mSettings.setJavaScriptEnabled(true);
        //使用缓存
        mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缩放时显示缩放图标
        mSettings.setBuiltInZoomControls(true);
        //支持缩放
        mSettings.setSupportZoom(true);
        //任意比例缩放
        mSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        mSettings.setLoadWithOverviewMode(true);

        //启用百度搜索
//        wv_network.loadUrl("http://baidu.com/s?wd=" + mSearchContent);
        //神马搜索
        wv_network.loadUrl("https://so.m.sm.cn/s?q=" + mSearchContent);

        wv_network.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                view.loadUrl(url);
                //不打开第三方浏览器，自己处理
                return true;
            }
        });
        //网页进度
        wv_network.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged (WebView view, int newProgress) {
                if (newProgress == 100){
                    // 网页加载完成
                    pb_network_search.setVisibility(View.INVISIBLE);
                    pb_network_search.setProgress(100);
                } else{
                    // 加载中
                    pb_network_search.setVisibility(View.VISIBLE);
                    pb_network_search.setProgress(newProgress);
                }

            }
        });
    }

    //改写返回键
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (wv_network.canGoBack()){
                wv_network.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
