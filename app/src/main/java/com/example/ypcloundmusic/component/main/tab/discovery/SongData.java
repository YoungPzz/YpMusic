package com.example.ypcloundmusic.component.main.tab.discovery;

import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.util.Constant;

import java.util.List;

public class SongData implements BaseMultiItemEntity{

    private List<Song> data;

    public SongData(List<Song> data) {
        this.data = data;
    }

    public void setData(List<Song> data) {
        this.data = data;
    }

    public List<Song> getData() {
        return data;
    }

    @Override
    public int getItemType() {
        return Constant.STYLE_SONG;
    }
}
