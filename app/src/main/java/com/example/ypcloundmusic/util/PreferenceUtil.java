package com.example.ypcloundmusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;

/**
 * MMKV框架
 */
public class PreferenceUtil {
    private static final String SHOW_GUIDE = "SHOW_GUIDE";

    private static final String SESSION = "SESSION";

    private static final String USERID = "USERID";

    private static final String CHATTOKEN = "CHATTOKEN";

    private static final String ISLOGIN = "ISLOGIN";



    private static PreferenceUtil instance;
    private final Context context;
    private final MMKV preference;

    public PreferenceUtil(Context context) {
        //为了保存传进来的context,如果是this.context = context。假设是某activity调用，context就代表该activity
        //所以以这种方法保存activity，就可以引发内存泄漏的问题
        //context.getApplicationContext();为整个应用的context，脱离了activity，就不会发生内存泄漏
        this.context = context.getApplicationContext();
        preference = MMKV.defaultMMKV();
    }

    /**
     * 获取偏好设置的单例
     * @param context
     * @return
     */
    public synchronized static PreferenceUtil getInstance(Context context){
        if( instance == null) {
            instance = new PreferenceUtil(context);
        }
        return instance;
    }

    /**
     * 是否显示引导界面
     *
     * @return
     */
    public boolean isShowGuide() {
        return getBoolean(SHOW_GUIDE, true);
    }


    /**
     * 设置是否显示引导界面
     *
     * @param value
     */
    public void setShowGuide(boolean value) {
        putBoolean(SHOW_GUIDE, value);
    }


    //region 辅助方法
    public void putBoolean(String key, Boolean value){
        preference.putBoolean(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return preference.getBoolean(key, value);
    }


    //endregion
    public void setSession(String session) {
        putString(SESSION, session);
    }

    private void putString(String key, String value) {
        preference.edit().putString(key, value).apply();

    }

    public void setUserId(String userId) {
        putString(USERID, userId);
    }
    public String getUserId( ){
        return preference.getString(USERID, null);
    }

    public void setChatToken(String chatToken) {
        putString(CHATTOKEN, chatToken);

    }

    public boolean isLogin() {
        return getBoolean(ISLOGIN, true);
    }
}
