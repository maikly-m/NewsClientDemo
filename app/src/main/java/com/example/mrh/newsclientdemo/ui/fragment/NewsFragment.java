package com.example.mrh.newsclientdemo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.bean.TitleBean;
import com.example.mrh.newsclientdemo.constant.Constant;
import com.example.mrh.newsclientdemo.http.HttpClient;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MR.H on 2016/7/7 0007.
 */
public class NewsFragment extends BaseFrameFragment implements View.OnClickListener {

    private View mView;
    private TabPageIndicator mIndicator;
    private ViewPager mvp_baseframefragment;
    private ImageButton ib_back;
    private ImageButton ib_forward;
    List<NewsBaseFragment> fragments = new ArrayList<>();
    private List<TitleBean.TList> mTList;
    public LoadState State = LoadState.Failure;
    private TitleBean mNewsTitleBean;
    private View mTextview;
    private FragmentManager mChildFragmentManager;
    private FrameLayout mRootView;
    private FrameLayout.LayoutParams mLp;
    private MyPagerAdapter mMyPagerAdapter;
    private View mMView;
    private Button mBnt_loading_error;

    public enum LoadState {
        LoadSuccess, Failure;
    }

    public NewsFragment () {

    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        //别在构造方法中调用,会报错
        mChildFragmentManager = getChildFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
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

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //防止多次创建
        if (mView == null){
            newsLoading();
        }
    }

    //数据解析后,mView添加到mRootView中
    private void setView () {
        initData();
        mView = View.inflate(getContext(), R.layout.baseframefragment, null);
        mIndicator = (TabPageIndicator) mView.findViewById(R.id.indicator);
        mvp_baseframefragment = (ViewPager) mView.findViewById(R.id.vp_baseframefragment);
        mMyPagerAdapter = new MyPagerAdapter(mChildFragmentManager);
        mvp_baseframefragment.setAdapter(mMyPagerAdapter);
        mIndicator.setViewPager(mvp_baseframefragment);
        initClick(mView);
        mRootView.addView(mView, mLp);
        mTextview.setVisibility(View.GONE);
        mIndicator.setCurrentItem(0);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected (int position) {

            }

            @Override
            public void onPageScrollStateChanged (int state) {

            }
        });
    }

    private void initData () {
        mTList = mNewsTitleBean.tList;
        for (int i = 0; i < mTList.size(); i++){
            NewsBaseFragment newsBaseFragment = new NewsBaseFragment();
            newsBaseFragment.setTitle(mTList.get(i).tname);
            newsBaseFragment.sendInfo(mTList.get(i));
            fragments.add(newsBaseFragment);
        }
    }

    private void initClick (View mView) {
        ib_back = (ImageButton) mView.findViewById(R.id.ib_back);
        ib_forward = (ImageButton) mView.findViewById(R.id.ib_forward);

        ib_back.setOnClickListener(this);
        ib_forward.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        int currentItem = mvp_baseframefragment.getCurrentItem();
        switch (v.getId()){
        case R.id.ib_back:
            if (currentItem > 0){
                currentItem--;
            } else{
                currentItem = 0;
            }
            mvp_baseframefragment.setCurrentItem(currentItem);
            break;
        case R.id.ib_forward:
            if (currentItem < fragments.size()){
                currentItem++;
            } else{
                currentItem = fragments.size();
            }
            mvp_baseframefragment.setCurrentItem(currentItem);
            break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle (int position) {
            return fragments.get(position).getTitle();
        }

        @Override
        public Fragment getItem (int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount () {
            return fragments.size();
        }

    }

    //加载数据
    public void newsLoading () {
        String result = Utils.getData(Constant.NEWS_TITLE);
        if (result != null){
            mNewsTitleBean = (TitleBean) Utils.Parse(result, TitleBean.class);
            setView();
        } else{
            AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
            httpClient.get(Constant.NEWS_TITLE, new TextHttpResponseHandler() {
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
                    Utils.setData(result, Constant.NEWS_TITLE, 30);
                    mNewsTitleBean = (TitleBean) Utils.Parse(result, TitleBean.class);
                    State = LoadState.LoadSuccess;
                    setView();
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
                newsLoading();
                mTextview.setVisibility(View.VISIBLE);
                mMView.setVisibility(View.GONE);
            }
        });
    }
}



