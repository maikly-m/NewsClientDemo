package com.example.mrh.newsclientdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/7/11 0011.
 */
public class NewsItemBean {
    public News news;
    public class News {
        public String body;
        public String digest;
        public String dkeys;
        public String docid;
        public String ec;
        public boolean hasNext;

        public boolean picnews;
        public String ptime;
        public String replyBoard;
        public int replyCount;
        public String shareLink;
        public String source;
        public String source_url;
        public String template;
        public int threadAgainst;
        public int threadVote;
        public String tid;
        public String title;
        List<Topiclist_news> topiclist_newses = new ArrayList<>();

        public class Topiclist_news {
            public String alias;
            public String cid;
            public String ename;
            public boolean hasCover;
            public String subnum;
            public String tname;
        }
    }
}
