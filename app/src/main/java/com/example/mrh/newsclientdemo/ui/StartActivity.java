package com.example.mrh.newsclientdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.R;
import com.example.mrh.newsclientdemo.service.TestService;
import com.example.mrh.newsclientdemo.utils.ThreadManager;
import com.example.mrh.newsclientdemo.utils.Utils;

/**
 * 启动界面
 * Created by MR.H on 2016/7/12 0012.
 */
public class StartActivity extends AppCompatActivity {
    private ImageView iv_logo;
    private TextView tv_logo;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        ActivityManager.getActivityManager().addActivity(this);

        //测试后台activity个数和名称
//        Intent intent = new Intent(this, TestService.class);
//        startService(intent);

        String filePath = this.getCacheDir().getAbsolutePath();
        Utils.setMyCachePath(filePath);
        initView();
        initAnimation();
    }

    private void initAnimation () {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, ScaleAnimation
                .RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(2000);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        iv_logo.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart (Animation animation) {

            }

            @Override
            public void onAnimationEnd (Animation animation) {
                ThreadManager.getThreadPool().startThread(new Runnable() {
                    @Override
                    public void run () {
                        SystemClock.sleep(1000);
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManager.getActivityManager().removeActivity(StartActivity.this);
                    }
                });

            }

            @Override
            public void onAnimationRepeat (Animation animation) {

            }
        });
    }

    private void initView () {
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        tv_logo = (TextView) findViewById(R.id.tv_logo);
    }
}
