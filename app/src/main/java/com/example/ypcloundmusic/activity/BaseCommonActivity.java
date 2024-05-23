package com.example.ypcloundmusic.activity;

import android.content.Intent;

import com.example.ypcloundmusic.component.splash.activity.SplashActivity;
import com.example.ypcloundmusic.guide.GuideActivity;
import com.example.ypcloundmusic.util.Constant;

public class BaseCommonActivity extends BaseActivity {
    /**
     * 启动并销毁activity
     * @param clazz
     */
    protected void startActivityAfterFinishThis(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * 获取传过来的id
     */
    protected String extraId() {
        return extraString(Constant.ID);
    }

    protected String extraString(String key) {
        return getIntent().getStringExtra(key);
    }
}
