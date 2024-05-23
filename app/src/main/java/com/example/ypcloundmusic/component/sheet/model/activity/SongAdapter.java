package com.example.ypcloundmusic.component.sheet.model.activity;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.song.model.Song;

/**
 * 歌曲详细，歌曲适配器
 */
public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据的方法
     * @param holder
     * @param data
     */
    @Override
    protected void convert(@NonNull BaseViewHolder holder, Song data) {
        //显示索引
        holder.setText(R.id.index, String.valueOf(holder.getLayoutPosition()));

        //显示标题
        holder.setText(R.id.title, data.getTitle());

        //显示信息
        holder.setText(R.id.info, data.getSinger().getNickname());


    }
}
