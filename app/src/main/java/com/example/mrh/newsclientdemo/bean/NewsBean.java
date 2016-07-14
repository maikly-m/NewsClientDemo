package com.example.mrh.newsclientdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/7/9 0009.
 */
public class NewsBean {

    public List<Cateory> listName = new ArrayList<>();

    public class Cateory {
        public List<Ads> ads = new ArrayList<>();
        public String alias;
        public String boardid;
        public String cid;
        public String digest;
        public String docid;
        public String id;
        public String ename;
        public int hasAD;
        public boolean hasCover;
        public int hasHead;
        public boolean hasIcon;
        public int hasImg;
        public List<Imgextra> imgextra = new ArrayList<>();
        public String imgsrc;
        public String lmodify;
        public int order;
        public String photosetID;
        public String postid;
        public int priority;
        public String ptime;
        public int replyCount;
        public String skipID;
        public String skipType;
        public String source;
        public String template;
        public String title;
        public String tname;
        public int votecount;
        public String ltitle;
        public String specialID;
        public String subtitle;
        public String url;
        public String url_3w;
        public String TAG;
        public String TAGS;
        public int imgType;

        public class Ads {
            public String imgsrc;
            public String subtitle;
            public String tag;
            public String title;
            public String url;
        }

        public class Imgextra {
            public String imgsrc;
        }

    }
}
