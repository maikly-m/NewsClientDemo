package com.example.mrh.newsclientdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/7/11 0011.
 */
public class ViewPagerImageBean {

    public String autoid;
    public String boardid;
    public String clientadurl;
    public String commenturl;
    public String cover;
    public String createdate;
    public String creator;
    public String datatime;
    public String desc;
    public String imgsum;
    public List<Photo> photos = new ArrayList<>();

    public String postid;
    public String reporter;
    public String scover;
    public String series;
    public String setname;
    public String settag;
    public String source;
    public String tcover;
    public String url;
    public List<?> relatedids;

    public class Photo {

        public String cimgurl;
        public String imgtitle;
        public String imgurl;
        public String newsurl;
        public String note;
        public String photohtml;
        public String photoid;
        public String simgurl;
        public String squareimgurl;
        public String timgurl;
    }
}
