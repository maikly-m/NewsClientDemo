package com.example.mrh.newsclientdemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.bean.TweetBean;
import com.example.mrh.newsclientdemo.constant.Constant;
import com.example.mrh.newsclientdemo.http.HttpClient;
import com.example.mrh.newsclientdemo.ui.MainActivity;
import com.example.mrh.newsclientdemo.ui.VideoActivity;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 视频
 * Created by MR.H on 2016/7/7 0007.
 */
public class TweetFragment extends BaseFrameFragment {
    private FrameLayout mRootView;
    private GridView mGv_tweet_vedio;
    private View mView;
    private FrameLayout.LayoutParams mLp;
    private View mTextview;
    private TweetBean mTweetBean;
    private View mMView;
    private Button mBnt_loading_error;
    private List<TweetBean.Vedio> mVedios;
    private TweetAdapter mTweetAdapter;
    private boolean isLoadMore = false;
    private int footerHeight;
    private int mPage = 1;
    private LinearLayout foot_refresh;
    private boolean isMore = true;
    private TextView tv_more_content;

    public TweetFragment () {

    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        ActionBar supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (!supportActionBar.isShowing()){
            supportActionBar.show();
        }
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
            DataLoading();
        }
    }

    //加载数据
    public void DataLoading () {

        String result = Utils.getData(Constant.NEWS_VEDIO);
        if (result != null){
            setView(result);
        } else{
            AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
            httpClient.get(Constant.NEWS_VEDIO, new TextHttpResponseHandler() {
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
                    Utils.setData(result, Constant.NEWS_VEDIO, 10);
                    setView(result);
                }
            });
        }
    }

    //数据解析后,mView添加到mRootView中
    private void setView (String result) {
        String mResult = Utils.resetVedioName(result);
        mTweetBean = (TweetBean) Utils.Parse(mResult, TweetBean.class);
        mVedios = mTweetBean.vedios;

        mView = View.inflate(getContext(), R.layout.tweet_vedio, null);
        mGv_tweet_vedio = (GridView) mView.findViewById(R.id.gv_tweet_vedio);
        foot_refresh = (LinearLayout) mView.findViewById(R.id.foot_refresh);
        tv_more_content = (TextView) mView.findViewById(R.id.tv_more_content);

        foot_refresh.measure(0, 0);
        footerHeight = foot_refresh.getMeasuredHeight();
        foot_refresh.setPadding(0, -footerHeight, 0, 0);

        mTweetAdapter = new TweetAdapter();
        mGv_tweet_vedio.setAdapter(mTweetAdapter);

        mRootView.addView(mView, mLp);
        mTextview.setVisibility(View.GONE);

        //点击事件总汇
        onClickEvent();
    }

    private void onClickEvent () {
        //gridview条目点击事件
        mGv_tweet_vedio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), VideoActivity.class);
                TweetBean.Vedio vedio = mVedios.get(position);
                intent.putExtra("vedio", vedio);
                startActivity(intent);
            }
        });
        //下拉刷新
        mGv_tweet_vedio.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged (AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && mGv_tweet_vedio.getLastVisiblePosition()
                        == mGv_tweet_vedio.getCount() - 1
                        && !isLoadMore){
                    foot_refresh.setPadding(0, 0, 0, 0);
                    isLoadMore = true;
                    mGv_tweet_vedio.setSelection(mGv_tweet_vedio.getCount() - 1);

                    if (mPage % 3 == 0){
                        isMore = false;
                        tv_more_content.setVisibility(View.VISIBLE);
                    }
                    if (isMore){
                        DataMoreLoading();
                    }
                }

            }

            @Override
            public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount,
                                  int totalItemCount) {

            }
        });
        //是否加载更多
        tv_more_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                isMore = true;
                tv_more_content.setVisibility(View.GONE);
                DataMoreLoading();
            }
        });
    }

    //加载更多数据
    public void DataMoreLoading () {
        AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
        httpClient.get(Constant.NEWS_VEDIO, new TextHttpResponseHandler() {
            @Override
            public void onFailure (int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                isLoadMore = false;
            }

            @Override
            public void onSuccess (int statusCode, Header[] headers, String
                    responseString) {
                //记录页面刷新次数
                mPage = mPage + 1;
                String result = responseString;
                Utils.setData(result, Constant.NEWS_VEDIO, 10);
                String mResult = Utils.resetVedioName(result);
                TweetBean tweetBean = (TweetBean) Utils.Parse(mResult, TweetBean.class);
                List<TweetBean.Vedio> vedios = tweetBean.vedios;
                for (int i = 0; i < vedios.size(); i++){
                    mVedios.add(vedios.get(i));
                }
                mTweetAdapter.notifyDataSetChanged();
                isLoadMore = false;
                foot_refresh.setPadding(0, -footerHeight, 0, 0);
            }
        });
    }

    class TweetAdapter extends BaseAdapter {

        @Override
        public int getCount () {
            return mVedios.size();
        }

        @Override
        public Object getItem (int position) {
            return mVedios.get(position);
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = View.inflate(getContext(), R.layout.tweet_vedio_gridview, null);
                viewHolder = ViewHolder.getViewHolder(convertView);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Picasso.with(getContext()).load(mVedios.get(position).cover).into(viewHolder
                    .iv_gridview_image);
            viewHolder.tv_gridview_title.setText(mVedios.get(position).title);
            viewHolder.tv_gridview_name.setText(mVedios.get(position).topicName);
            return convertView;
        }


    }

    static class ViewHolder {
        public View rootView;
        public ImageView iv_gridview_image;
        public TextView tv_gridview_title;
        public TextView tv_gridview_name;

        private ViewHolder (View rootView) {
            this.rootView = rootView;
            this.iv_gridview_image = (ImageView) rootView.findViewById(R.id.iv_gridview_image);
            this.tv_gridview_title = (TextView) rootView.findViewById(R.id.tv_gridview_title);
            this.tv_gridview_name = (TextView) rootView.findViewById(R.id.tv_gridview_name);
            rootView.setTag(this);
        }

        public static ViewHolder getViewHolder (View rootView) {
            return new ViewHolder(rootView);
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
                DataLoading();
                mTextview.setVisibility(View.VISIBLE);
                mMView.setVisibility(View.GONE);
            }
        });
    }
}
