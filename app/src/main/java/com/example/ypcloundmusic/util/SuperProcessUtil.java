package com.example.ypcloundmusic.util;
import android.os.Process;
/**
 * 进程工具类
 */
public class SuperProcessUtil {
    public static void killApp() {
        Process.killProcess(Process.myPid());
    }
}
