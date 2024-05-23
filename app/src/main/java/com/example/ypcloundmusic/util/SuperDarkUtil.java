package com.example.ypcloundmusic.util;

import android.content.Context;
import android.content.res.Configuration;

public class SuperDarkUtil {
    //判断是不是深色模式
    public static boolean isDark(Context context){
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }
}
