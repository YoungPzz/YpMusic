package com.example.ypcloundmusic.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * 所有DialogFragment对话框的父类
 */
public abstract class BaseDialogFragment extends DialogFragment {
    /**
     * 找控件
     */
    protected void initViews() {
    }

    /**
     * 设置数据
     */
    protected void initDatum() {
    }

    /**
     * 设置监听器
     */
    protected void initListeners() {
    }

    @Nullable
    @Override
    /*
    该函数返回一个View，所以说在这个函数中绑定xml
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //再次封装
        View view = getLayoutView(inflater, container, savedInstanceState);
        return view;
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 该方法每个子类必须实现，所以定义为abstract抽象类
     */
    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /*
    View生成好后回调此函数
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initDatum();
        initListeners();
    }

    public <T extends View> T findViewById(@IdRes int id) {
        //由于在fragment中，不能直接findViewById找控件，所以在这里抽离一个方法，方便写代码
        return getView().findViewById(id);
    }

}
