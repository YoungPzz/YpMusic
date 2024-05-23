package com.example.ypcloundmusic.component.sheet.model;

import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.component.song.model.User;

import java.util.ArrayList;

/**
 * 歌单模型
 */
public class Sheet {
    private String id;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String icon;
    private String detail;
    private String userId;
    private int clicksCount;

    private User user;
    private String collectId;

    /**
     * 是否收藏
     * @return
     */
    public boolean isCollect() {
        //服务端是这样设置，如果该字段有值，则表示用户已经收藏
        return collectId != null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 歌曲
     * @return
     */
    private ArrayList<Song> songs;

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getClicksCount() {
        return clicksCount;
    }

    public void setClicksCount(int clicksCount) {
        this.clicksCount = clicksCount;
    }

    public int getCollectsCount() {
        return collectsCount;
    }

    public void setCollectsCount(int collectsCount) {
        this.collectsCount = collectsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    private int collectsCount;
    private int commentsCount;
    private int songsCount;
}
