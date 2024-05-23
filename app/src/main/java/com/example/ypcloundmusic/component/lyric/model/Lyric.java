package com.example.ypcloundmusic.component.lyric.model;

import com.example.ypcloundmusic.Model.Base;

import java.util.ArrayList;

/**
 * 歌词解析后的模型
 */
public class Lyric extends Base {

    /**
     * 是否精确到字
     */
    private boolean isAccurate;

    /**
     * 所有的歌词
     */
    private ArrayList<Line> datum;

    public boolean isAccurate() {
        return isAccurate;
    }

    public void setAccurate(boolean accurate) {
        isAccurate = accurate;
    }

    public ArrayList<Line> getDatum() {
        return datum;
    }

    public void setDatum(ArrayList<Line> datum) {
        this.datum = datum;
    }
}
