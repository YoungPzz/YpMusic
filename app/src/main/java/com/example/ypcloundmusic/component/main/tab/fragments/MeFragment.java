package com.example.ypcloundmusic.component.main.tab.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ypcloundmusic.Fragment.BaseFragment;
import com.example.ypcloundmusic.R;

public class MeFragment extends BaseFragment {
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    public static MeFragment newInstance() {

        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
