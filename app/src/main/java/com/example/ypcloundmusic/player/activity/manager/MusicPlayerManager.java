package com.example.ypcloundmusic.player.activity.manager;


import android.media.MediaPlayer;

import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.player.activity.MusicPlayerActivity;
import com.example.ypcloundmusic.player.fragment.RecordFragment;

import java.io.IOException;

/**
 * 音乐播放器对外暴露的接口
 */
public interface MusicPlayerManager {


    public void addMusicPlayerListener(MusicPlayerListener listener);

    public void removeMusicPlayerListener(MusicPlayerListener listener);

    /**
     * 播放
     *
     * @param uri  播放音乐的绝对地址
     * @param data 音乐对象
     */
    void play(String uri, Song data) throws IOException;

    /**
     * 是否播放了
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    Song getData();

    void seekTo(int progress);

    void onCompletion(MediaPlayer mp);

    //设置单曲循环
    void setLooping(boolean b);

}
