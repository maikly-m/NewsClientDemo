package com.example.mrh.newsclientdemo.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.bean.NewsBean;
import com.example.mrh.newsclientdemo.bean.TitleBean;
import com.example.mrh.newsclientdemo.constant.Constant;
import com.example.mrh.newsclientdemo.http.HttpClient;
import com.example.mrh.newsclientdemo.ui.MainActivity;
import com.example.mrh.newsclientdemo.ui.NewsDetailActivity;
import com.example.mrh.newsclientdemo.ui.view.DownPullListView;
import com.example.mrh.newsclientdemo.ui.HeaderPictureActivity;
import com.example.mrh.newsclientdemo.utils.ThreadManager;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MR.H on 2016/7/7 0007.
 */
public class NewsBaseFragment extends Fragment {

    public static final int INTNET_TAG_MADS = 0;
    public static final int INTNET_TAG_MIMAGE = 1;
    public static final String NEWS_URL = "NEWS_URL";
    private View mView;
    private ViewPager vp_news;
    private DownPullListView lv_news;
    private CirclePageIndicator cp_news;
    private String title;
    private TitleBean.TList mList;
    private int mPage;
    private String mURL;
    private NewsBean mNewsBean;
    private List<NewsBean.Cateory.Ads> mAds;
    private FrameLayout mRootView;
    private FrameLayout.LayoutParams mLp;
    private View mTextview;
    private TextView tv_news;
    private NewsPagerAdapter mNewsPagerAdapter;
    private String mImage;
    private NewsAdapter mNewsAdapter;
    private View headerView;
    private SwipeRefreshLayout sr_news_content;
    public static final int VIEWHODLER01_TAG = R.layout.news_content_listview01; //第一种viewholder类型
    public static final int VIEWHODLER02_TAG = R.layout.news_content_listview02; //第二种viewholder类型
    public static final int VIEWHODLER03_TAG = R.layout.news_content_listview03; //第三种viewholder类型
    private NewsBean mCacheNewsBean;
    private NewsBean mDownloadNewsBean;
    private int mMorepage = 1; //开始加载更多数据的索引
    private List<NewsBean.Cateory> mListName;
    private boolean first = true;
    private boolean needMoreData = true;
    private boolean clickMoreData = false;
    private TextView tv_more_content;
    private DownPullListView.OnFooterRefresh mOnFooterRefresh;
    private View mMView;
    private Button mBnt_loading_error;

