package com.example.ypcloundmusic.player.activity.manager;

import android.content.Context;

import com.example.ypcloundmusic.component.song.model.Song;

import java.util.List;

/**
 * 列表管理器
 * 主要是封装了列表相关的操作
 * 例如：上一曲、下一曲循环模式
 */
public interface MusicListManager {

    /**
     * 设置播放列表
     */
    void setDatum(List<Song> datum);

    /**
     * 获取播放列表
     */
    List<Song> getDatum();

    /**
     * 播放
     */
    void play(Song data);

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    Song getData();

    /**
     * 获取循环模式
     * @return
     */
    int getLoopModel();

    /**
     * 更改循环模式
     */
    int changeLoopModel();

    //下一曲
    Song next();
    //上一曲
    Song previous();
}
