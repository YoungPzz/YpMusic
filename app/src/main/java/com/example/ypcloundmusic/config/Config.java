package com.example.ypcloundmusic.config;

/**
 * 配置文件、可以配置API地址、第三方服务配置
 */
public class Config {
    /*
    是否是调试模式
     */
    public static final boolean DEBUG = true;
    //启动界面默认延迟时间
    /*
    默认延迟时间
     */
    public static int SPLASH_DEFAULT_DELAY_TIME  = 2000;

    /*
    API端口
     */
    public static String ENDPOINT = "http://my-cloud-music-api-sp3-dev.ixuea.com/";

    /*
    资源端点
     */
    public static String RESOURCE_ENDPOINT = "http://course-music-dev.ixuea.com/%s";


    /*
    网络缓存目录大小100M
     */
    public static final long NETWORK_CACHE_SIZE = 1024 * 1024 * 100;
}
