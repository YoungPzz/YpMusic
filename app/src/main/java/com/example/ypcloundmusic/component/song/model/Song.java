package com.example.ypcloundmusic.component.song.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.ypcloundmusic.component.lyric.model.Lyric;

import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

public class Song extends LitePalSupport {
    private String id;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String icon;
    private String uri;
    private int clicksCount;
    private int commentsCount;
    private User user;
    private String userId;
    private Singer singer;
    private String singerId;
    private long duration;
    private long progress;
    private boolean rotate;

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    private int style;
    /**
     * 歌词内容
     */
    private String lyric;

    /**
     * 已经解析后的歌词
     */
    private Lyric parsedLyric;

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Lyric getParsedLyric() {
        return parsedLyric;
    }

    public void setParsedLyric(Lyric parsedLyric) {
        this.parsedLyric = parsedLyric;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getClicksCount() {
        return clicksCount;
    }

    public void setClicksCount(int clicksCount) {
        this.clicksCount = clicksCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getProgress() {
        return progress;
    }


    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }
}
