package com.example.ypcloundmusic.component.lyric.model;

import com.example.ypcloundmusic.Model.Base;

/**
 * 一行歌词
 */
/**
 * 一行歌词
 */
public class Line extends Base {
    /**
     * 整行歌词
     */
    private String data;

    /**
     * 开始时间
     * 单位毫秒
     */
    private long startTime;

    //如果是逐行 只有上面两个
    /**
     * 每一个字
     */
    private String[] words;

    /**
     * 每一个字对应的时间
     */
    private Integer[] wordDurations;

    /**
     * 结束时间
     */
    private long endTime;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public Integer[] getWordDurations() {
        return wordDurations;
    }

    public void setWordDurations(Integer[] wordDurations) {
        this.wordDurations = wordDurations;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
