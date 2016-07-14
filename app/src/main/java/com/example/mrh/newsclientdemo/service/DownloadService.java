package com.example.mrh.newsclientdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by MR.H on 2016/7/7 0007.
 */
public class DownloadService extends Service{

    private IBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        mBinder = new ServiceBinder();

    }

    public DownloadService () {
        super();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind (Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind (Intent intent) {
        super.onRebind(intent);
    }

    private class ServiceBinder extends Binder {

    }
}
