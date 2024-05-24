package com.example.ypcloundmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.ypcloundmusic.player.activity.MusicPlayerActivity;
import com.example.ypcloundmusic.player.activity.SimplePlayerActivity;
import com.example.ypcloundmusic.player.activity.manager.MusicListManager;
import com.example.ypcloundmusic.service.MusicPlayerService;
import org.greenrobot.eventbus.EventBus;


/*

   项目中特有逻辑
 */
public class BaseLogicActivity extends BaseCommonActivity{


    /**
     * 进入音乐播放界面
     * @param activity：从哪个Activity播放
     * */

    public void startMusicPlayerActivity(Activity activity){
        //简单播放页面入口
//        Intent intent = new Intent(activity, SimplePlayerActivity.class);
        Intent intent = new Intent(activity, MusicPlayerActivity.class);

        startActivity(intent);
    }

    public MusicListManager getMusicListManager(){
        return MusicPlayerService.getListManager(getApplication());
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 是否注册EventBus
     *
     * @return
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 加载数据方法
     *
     * @param isPlaceholder 是否是通过placeholder控件触发的
     */
    protected void loadData(boolean isPlaceholder) {

    }


}