    public NewsBaseFragment () {
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //防止多次创建
        if (mView == null){
            initData();
        }
    }
    //初始化数据
    private void initData () {
        mPage = 0;
        if (mList.tname.equals("头条")){
            mURL = Constant.NEWS_EACH_HEADLINE + mList.tid + File.separator + 20 * mPage + "-20" +
                    ".html";
        } else{
            mURL = Constant.NEWS_EACH + mList.tid + File.separator + 20 * mPage + "-20.html";
        }
        String result = Utils.getData(mURL);
        if (result != null){
            setView(result);
        } else{
            AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
            httpClient.get(mURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure (int statusCode, Header[] headers, String
                        responseString, Throwable throwable) {
                    Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    loadErrorView();
                }

                @Override
                public void onSuccess (int statusCode, Header[] headers, String
                        responseString) {
                    String result = responseString;
                    Utils.setData(result, mURL);
                    setView(result);
                }
            });
        }
    }
    //连接失败显示页面
    private void loadErrorView () {
        if (mMView == null){
            mMView = View.inflate(getContext(), R.layout.loading_error, null);
            mBnt_loading_error = (Button) mMView.findViewById(R.id.bnt_loading_error);
            mRootView.addView(mMView);
        }
        mTextview.setVisibility(View.GONE);
        mMView.setVisibility(View.VISIBLE);
        mBnt_loading_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //重新加载
                initData();
                mTextview.setVisibility(View.VISIBLE);
                mMView.setVisibility(View.GONE);
            }
        });
    }
    //获取数据
    public void getMoreData (final int page) {
        mPage = page;
        if (mList.tname.equals("头条")){
            mURL = Constant.NEWS_EACH_HEADLINE + mList.tid + File.separator + 20 * mPage + "-20" +
                    ".html";
        } else{
            mURL = Constant.NEWS_EACH + mList.tid + File.separator + 20 * mPage + "-20.html";
        }
        String result = Utils.getData(mURL);
        if (result != null){
            String mResult = Utils.resetListName(result);
            mCacheNewsBean = (NewsBean) Utils.Parse(mResult, NewsBean.class);
            List<NewsBean.Cateory> listName = mCacheNewsBean.listName;
            if (page == 0){
                Toast.makeText(getContext(), R.string.no_more, Toast.LENGTH_SHORT).show();
            } else{
                //加载数据的索引增加
                mMorepage ++;
                for (int i = 0; i < listName.size(); i++){
                    mNewsBean.listName.add(listName.get(i));
                }
            }
            //更新数据
           updateData();
        } else{
            AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
            httpClient.get(mURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure (int statusCode, Header[] headers, String
                        responseString, Throwable throwable) {
                    Toast.makeText(getContext(), R.string.no_more, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess (int statusCode, Header[] headers, String
                        responseString) {

                    String result = responseString;
                    Utils.setData(result, mURL);
                    String mResult = Utils.resetListName(result);
                    mDownloadNewsBean = (NewsBean) Utils.Parse(mResult, NewsBean.class);
                    List<NewsBean.Cateory> listName = mDownloadNewsBean.listName;
                    if (page == 0){
                        //更换第一批数据
                        for (int i = 0; i < listName.size(); i++){
                            mNewsBean.listName.remove(0);
                        }
                        for (int i = 0; i < listName.size(); i++){
                            mNewsBean.listName.add(i, listName.get(i));
                        }
                    } else{
                        //加载数据的索引增加
                        mMorepage ++;
                        //追加数据
                        for (int i = 0; i < listName.size(); i++){
                            mNewsBean.listName.add(listName.get(i));
                        }
                        if (listName.size() == 0){
                            Toast.makeText(getContext(), R.string.no_more, Toast.LENGTH_SHORT).show();
                        }
                    }
                    //更新数据
                    updateData();
                }
            });
        }
    }
    //更新数据
    private void updateData () {
        //通知适配器更新
        mNewsAdapter.notifyDataSetChanged();
        mNewsPagerAdapter.notifyDataSetChanged();
        //开关更新
        lv_news.shutDownRefresh(true);
        sr_news_content.setRefreshing(false);
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        ActionBar supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (!supportActionBar.isShowing()){
            supportActionBar.show();
        }
        //没有数据时显示
        //防止多次创建
        if (mRootView == null){
            mRootView = new FrameLayout(getContext());
            mLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                    .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            mTextview = View.inflate(getContext(), R.layout.loading_view, null);
            mRootView.addView(mTextview);
            return mRootView;
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null){
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void setView (String result) {
        String mResult = Utils.resetListName(result);
        mNewsBean = (NewsBean) Utils.Parse(mResult, NewsBean.class);

        mAds = mNewsBean.listName.get(0).ads;
        mImage = mNewsBean.listName.get(0).imgsrc;

        mView = View.inflate(getContext(), R.layout.news_content, null);
        headerView = View.inflate(getContext(), R.layout.news_content_header, null);
        lv_news = (DownPullListView) mView.findViewById(R.id.lv_news);
        tv_more_content = (TextView) mView.findViewById(R.id.tv_more_content);
        sr_news_content = (SwipeRefreshLayout) mView.findViewById(R.id.sr_news_content);
        vp_news = (ViewPager) headerView.findViewById(R.id.vp_news);
        tv_news = (TextView) headerView.findViewById(R.id.tv_news);
        cp_news = (CirclePageIndicator) headerView.findViewById(R.id.cp_news);
        cp_news.setSnap(true);
        mNewsPagerAdapter = new NewsPagerAdapter();
        vp_news.setAdapter(mNewsPagerAdapter);
        mNewsAdapter = new NewsAdapter();
        lv_news.addHeaderView(headerView);

        lv_news.setAdapter(mNewsAdapter);
        //单张图片不显示
        if (mAds != null && mAds.size() > 1){
            cp_news.setViewPager(vp_news);
            //定时切换图片
            ThreadManager.getThreadPool().startThread(new Runnable() {
                @Override
                public void run () {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(0, 2500);
                }
            });
        }
        mRootView.addView(mView, mLp);
        mTextview.setVisibility(View.GONE);

        //点击事件总汇
        onClickEvent();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                int currentItem = vp_news.getCurrentItem();
                currentItem++;
                if (currentItem % mAds.size() == 0){
                    currentItem = 0;
                }
                vp_news.setCurrentItem(currentItem);
                mHandler.sendEmptyMessageDelayed(0, 2500);
            }
        }
    };

    private void onClickEvent () {
        //头部图片点击事件
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

            }
        });
        //下拉刷新
        sr_news_content.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
