package com.example.mrh.newsclientdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MR.H on 2016/7/14 0014.
 */
public class TweetBean {

    public List<Vedio> vedios;
    public class Vedio implements Serializable{

        public String cover;
        public String description;
        public int length;
        public String m3u8_url;
        public String mp4_url;
        public int playCount;
        public int playersize;
        public String prompt;
        public String ptime;
        public String replyBoard;
        public int replyCount;
        public String replyid;
        public String sectiontitle;
        public String title;
        public String topicDesc;
        public String topicImg;
        public String topicName;
        public String topicSid;
        public String vid;
        public VedioTopic vedioTopic;

        public class VedioTopic {
            public String alias;
            public String ename;
            public String tid;
            public String tname;
        }
        
        public String videosource;

    }
}

