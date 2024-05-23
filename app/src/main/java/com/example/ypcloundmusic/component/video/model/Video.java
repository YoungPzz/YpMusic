package com.example.ypcloundmusic.component.video.model;

import com.example.ypcloundmusic.component.song.model.User;

public class Video {
    private String id;

    private String createdAt;

    private String updatedAt;


    /**
     * 标题
     */
    private String title;

    /**
     * 视频地址
     * 和图片地址一样
     * 都是相对地址
     */
    private String uri;

    /**
     * 封面地址
     */
    private String icon;

    /**
     * 视频时长
     * 单位：秒
     */
    private long duration;

    /**
     * 视频宽
     */
    private int width;

    /**
     * 视频高
     */
    private int height;

    /**
     * 点击数
     */
    private long clicksCount;

    /**
     * 点击数
     */
    private long likesCount;

    /**
     * 评论数
     */
    private long commentsCount;

    /**
     * 谁发布了这个视频
     */
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getClicksCount() {
        return clicksCount;
    }

    public void setClicksCount(long clicksCount) {
        this.clicksCount = clicksCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
