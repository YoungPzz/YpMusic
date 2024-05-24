package com.example.ypcloundmusic.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ypcloundmusic.R;

/**
 * 图片相关工具类
 */
public class ImageUtil {
    public static void show(Context context, ImageView itemView, String data) {
        if (data == null) return;

        if (data.isEmpty()) {
            //没有获取到资源
            //显示默认图片
            itemView.setImageResource(R.drawable.placeholder);
            return;
        }

        //把图片地址转换为绝对地址
        data = ResourceUtil.resourceUri(data);

        showFull(context, itemView, data);
    }

    /**
     * 显示本地图片
     *
     * @param context
     * @param view
     * @param data
     */
    public static void showLocalImage(Context context, ImageView view, String data) {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();

        //使用Glide显示图片
        Glide.with(context)
                .load(data)
                .apply(options)
                .into(view);
    }
    /**
     * 显示绝对地址图片
     *
     * @param context
     * @param itemView
     * @param data
     */
    public static void showFull(Context context, ImageView itemView, String data) {
        RequestOptions options = getCommonRequestOptions();

        //显示图片 异步下载图片展示在itemview空间上
        Glide.with(context)
                .load(data)
                .apply(options)
                .into(itemView);
    }

    public static RequestOptions getCommonRequestOptions() {
        //获取功能配置 通用配置
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.network_error);
        return options;
    }

    /**
     * 显示头像
     *
     * @param activity
     * @param view
     * @param uri
     */
    public static void showAvatar(Activity activity, ImageView view, String uri) {
        if (TextUtils.isEmpty(uri)) {
            //没有头像

            //显示默认头像
            show(activity, view, R.drawable.default_avatar);
        } else {
            //有头像

            if (uri.startsWith("http")) {
                //绝对路径
                showFull(activity, view, uri);
            } else {
                //相对路径
                show(activity, view, uri);
            }
        }
    }

    /**
     * 显示圆形资源目录图片
     *
     * @param activity
     * @param view
     * @param resourceId
     */
    public static void show(Activity activity, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {
        RequestOptions options = getCommonRequestOptions();

        //显示图片
        Glide.with(activity)
                .load(resourceId)
                .apply(options)
                .into(view);
    }
}
