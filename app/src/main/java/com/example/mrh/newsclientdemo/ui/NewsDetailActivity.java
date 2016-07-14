package com.example.mrh.newsclientdemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.bean.NewsItemBean;
import com.example.mrh.newsclientdemo.constant.Constant;
import com.example.mrh.newsclientdemo.http.HttpClient;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MR.H on 2016/7/11 0011.
 */
public class NewsDetailActivity extends AppCompatActivity {
    private TextView tv_detail_title;
    private WebView wv_detail;
    private String mURL;
    private NewsItemBean mNewsItemBean;
    private Toolbar mTb_news_detail;
    private View mMView;
    private Button mBnt_loading_error;
    private RelativeLayout rl_news_detail;
    private ViewGroup.LayoutParams mLp;
    private ProgressBar pb_news_detail;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        ActivityManager.getActivityManager().addActivity(this);
        String news_url = getIntent().getStringExtra("NEWS_URL");
        mURL = Constant.NEWS_LIST + news_url + File.separator + "full.html";
        initView();
        initData();
    }

    private void initData () {
        String result = Utils.getData(mURL);
        if (result != null){
            updateView(result);
        } else{
            AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
            httpClient.get(mURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure (int statusCode, Header[] headers, String responseString,
                                       Throwable throwable) {
                    Toast.makeText(NewsDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT)
                            .show();
                    loadErrorView();
                }

                @Override
                public void onSuccess (int statusCode, Header[] headers, String responseString) {
                    String result = responseString;
                    Utils.setData(result, mURL);
                    updateView(result);
                }
            });
        }
    }
    //连接失败显示页面
    private void loadErrorView () {
        if (mMView == null){
            mMView = View.inflate(this, R.layout.loading_error, null);
            mBnt_loading_error = (Button) mMView.findViewById(R.id.bnt_loading_error);
            mLp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.addContentView(mMView, mLp);
        }
        rl_news_detail.setVisibility(View.GONE);
        mMView.setVisibility(View.VISIBLE);
        pb_news_detail.setVisibility(View.VISIBLE);
        mBnt_loading_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //重新加载
                initData();
                rl_news_detail.setVisibility(View.VISIBLE);
                mMView.setVisibility(View.GONE);
            }
        });
    }
    //更新数据
    private void updateView (String result) {
        pb_news_detail.setVisibility(View.GONE);
        String newsName = Utils.resetNewsName(result);
        mNewsItemBean = (NewsItemBean) Utils.Parse(newsName, NewsItemBean.class);
        tv_detail_title.setText(mNewsItemBean.news.title);
        String html = "<html>" + mNewsItemBean.news.body + "</html>";
        wv_detail.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    private void initView () {
        mTb_news_detail = (Toolbar) findViewById(R.id.tb_news_detail);
        mTb_news_detail.setTitle("");
        setSupportActionBar(mTb_news_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rl_news_detail = (RelativeLayout) findViewById(R.id.rl_news_detail);
        pb_news_detail = (ProgressBar) findViewById(R.id.pb_news_detail);
        tv_detail_title = (TextView) findViewById(R.id.tv_detail_title);
        wv_detail = (WebView) findViewById(R.id.wv_detail);
        wv_detail.getSettings().setJavaScriptEnabled(true);
        wv_detail.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
        case R.id.news_share:

            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }
}
