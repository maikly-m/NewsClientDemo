package com.example.mrh.newsclientdemo;

import android.app.Application;

import com.example.mrh.newsclientdemo.http.HttpClient;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;


/**
 * Created by MR.H on 2016/7/8 0008.
 */
public class MyApplication extends Application{

    public MyApplication () {
        super();
        init();
    }

    private void init () {
        initHttp();
    }
    //初始化网络
    private void initHttp () {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        HttpClient.setAsyncHttpClient(asyncHttpClient);
    }
}
