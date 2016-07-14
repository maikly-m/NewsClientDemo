package com.example.mrh.newsclientdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/7/8 0008.
 */
public class TitleBean {
    public List<TList> tList = new ArrayList<>();
    /**
     * alias : The Truth
     * bannerOrder : 105
     * cid : C1348654575297
     * color :
     * ename : zhenhua
     * hasCover : false
     * hasIcon : true
     * headLine : false
     * img : http://img2.cache.netease.com/m/newsapp/banner/zhenhua.png
     * isHot : 0
     * isNew : 0
     * recommend : 0
     * recommendOrder : 0
     * showType : comment
     * special : 0
     * subnum : 超过1000万
     * template : normal1
     * tid : T1370583240249
     * tname : 独家
     * topicid : 00040BGE
     */
    public class TList {
        public String alias;
        public int bannerOrder;
        public String cid;
        public String color;
        public String ename;
        public boolean hasCover;
        public boolean hasIcon;
        public boolean headLine;
        public String img;
        public int isHot;
        public int isNew;
        public String recommend;
        public int recommendOrder;
        public String showType;
        public int special;
        public String subnum;
        public String template;
        public String tid;
        public String tname;
        public String topicid;
    }

}
