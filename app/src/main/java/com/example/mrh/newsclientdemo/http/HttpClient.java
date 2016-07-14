package com.example.mrh.newsclientdemo.http;


import com.example.mrh.newsclientdemo.bean.NewsBean;
import com.example.mrh.newsclientdemo.bean.TitleBean;
import com.example.mrh.newsclientdemo.constant.Constant;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MR.H on 2016/7/8 0008.
 */
public class HttpClient {
    private static AsyncHttpClient client;
    private HttpClient () {

    }
    public synchronized static AsyncHttpClient getAsyncHttpClient(){
        return client;
    }
    public static void setAsyncHttpClient(AsyncHttpClient c){
        client = c;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
//        client.addHeader("Host", Constant.HOST);
        client.addHeader("Connection", "Keep-Alive");
    }

}
