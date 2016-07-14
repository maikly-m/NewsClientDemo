package com.example.mrh.newsclientdemo;

import com.example.mrh.newsclientdemo.ui.fragment.DiscoverFragment;
import com.example.mrh.newsclientdemo.ui.fragment.MyFragment;
import com.example.mrh.newsclientdemo.ui.fragment.NewsFragment;
import com.example.mrh.newsclientdemo.ui.fragment.TweetFragment;

/**
 * 导航枚举类
 * Created by MR.H on 2016/7/7 0007.
 */
public enum  MainTab {
    NEWS(1, "新闻", R.drawable.ic_nav_news, NewsFragment.class),
    TWEET(2, "视频", R.drawable.ic_nav_tweet, TweetFragment.class),
    DISCOVER(3, "发现", R.drawable.ic_nav_discover, DiscoverFragment.class),
    MY(4, "我", R.drawable.ic_nav_my, MyFragment.class);

    private int id;
    private String title;
    private int imageID;
    private Class<?> fragment;

    MainTab (int id, String title, int imageID, Class<?> fragment) {
        this.id = id;
        this.title = title;
        this.imageID = imageID;
        this.fragment = fragment;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public int getImageID () {
        return imageID;
    }

    public void setImageID (int imageID) {
        this.imageID = imageID;
    }

    public Class<?> getFragment () {
        return fragment;
    }

    public void setFragment (Class<?> fragment) {
        this.fragment = fragment;
    }
}
