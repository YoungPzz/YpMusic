package com.example.ypcloundmusic.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 偏好设置工具类
 * 是否登录了，是否显示引导界面，保存用户Id等
 * 用单例模式
 */
public class DefaultPreferenceUtil {
    private static final String NAME = "YpCloudMusic";
    public static final String TERMS_SERVICE = "TERMS_SERVICE";
    private static DefaultPreferenceUtil instance;
    private final Context context;
    private final SharedPreferences preference;

    public DefaultPreferenceUtil(Context context) {
        //为了保存传进来的context,如果是this.context = context。假设是某activity调用，context就代表该activity
        //所以以这种方法保存activity，就可以引发内存泄漏的问题
        //context.getApplicationContext();为整个应用的context，脱离了activity，就不会发生内存泄漏
        this.context = context.getApplicationContext();
        preference = this.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取偏好设置的单例
     * @param context
     * @return
     */
    public synchronized static DefaultPreferenceUtil getInstance(Context context){
        if( instance == null) {
            instance = new DefaultPreferenceUtil(context);
        }
        return instance;
    }

    /**
     * 设置同意了用户协议
     */
    public void setAcceptTermsServiceAgreement() {
        putBoolean(TERMS_SERVICE, true);
    }

    public void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
        //editor.apply(); apply是异步操作的，如果想apply之后立刻获取数据可能会出错
    }
    /**
     * 获取是否同意了用户条款
     *
     * @return
     */
    public boolean isAcceptTermsServiceAgreement() {
        return preference.getBoolean(TERMS_SERVICE, false);
    }


}
