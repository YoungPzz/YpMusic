package com.example.ypcloundmusic.util;

import android.app.Activity;

import com.example.ypcloundmusic.MainActivity;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 界面管理器
 * <p>
 * 用来保存当前应用所有开启的界面，目的是方便关闭到指定界面
 * 例如：在首页，跳转到登录首页，然后跳转到用户名登录界面，登录成功后，需要关闭最后两个界面
 */
public class MyActivityManager {
    /**
     * 已经显示的界面
     * 装在列表里面的目的是
     * 当退出后
     * 要关闭所有界面
     * 因为我们这个应用是只有登录了
     * 才能查看主界面
     * 之所以使用Set
     * 是因为他不会保存重复元素
     */
    private static Set<Activity> activities = new LinkedHashSet<>();
    /**
     * 实例
     */
    private static MyActivityManager instance;
    /**
     * 获取实例
     *
     * @return
     */
    public static MyActivityManager getInstance() {
        if (instance == null) {
            instance = new MyActivityManager();
        }
        return instance;
    }
    /**
     * 添加界面
     *
     * @param activity
     */
    public void add(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除界面
     *
     * @param activity
     */
    public void remove(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有界面
     */
    public void clear() {
        //for (Activity oldActivity : activities) {
        //    oldActivity.finish();
        //
        //}
        //使用迭代器遍历
        //否则可能会发生并发修改列表异常
        //因为在onActivityDestroyed方法中
        //从列表移除界面
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
        }
    }

    /**
     * 关闭到主界面
     */
    public void finishToMain() {
        LinkedList<Activity> list = new LinkedList<>(activities);
        Iterator<Activity> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity instanceof MainActivity) {
                break;
            }
            activity.finish();
        }
    }

    /**
     * 关闭所有界面
     */
    public void finishAll() {
        LinkedList<Activity> list = new LinkedList<>(activities);
        Iterator<Activity> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.finish();
        }
    }
}