//                ThreadManager.getThreadPool().startThread(new Runnable() {
//                    @Override
//                    public void run () {
//                        SystemClock.sleep(3000);
//                        //貌似没有问题，可以在子线程更新UI
//                        sr_news_content.setRefreshing(false);
//                    }
//                });
                //获取更多数据
                getMoreData(0);
            }
        });
        //上拉加载

        //是否加载更多
        mOnFooterRefresh = new DownPullListView.OnFooterRefresh() {
            @Override
            public void loadingMore () {
                //是否点击了加载更多按钮
                if (!clickMoreData){
                    //是否加载更多
                    if (mMorepage % 2 == 0){
                        needMoreData = false;
                        //显示是否加载更多
                        tv_more_content.setVisibility(View.VISIBLE);
                        //关闭刷新progressbar
                        lv_news.shutDownRefresh(true);
                    }
                }

                if (needMoreData){
                    //获取更多数据
                    getMoreData(mMorepage);
                    //显示是否加载更多按钮重置（未点击）
                    clickMoreData = false;
                }
            }
        };
        lv_news.setOnFooterRefresh(mOnFooterRefresh);
        //listview条目点击事件
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                String url = mListName.get(position).docid;
                if (url != null){
                    intent.putExtra(NEWS_URL, url);
                    startActivity(intent);
                }
            }
        });
        cp_news.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected (int position) {
                if (mAds != null){
                    tv_news.setText(mAds.get(position).title);
                }
            }

            @Override
            public void onPageScrollStateChanged (int state) {

            }
        });
        //是否加载更多
        tv_more_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //显示是否加载更多按钮 已经点击
                clickMoreData = true;
                //需要加载更多数据
                needMoreData = true;
                //显示是否加载更多按钮 设置不可见
                tv_more_content.setVisibility(View.GONE);
                //progressbar设置可见
                mOnFooterRefresh.loadingMore();
            }
        });
    }

    //接收信息对象
    public void sendInfo (TitleBean.TList list) {
        mList = list;
    }

    class NewsPagerAdapter extends PagerAdapter {

        public NewsPagerAdapter () {
            super();
        }

        @Override
        public int getCount () {
            if (mAds != null){
                return mAds.size();
            } else if (mImage != null){
                return 1;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject (View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem (ViewGroup container, final int position) {

            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //viewpager条目点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (mAds != null){
                        Intent intent = new Intent(getContext(), HeaderPictureActivity.class);
                        intent.putExtra("INTNET_TAG", INTNET_TAG_MADS);
                        String url = mAds.get(position).url;
                        String[] split = url.split("\\|", 2);
                        intent.putExtra("url", split[1]);
                        startActivity(intent);
                    } else if (mImage != null){
                        Intent intent = new Intent(getContext(), HeaderPictureActivity.class);
                        intent.putExtra("INTNET_TAG", INTNET_TAG_MIMAGE);
                        intent.putExtra("imgsrc", mNewsBean.listName.get(0).imgsrc);
                        intent.putExtra("digest", mNewsBean.listName.get(0).digest);
                        startActivity(intent);
                    }
                }
            });
            container.addView(imageView);
            if (mAds != null){
                Picasso.with(getContext()).load(mAds.get(position).imgsrc).into(imageView);
                if (first){
                    tv_news.setText(mAds.get(position).title);
                    first = false;
                }
                return imageView;
            } else if (mImage != null){
                Picasso.with(getContext()).load(mImage).into(imageView);
                tv_news.setText("");
                return imageView;
            }
            return null;
        }

        @Override
        public void destroyItem (ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter {

        public NewsAdapter () {
            super();
            mListName = mNewsBean.listName;
        }

        @Override
        public int getCount () {
            return mListName.size() - 1;
        }

        @Override
        public Object getItem (int position) {
            return mListName.get(position + 1);
        }

        @Override
        public long getItemId (int position) {
            return position + 1;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            int mPosition = position + 1;
            ViewHolder01 viewHolder01;
            ViewHolder02 viewHolder02;
            ViewHolder03 viewHolder03;
            int size = 0;
            int votecount = mListName.get(mPosition).votecount;
            float num = (float) votecount / 10000;
            String count = Utils.formatFloat(num, "0.0");

            String mTAG = mListName.get(mPosition).TAG;
            if (mNewsBean.listName.get(mPosition).imgextra != null){
                size = mNewsBean.listName.get(mPosition).imgextra.size();
            }
            if (size >= 2){
                if (convertView == null || convertView.getTag(VIEWHODLER02_TAG) == null){
                    convertView = View.inflate(getContext(), R.layout.news_content_listview02,
                            null);
                    viewHolder02 = ViewHolder02.getViewHolder02(convertView);
                } else{
                    viewHolder02 = (ViewHolder02) convertView.getTag(VIEWHODLER02_TAG);
                }
                viewHolder02.tv_listview02_title.setText(mListName.get(mPosition).title);

                Picasso.with(getContext()).load(mListName.get(mPosition).imgsrc).into(viewHolder02
                        .iv_listview02_01);
                Picasso.with(getContext()).load(mListName.get(mPosition).imgextra.get(0).imgsrc)
                        .into(viewHolder02.iv_listview02_02);
                Picasso.with(getContext()).load(mListName.get(mPosition).imgextra.get(1).imgsrc)
                        .into(viewHolder02.iv_listview02_03);
                viewHolder02.tv_listview02_source.setText(mListName.get(mPosition).source);

                if (votecount < 10000){
                    viewHolder02.tv_listview02_votecount.setText(votecount + "跟帖");
                } else{
                    viewHolder02.tv_listview02_votecount.setText(count + "万跟帖");
                }

                if (mTAG != null){
                    viewHolder02.tv_listview02_tag.setText(mTAG);
                } else{
                    viewHolder02.tv_listview02_tag.setText("");
                }

            } else{

                if (mListName.get(mPosition).imgType == 1){
                    if (convertView == null || convertView.getTag(VIEWHODLER03_TAG) == null){
                        convertView = View.inflate(getContext(), R.layout.news_content_listview03,
                                null);
                        viewHolder03 = ViewHolder03.getViewHolder03(convertView);
                    } else{
                        viewHolder03 = (ViewHolder03) convertView.getTag(VIEWHODLER03_TAG);
                    }
                    Picasso.with(getContext()).load(mListName.get(mPosition).imgsrc).into
                            (viewHolder03.iv_listview03);
                    viewHolder03.tv_listview03_title.setText(mListName.get(mPosition).title);
                    viewHolder03.tv_listview03_source.setText(mListName.get(mPosition).source);

                    if (votecount < 10000){
                        viewHolder03.tv_listview03_votecount.setText(votecount + "跟帖");
                    } else{
                        viewHolder03.tv_listview03_votecount.setText(count + "万跟帖");
                    }

                    if (mTAG != null){
                        viewHolder03.tv_listview03_tag.setText(mTAG);
                    } else{
                        viewHolder03.tv_listview03_tag.setText("");
                    }
                } else{
                    if (convertView == null || convertView.getTag(VIEWHODLER01_TAG) == null){
                        convertView = View.inflate(getContext(), R.layout.news_content_listview01,
                                null);
                        viewHolder01 = ViewHolder01.getViewHolder(convertView);
                    } else{
                        viewHolder01 = (ViewHolder01) convertView.getTag(VIEWHODLER01_TAG);
                    }
                    Picasso.with(getContext()).load(mListName.get(mPosition).imgsrc).into
                            (viewHolder01.iv_listview);
                    viewHolder01.tv_listview_title.setText(mListName.get(mPosition).title);
                    viewHolder01.tv_listview_source.setText(mListName.get(mPosition).source);

                    if (votecount < 10000){
                        viewHolder01.tv_listview_votecount.setText(votecount + "跟帖");
                    } else{
                        viewHolder01.tv_listview_votecount.setText(count + "万跟帖");
                    }

                    if (mTAG != null){
                        viewHolder01.tv_listview01_tag.setText(mTAG);
                    } else{
                        viewHolder01.tv_listview01_tag.setText("");
                    }
                }
            }
            return convertView;
        }
    }

    static class ViewHolder03 {
        public View rootView;
        public TextView tv_listview03_title;
        public ImageView iv_listview03;
        public TextView tv_listview03_source;
        public TextView tv_listview03_tag;
        public TextView tv_listview03_votecount;
        public LinearLayout ll_listview03;

        public ViewHolder03 (View rootView) {
            this.rootView = rootView;
            this.tv_listview03_title = (TextView) rootView.findViewById(R.id
                    .tv_listview03_title);
            this.iv_listview03 = (ImageView) rootView.findViewById(R.id.iv_listview03);
            this.tv_listview03_source = (TextView) rootView.findViewById(R.id
                    .tv_listview03_source);
            this.tv_listview03_tag = (TextView) rootView.findViewById(R.id.tv_listview03_tag);
            this.tv_listview03_votecount = (TextView) rootView.findViewById(R.id
                    .tv_listview03_votecount);
            this.ll_listview03 = (LinearLayout) rootView.findViewById(R.id.ll_listview03);
            rootView.setTag(VIEWHODLER03_TAG, this);
        }

        public static ViewHolder03 getViewHolder03 (View rootView) {
            return new ViewHolder03(rootView);
        }

    }

    static class ViewHolder02 {
        public View rootView;
        public TextView tv_listview02_title;
        public ImageView iv_listview02_01;
        public ImageView iv_listview02_02;
        public ImageView iv_listview02_03;
        public TextView tv_listview02_source;
        public TextView tv_listview02_votecount;
        public LinearLayout ll_listview02;
        public TextView tv_listview02_tag;

        private ViewHolder02 (View rootView) {
            this.rootView = rootView;
            this.tv_listview02_title = (TextView) rootView.findViewById(R.id
                    .tv_listview02_title);
            this.iv_listview02_01 = (ImageView) rootView.findViewById(R.id.iv_listview02_01);
            this.iv_listview02_02 = (ImageView) rootView.findViewById(R.id.iv_listview02_02);
            this.iv_listview02_03 = (ImageView) rootView.findViewById(R.id.iv_listview02_03);
            this.tv_listview02_source = (TextView) rootView.findViewById(R.id
                    .tv_listview02_source);
            this.tv_listview02_votecount = (TextView) rootView.findViewById(R.id
                    .tv_listview02_votecount);
            this.ll_listview02 = (LinearLayout) rootView.findViewById(R.id.ll_listview02);
            this.tv_listview02_tag = (TextView) rootView.findViewById(R.id.tv_listview02_tag);
            rootView.setTag(VIEWHODLER02_TAG, this);
        }

        public static ViewHolder02 getViewHolder02 (View rootView) {
            return new ViewHolder02(rootView);
        }

    }

    static class ViewHolder01 {
        public View rootView;
        public ImageView iv_listview;
        public TextView tv_listview_title;
        public TextView tv_listview_source;
        public TextView tv_listview_votecount;
        public LinearLayout ll_listview;
        public TextView tv_listview01_tag;

        private ViewHolder01 (View rootView) {
            this.rootView = rootView;
            this.iv_listview = (ImageView) rootView.findViewById(R.id.iv_listview01);
            this.tv_listview_title = (TextView) rootView.findViewById(R.id.tv_listview01_title);
            this.tv_listview_source = (TextView) rootView.findViewById(R.id.tv_listview01_source);
            this.tv_listview_votecount = (TextView) rootView.findViewById(R.id
                    .tv_listview01_votecount);
            this.ll_listview = (LinearLayout) rootView.findViewById(R.id.ll_listview01);
            this.tv_listview01_tag = (TextView) rootView.findViewById(R.id.tv_listview01_tag);
            rootView.setTag(VIEWHODLER01_TAG, this);
        }

        public static ViewHolder01 getViewHolder (View rootView) {
            return new ViewHolder01(rootView);
        }
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getTitle () {
        return title;
    }
}
