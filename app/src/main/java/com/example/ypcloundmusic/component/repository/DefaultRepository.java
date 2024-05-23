package com.example.ypcloundmusic.component.repository;


import com.example.ypcloundmusic.Model.Base;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.component.ad.model.Ad;
import com.example.ypcloundmusic.component.api.DefaultService;
import com.example.ypcloundmusic.component.api.NetWorkModule;
import com.example.ypcloundmusic.component.feed.model.Feed;
import com.example.ypcloundmusic.component.login.activity.BaseId;
import com.example.ypcloundmusic.component.login.model.CodeRequest;
import com.example.ypcloundmusic.component.login.model.Session;
import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.component.song.model.User;
import com.example.ypcloundmusic.component.video.model.Video;
import com.example.ypcloundmusic.util.Constant;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * 本项目默认仓库
 * 主要是从网络、数据库获取知识
 * 目前项目中大部分操作都在这里
 *
 * 如果项目每个模块之间有明显的区别，例如：有商城、有歌单，那可以放到对应模块的Repository
 *
 * 单例模式
 */
public class DefaultRepository {

    private static DefaultRepository instance;
    private final DefaultService service;

    public DefaultRepository() {
        OkHttpClient okHttpClient = NetWorkModule.provideOkHttpClient();
        Retrofit retrofit = NetWorkModule.provideRetrofit(okHttpClient);
        service = retrofit.create(DefaultService.class);
    }
    /**
     * 单例模式
     * 由于Android中的并发量不高，可以直接使用这种最简单的单例模式
     * @return
     */
    public static DefaultRepository getInstance() {
        if(instance == null) {
            instance = new DefaultRepository();
        }
        return instance;
    }

    /**
     * 广告列表
     *
     * @return
     */
    public Observable<ListResponse<Ad>> ads(int position) {
        return service.ads(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 首页banner界面广告
     * 传0到position代表取到的是广告列表
     * @return
     */
    public Observable<ListResponse<Ad>> bannerAd() {
        return ads(Constant.VALUE0);
    }

    /**
     * 歌单列表
     *
     * @return
     */
    public Observable<ListResponse<Sheet>> sheets(String category) {
        return sheets(category, Constant.SIZE10);
    }

    /**
     * 歌单列表
     *
     * @return
     */
    public Observable<ListResponse<Sheet>> sheets(int size) {
        return sheets(null, size);
    }

    /**
     * 歌单列表
     *
     * @return
     */
    public Observable<ListResponse<Sheet>> sheets(String category, int size) {
        return service.sheets(category, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 单曲
     *
     * @return
     */
    public Observable<ListResponse<Song>> songs() {
        return service.songs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 登录
     *
     * @param data
     * @return
     */
    public @NonNull Observable<DetailResponse<Session>> login(User data) {
        return service.login(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册
     *
     * @param data
     * @return
     */
    public Observable<DetailResponse<BaseId>> register(User data) {
        return service.register(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送验证码
     *
     * @param style 0:邮件验证码，10：短信验证码
     * @param data
     * @return
     */
    public Observable<DetailResponse<Base>> sendCode(int style, CodeRequest data) {
        return service.sendCode(style, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 校验验证码
     *
     * @param data
     * @return
     */
    public Observable<DetailResponse<Base>> checkCode(CodeRequest data) {
        return service.checkCode(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 歌单详情
     *
     * @param id
     * @return
     */
    public Observable<DetailResponse<Sheet>> sheetDetail(String id) {
        return service.sheetDetail("testHeader", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 单曲详情
     *
     * @return
     */
    public Observable<DetailResponse<Song>> songDetail(String id) {
        return service.songDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 视频列表
     *
     * @return
     */
    public Observable<ListResponse<Video>> videos(int page) {
        return service.videos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 视频详情
     *
     * @param id
     * @return
     */
    public Observable<DetailResponse<Video>> videoDetail(String id) {
        return service.videoDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 动态列表
     * 传UserId数据就是该用户的
     * 不传就是全部
     *
     * @param userId
     * @return
     */
    public Observable<ListResponse<Feed>> feeds(String userId) {
        //创建查询参数
        Map<String, String> datum = new HashMap<>();

        if (StringUtils.isNotBlank(userId)) {
            //添加用户id
            datum.put(Constant.USER_ID, userId);
        }

        return service.feeds(datum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发布动态
     *
     * @param data
     * @return
     */
    public Observable<DetailResponse<Base>> createFeed(Feed data) {
        return service.createFeed(data)
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }
}
