package com.example.ypcloundmusic.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;


import org.greenrobot.eventbus.EventBus;

import superui.text.SuperClickableSpan;

public class SpannableStringBuilderUtil {
    /**
     * 向SpannableStringBuilder扩展用户点击方法
     *
     * @param start 开始位置
     * @param end   结束位置，不包括
     * @param data  消息
     */
    public static void setUserClickSpan(SpannableStringBuilder builder, int start, int end, String data) {
        builder.setSpan(new SuperClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
//                EventBus.getDefault().post(new UserDetailEvent(data));
                Log.d("username",data);
            }
        }, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }
}
