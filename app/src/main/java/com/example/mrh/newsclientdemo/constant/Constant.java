package com.example.mrh.newsclientdemo.constant;

/**
 * Created by MR.H on 2016/7/8 0008.
 */
public class Constant {
    //网易主机名
    public static final String HOST = "http://c.m.163.com/";
    //新闻导航条
    public static final String NEWS_TITLE =
            HOST + "nc/topicset/android/subscribe/manage/listspecial.html";
    //详细新闻
    public static final String NEWS_EACH_HEADLINE = HOST + "nc/article/headline/";
    public static final String NEWS_EACH = HOST + "nc/article/list/";

    //    http://c.m.163.com/photo/api/related/0001/2188690.json
    //部分新闻viewpager图片
    public static final String NEWS_VIEWPAGER_IMAGE = HOST + "photo/api/set/0001/";


    //部分新闻url
    public static final String NEWS_LIST = HOST + "nc/article/";
    //部分视频
    public static final String NEWS_VEDIO = HOST + "recommend/getChanListNews?channel=T1457068979049";
}
