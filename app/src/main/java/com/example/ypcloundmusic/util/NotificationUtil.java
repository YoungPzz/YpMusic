package com.example.ypcloundmusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.ypcloundmusic.AppContext;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.sheet.model.activity.SheetDetailActivity;
import com.example.ypcloundmusic.player.activity.SimplePlayerActivity;

/**
 * 通知相关工具类
 */
public class NotificationUtil {

    private static final String CHANNEL_ID_MUSIC = "CHANNEL_ID_MUSIC";
    private static final String  CHANNEL_ID_DEFAULT = "CHANNEL_ID_DEFAULT";
    private static NotificationManager notificationManager;

    public static void showAlert(int message) {

        //获取通知管理器
        getNotificationManager();

        AppContext context = AppContext.getInstance();

        //创建通知渠道
        //所谓的渠道，将通知进行归类。比如活动通知、消息通知，用户可以自行选择是否接受
        //把不同的通知放到不同的渠道
        createNotificationChannel(CHANNEL_ID_MUSIC, AppContext.getInstance().getString(R.string.channel_music));

        //设置通知点击后启动的界面
        Intent intent = new Intent(context, SimplePlayerActivity.class);
        intent.putExtra(Constant.ID, "1");

        //通知要使用到的PendingIntent
        //简单来说如果要在通知里点击之后跳转到activity，就必须要有一个pendingIntent
        //最后一个参数的意义：多个通知一样的pending以最后一个诶准
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_MUSIC)
                //设置通知的标题
                .setContentTitle(context.getString(R.string.app_name))
                //通知内容
                .setContentText(context.getString(message))
                //通知小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                //点击后消失
                .setAutoCancel(true)
                .build();

        notify(context, "showAlert".hashCode(), notification);
    }

    /**
     *
     * @param context
     * @param id:用于标识通知，如果有多个同id的通知，只会显示一个
     * @param notification
     */
    private static void notify(AppContext context, int id, Notification notification) {
        notificationManager.notify(id, notification);
    }

    /**
     * @param id
     * @param string 渠道的名称
     */
    private static void createNotificationChannel(String id, String string) {
        //因为这个API是8。0才有的
        //所以要判断版本
        //不然低版本会奔溃
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道 可以多次创建 但一个id只会创建一个
            //第三个为通知的优先级
            NotificationChannel channel = new NotificationChannel(id, string, NotificationManager.IMPORTANCE_HIGH);
            //长按桌面图标时显示该渠道的通知
            channel.setShowBadge(true);
            //桌面图标显示角标 可能什么MIUI没有效果
            channel.enableLights(true);
            //创建渠道
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static void getNotificationManager() {
        if (notificationManager == null) {
            //获取通知管理器的规定写法
            //TODO 理解
            notificationManager = (NotificationManager) AppContext.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }


    public static Notification getServiceForeground(Context context) {
        //获取通知管理器
        getNotificationManager();
        createNotificationChannel(CHANNEL_ID_DEFAULT, AppContext.getInstance().getString(R.string.channel_default));

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID_MUSIC)
                //设置通知的标题
                .setContentTitle(context.getString(R.string.app_name))
                //通知内容
                .setContentText("")
                //通知小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .build();

        return notification;
    }
}
