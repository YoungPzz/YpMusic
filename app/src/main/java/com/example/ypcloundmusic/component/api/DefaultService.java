package com.example.ypcloundmusic.component.api;


import com.example.ypcloundmusic.Model.Base;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.component.ad.model.Ad;
import com.example.ypcloundmusic.component.comment.model.Comment;
import com.example.ypcloundmusic.component.feed.model.Feed;
import com.example.ypcloundmusic.component.login.activity.BaseId;
import com.example.ypcloundmusic.component.login.model.CodeRequest;
import com.example.ypcloundmusic.component.login.model.Session;
import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.component.song.model.User;
import com.example.ypcloundmusic.component.video.model.Video;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 默认远程数据源
 */
public interface DefaultService {
    /**
     * 歌单列表
     *
     * @return
     */
    @GET("v1/sheets")
    Observable<ListResponse<Sheet>> sheets(@Query(value = "category") String category, @Query(value = "size") int size);

    /**
     * 歌单详情
     *
     * @return
     */
    @GET("v1/sheets/{id}")
    Observable<DetailResponse<Sheet>> sheetDetail(@Header("testHeader") String testHeader, @Path("id") String id);
    /**
     * 评论列表
     *
     * @return
     */
    @GET("v1/comments")
    Observable<ListResponse<Comment>> comments();

    /**
     * 广告列表
     *
     * @return
     */
    @GET("v1/ads")
    Observable<ListResponse<Ad>> ads(@Query(value = "position") int position);

    /**
     * 单曲
     *
     * @return
     */
    @GET("v1/songs")
    Observable<ListResponse<Song>> songs();

    /**
     * 登录
     *
     * @param data
     * @return
     */
    @POST("v1/sessions")
    Observable<DetailResponse<Session>> login(@Body User data);

    /**
     * 注册
     *
     * @param data
     * @return
     */
    @POST("v1/users")
    Observable<DetailResponse<BaseId>> register(@Body User data);


    /**
     * 发送验证码
     *
     * @param data
     * @return
     */
    @POST("v1/codes")
    Observable<DetailResponse<Base>> sendCode(@Query(value = "style") int style, @Body CodeRequest data);

    /**
     * 校验验证码
     *
     * @param data
     * @return
     */
    @POST("v1/codes/check")
    Observable<DetailResponse<Base>> checkCode(@Body CodeRequest data);

    /**
     * 单曲详情
     *
     * @param id
     * @return
     */
    @GET("v1/songs/{id}")
    Observable<DetailResponse<Song>> songDetail(@Path("id") String id);

    /**
     * 视频列表
     *
     * @return
     */
    @GET("v1/videos")
    Observable<ListResponse<Video>> videos(@Query(value = "page") int page);

    /**
     * 视频详情
     *
     * @param id
     * @return
     */
    @GET("v1/videos/{id}")
    Observable<DetailResponse<Video>> videoDetail(@Path("id") String id);

    /**
     * 动态列表
     *
     * @param data
     * @return
     */
    @GET("v1/feeds")
    Observable<ListResponse<Feed>> feeds(@QueryMap Map<String, String> data);

    /**
     * 发布动态
     *
     * @param data
     * @return
     */
    @POST("v1/feeds")
    Observable<DetailResponse<Base>> createFeed(@Body Feed data);


}