package com.example.ypcloundmusic.component.main.tab.discovery;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.player.activity.SimplePlayerActivity;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.ImageUtil;

public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Song data) {
        ImageUtil.show(getContext(), holder.getView(R.id.icon), data.getIcon());
        holder.setText(R.id.title, data.getTitle());
        holder.setText(R.id.more, data.getSinger().getNickname());
        holder.getView(R.id.songClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAF", data.getTitle());
                Intent intent = new Intent(v.getContext(), SimplePlayerActivity.class);
                v.getContext().startActivity(intent);
                MusicPlayerService.getListManager(getContext()).play(data);

            }
        });
    }
}
