package com.example.ypcloundmusic.player.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.song.model.Song;

public class SimplePlayerAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private int selectedIndex;

    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId);
    }

    //使用系统提供的 item，自己就不定义 item 了
    @Override
    protected void convert(@NonNull BaseViewHolder holder, Song song) {
        //获取到文本控件
        TextView tv_title = holder.getView(android.R.id.text1);

        //显示标题
        tv_title.setText(song.getTitle());

        if (selectedIndex == holder.getAbsoluteAdapterPosition()) {
            tv_title.setTextColor(getContext().getColor(R.color.primary));
        } else {
            tv_title.setTextColor(getContext().getColor(R.color.black32));
        }
    }

    /**
     * 选中音乐
     *
     * @param selectedIndex
     */
    public void setSelectedIndex(int selectedIndex) {
        if (this.selectedIndex != -1) {
            //先刷新上一行
            notifyItemChanged(this.selectedIndex);
        }

        //保存选中索引
        this.selectedIndex = selectedIndex;

        if (this.selectedIndex != -1) {
            //刷新当前行
            notifyItemChanged(this.selectedIndex);
        }

    }
}
