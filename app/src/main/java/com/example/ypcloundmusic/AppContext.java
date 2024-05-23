package com.example.ypcloundmusic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ypcloundmusic.component.login.event.LoginStatusChangedEvent;
import com.example.ypcloundmusic.component.login.model.Session;
import com.example.ypcloundmusic.util.MyActivityManager;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import superui.text.toast.SuperToast;

/**
 * 全局Application
 */
public class AppContext extends Application implements Application.ActivityLifecycleCallbacks {
    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }

    private MyActivityManager myActivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //注册生命周期声明监听
        registerActivityLifecycleCallbacks(this);
        myActivityManager = MyActivityManager.getInstance();
        initMMKV();
        SuperToast.init(getApplicationContext());
        //配置 LitePal
        LitePal.initialize(this);

        //创建数据库
        Connector.getDatabase();
    }

    private void initMMKV() {
        String rootDir = MMKV.initialize(this); //对MMKV进行初始化，返回其数据存储的目录
    }


    //登录状态改变
    public void onLogin(Session data) {
        loginStatusChanged();
    }

    private void loginStatusChanged() {
        EventBus.getDefault().post(new LoginStatusChangedEvent(false));
    }


    //region activity生命周期监听
    //如一个activity创建了，会把该activity传来
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        myActivityManager.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        //当一个activity被销毁时触发
        myActivityManager.remove(activity);
    }
    //endregion
}
