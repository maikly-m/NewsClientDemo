package com.example.mrh.newsclientdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrh.newsclientdemo.ui.MainActivity;

/**
 * Created by MR.H on 2016/7/7 0007.
 */
public class MyFragment extends BaseFrameFragment {
    public MyFragment(){

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
        if (supportActionBar.isShowing()){
            supportActionBar.hide();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
