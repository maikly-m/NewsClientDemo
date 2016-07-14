package com.example.mrh.newsclientdemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.bean.ViewPagerImageBean;
import com.example.mrh.newsclientdemo.constant.Constant;
import com.example.mrh.newsclientdemo.http.HttpClient;
import com.example.mrh.newsclientdemo.ui.fragment.NewsBaseFragment;
import com.example.mrh.newsclientdemo.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MR.H on 2016/7/10 0010.
 */
public class HeaderPictureActivity extends AppCompatActivity {
    private ViewPager vp_header_picture;
    private PictureAdapter mPictureAdapter;
    private int mTag;
    private String mUrl;
    private String mImgsrc;
    private String mDigest;
    private String mURL;
    private ViewPagerImageBean mImageBean;
    private List<ViewPagerImageBean.Photo> photoUrl;
    private List<Integer> mList = new ArrayList<>();
    private TextView mTv_image;
    private TextView mTv_image_title;
    private TextView mTv_image_sum;
    private View mRootView;
    private ImageView mIv_image;
    private ProgressBar mPb_image;
    private Toolbar mTb_header_picture;
    private View mView;
    private Button mBnt_loading_error;
    private ViewGroup.LayoutParams mLp;
    private LinearLayout ll_header;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_picture);
        ActivityManager.getActivityManager().addActivity(this);
        mTag = getIntent().getIntExtra("INTNET_TAG", -1);
        if (mTag == NewsBaseFragment.INTNET_TAG_MADS){
            mUrl = getIntent().getStringExtra("url");
        } else if (mTag == NewsBaseFragment.INTNET_TAG_MIMAGE){
            mImgsrc = getIntent().getStringExtra("imgsrc");
            mDigest = getIntent().getStringExtra("digest");
        }
        initView();
        if (mUrl != null){
            initListener();
            getData();
        }
    }

    private void initListener () {
        vp_header_picture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected (int position) {
                int mPosition = position +1;
                mTv_image_sum.setText(mPosition +"/"+ mImageBean.imgsum);
                mTv_image.setText(photoUrl.get(position).note);
            }

            @Override
            public void onPageScrollStateChanged (int state) {

            }
        });
    }

    //获取数据
    private void getData () {
        mURL = Constant.NEWS_VIEWPAGER_IMAGE + mUrl + ".json";
        String result = Utils.getData(mURL);
        if (result != null){
            updateView(result);
        } else{

            AsyncHttpClient httpClient = HttpClient.getAsyncHttpClient();
            httpClient.get(mURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure (int statusCode, Header[] headers, String
                        responseString, Throwable throwable) {
                    loadErrorView();
                }

                @Override
                public void onSuccess (int statusCode, Header[] headers, String
                        responseString) {
                    ll_header.setVisibility(View.VISIBLE);
                    vp_header_picture.setVisibility(View.VISIBLE);
                    String result = responseString;
                    Utils.setData(result, mURL);
                    updateView(result);
                }
            });
        }
    }
    //连接失败显示页面
    private void loadErrorView () {
        //防止多次加入mView
        if (mView == null){
            mView = View.inflate(this, R.layout.loading_error, null);
            mBnt_loading_error = (Button) mView.findViewById(R.id.bnt_loading_error);
            mLp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.addContentView(mView, mLp);
        }
        ll_header.setVisibility(View.GONE);
        vp_header_picture.setVisibility(View.GONE);
        mView.setVisibility(View.VISIBLE);
        mBnt_loading_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //重新加载
                getData();
                ll_header.setVisibility(View.VISIBLE);
                vp_header_picture.setVisibility(View.VISIBLE);
                mView.setVisibility(View.GONE);
            }
        });
    }
    //更新数据
    private void updateView (String result) {

        mImageBean = (ViewPagerImageBean) Utils.Parse(result, ViewPagerImageBean.class);
        photoUrl = mImageBean.photos;
        mPictureAdapter.notifyDataSetChanged();
        vp_header_picture.setCurrentItem(0);
        mTv_image_title.setText(mImageBean.setname);
        mTv_image_sum.setText(1 +"/"+ mImageBean.imgsum);
        mTv_image.setText(photoUrl.get(0).note);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }

    private void initView () {
        mTb_header_picture = (Toolbar) findViewById(R.id.tb_header_picture);
        mTb_header_picture.setTitle("");
        setSupportActionBar(mTb_header_picture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_bule);

        mList.add(R.drawable.default_bg);
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        vp_header_picture = (ViewPager) findViewById(R.id.vp_header_picture);
        mTv_image = (TextView) findViewById(R.id.tv_image);
        mTv_image_title = (TextView) findViewById(R.id.tv_image_title);
        mTv_image_sum = (TextView) findViewById(R.id.tv_image_sum);
        mPictureAdapter = new PictureAdapter();
        vp_header_picture.setAdapter(mPictureAdapter);

    }

    class PictureAdapter extends PagerAdapter {

        @Override
        public int getCount () {
            if (mTag == NewsBaseFragment.INTNET_TAG_MADS){
                if (mImageBean == null){
                    return mList.size();
                } else{
                    return photoUrl.size();
                }
            } else if (mTag == NewsBaseFragment.INTNET_TAG_MIMAGE){
                return 1;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject (View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem (ViewGroup container, int position) {
            mRootView = View.inflate(HeaderPictureActivity.this, R.layout
                    .news_viewpager_image, null);
            mIv_image = (ImageView) mRootView.findViewById(R.id.iv_image);
            mPb_image = (ProgressBar) mRootView.findViewById(R.id.pb_image);
            mRootView.setTag(position);
            if (mTag == NewsBaseFragment.INTNET_TAG_MADS){
                if (mImageBean == null){
                    mPb_image.setVisibility(View.VISIBLE);

                } else{
                    mPb_image.setVisibility(View.INVISIBLE);
                    Picasso.with(HeaderPictureActivity.this).load(photoUrl.get(position).imgurl)
                            .into(mIv_image);
                }
            } else if (mTag == NewsBaseFragment.INTNET_TAG_MIMAGE){
                mPb_image.setVisibility(View.INVISIBLE);
                Picasso.with(HeaderPictureActivity.this).load(mImgsrc).into(mIv_image);
                mTv_image.setText(mDigest);
            }
            container.addView(mRootView);
            return mRootView;
        }

        @Override
        public void destroyItem (ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        //解决数据更新第一条刷新的问题
        @Override
        public int getItemPosition (Object object) {
            View view = (View) object;
            if (view.getTag().equals(0)){
                return POSITION_NONE;
            }else {
                return POSITION_UNCHANGED;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
        case R.id.picture_share:

            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.header_picture, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
