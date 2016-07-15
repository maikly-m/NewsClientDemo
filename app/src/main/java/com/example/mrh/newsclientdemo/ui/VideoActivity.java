package com.example.mrh.newsclientdemo.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.bean.TweetBean;


/**
 * Created by MR.H on 2016/7/15 0015.
 */
public class VideoActivity extends AppCompatActivity {
    private TextView tv_video;
    private VideoView sv_video;
    private TweetBean.Vedio mVedio;
    private String mMp4_url;
    private MediaController mMediaController;
    private ProgressBar pb_video;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mVedio = (TweetBean.Vedio) getIntent().getSerializableExtra("vedio");
        mMp4_url = mVedio.mp4_url;
        setContentView(R.layout.activity_video);
        initView();

    }

    private void initView () {
        tv_video = (TextView) findViewById(R.id.tv_video);
        sv_video = (VideoView) findViewById(R.id.sv_video);
        pb_video = (ProgressBar) findViewById(R.id.pb_video);
        tv_video.setText(mVedio.title);

        sv_video.setVideoPath(mMp4_url);
        mMediaController = new MediaController(this);
        sv_video.setMediaController(mMediaController);
        mMediaController.show(4000);
        sv_video.requestFocus();
        sv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared (MediaPlayer mp) {
                pb_video.setVisibility(View.GONE);
            }
        });
        sv_video.start();

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }

}
