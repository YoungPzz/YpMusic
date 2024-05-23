package com.example.ypcloundmusic.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.ypcloundmusic.player.activity.impl.MusicListManagerImpl;
import com.example.ypcloundmusic.player.activity.manager.MusicListManager;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerManager;
import com.example.ypcloundmusic.player.activity.impl.MusicPlayerManagerImpl;
import com.example.ypcloundmusic.util.NotificationUtil;
import com.example.ypcloundmusic.util.ServiceUtil;

/**
 * 音乐播放service
 */
public class MusicPlayerService extends Service {
    private static Context context;

    /**
     * 获取音乐播放Manager
     * <p>
     * 为什么不直接将逻辑写到Service呢？
     * 是因为操作service要么通过bindService
     * 那么startService来使用
     * 比较麻烦
     *
     * @return
     */
    public static MusicPlayerManager getMusicPlayerManager(Context context) {
        MusicPlayerService.context = context.getApplicationContext();

        //启动service
        ServiceUtil.startService(MusicPlayerService.context, MusicPlayerService.class);

        return MusicPlayerManagerImpl.getInstance(MusicPlayerService.context);
    }
    /**
     * 获取音乐播放Manager
     */
    public static MusicListManager getListManager(Context context) {
        MusicPlayerService.context = context.getApplicationContext();

        //启动service
        ServiceUtil.startService(MusicPlayerService.context, MusicPlayerService.class);

        return MusicListManagerImpl.getInstance(MusicPlayerService.context);
    }

    /**
     * 第一次启动service会启动
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 启动service调用，多次启动也调用该方法
     * @param intent The Intent supplied to {@link android.content.Context#startService},
     * as given.  This may be null if the service is being restarted after
     * its process has gone away, and it had previously returned anything
     * except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to
     * start.  Use with {@link #stopSelfResult(int)}.
     *
     * @return
     */

    @SuppressLint("NotificationId0")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //因为这个API是8.0才有的，前台service，所以要这样判断版本
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //设置service为前台service
            //提高应用的优先级

            //获取通知 返回值是一个notification
            Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());

            //Id写0表示这个通知不会显示 对于我们这里来说不需要显示
            startForeground(10, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Service销毁
     */
    @Override
    public void onDestroy() {
        /*
        true移除通知
         */
        stopForeground(true);
        super.onDestroy();
    }
}
