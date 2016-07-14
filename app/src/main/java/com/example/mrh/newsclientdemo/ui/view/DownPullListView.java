package com.example.mrh.newsclientdemo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mrh.newsclientdemo.R;

/**
 *
 * Created by MR.H on 2016/6/11 0011.
 */
public class DownPullListView extends ListView implements AbsListView.OnScrollListener{

    private View foot_pull_to_refresh;
    private ProgressBar pb_refresh;
    private TextView tv_refresh;
    private int footerHeight;
    private boolean isLoadMore = false;
    private OnFooterRefresh onFooterRefresh;

    public DownPullListView (Context context) {
        super(context);
        initFooterView();
    }

    public DownPullListView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFooterView();
    }

    public DownPullListView (Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
    }
    /**
     * 初始化脚布局
     */
    private void initFooterView () {
        foot_pull_to_refresh = View.inflate(getContext(), R.layout.foot_pull_to_refresh, null);
        pb_refresh = (ProgressBar) foot_pull_to_refresh.findViewById(R.id.pb_refresh);
        tv_refresh = (TextView) foot_pull_to_refresh.findViewById(R.id.tv_refresh);
        this.addFooterView(foot_pull_to_refresh);
        foot_pull_to_refresh.measure(0, 0);
        footerHeight = foot_pull_to_refresh.getMeasuredHeight();
        foot_pull_to_refresh.setPadding(0, -footerHeight, 0, 0);
        this.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged (AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() == getCount()-1
                && !isLoadMore){
            foot_pull_to_refresh.setPadding(0, 0, 0, 0);
            isLoadMore = true;
            setSelection(getCount()-1);
            if (onFooterRefresh != null){
                onFooterRefresh.loadingMore();
            }
        }
        isLoadMore = false;
    }
    public void shutDownRefresh(boolean shut){
        if (shut){
            foot_pull_to_refresh.setPadding(0, -footerHeight, 0, 0);
        }
    }

    @Override
    public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {

    }
    public interface OnFooterRefresh{

        void loadingMore ();
    }
    public void setOnFooterRefresh(OnFooterRefresh listener){
        onFooterRefresh = listener;
    }

}

