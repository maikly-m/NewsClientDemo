package com.example.mrh.newsclientdemo.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mrh.newsclientdemo.ActivityManager;
import com.example.mrh.newsclientdemo.utils.ThreadManager;

import java.util.List;

/**
 * Created by MR.H on 2016/7/12 0012.
 */
public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        ThreadManager.getThreadPool().startThread(new Runnable() {
            @Override
            public void run () {
                while (true){
                    SystemClock.sleep(5000);
                    List<Activity> activityList = ActivityManager.getActivityManager()
                            .getActivityList();
                    Log.d("TAG", "List<Activity>.size: " + activityList.size());
                    for (int i = 0; i < activityList.size(); i++){
                        Log.d("TAG", "List<Activity>.size: " + activityList.get(i)
                                .getComponentName());
                    }
                }
            }
        });
    }
}
