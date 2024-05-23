package com.example.ypcloundmusic.util;

//将网络获取到的相对地址转换为绝对路径

import com.example.ypcloundmusic.config.Config;

public class ResourceUtil {
    public static String resourceUri(String data) {
        return String.format(Config.RESOURCE_ENDPOINT, data);
    }
}
