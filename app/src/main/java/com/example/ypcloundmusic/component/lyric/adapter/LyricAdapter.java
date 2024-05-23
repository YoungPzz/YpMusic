package com.example.ypcloundmusic.component.lyric.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.lyric.model.Line;

/**
 * 播放界面歌词适配器
 */
public class LyricAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private int selectedIndex;

    public LyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Object line) {
        if(line instanceof String) {
            //占位数据
            holder.setText(R.id.content, "");
        }else {
            Line d = (Line) line;
            holder.setText(R.id.content, d.getData());
        }
        if(selectedIndex == holder.getBindingAdapterPosition()){
            holder.setTextColor(R.id.content, getContext().getColor(R.color.primary));
        }else {
            holder.setTextColor(R.id.content, getContext().getColor(R.color.white));
        }
    }

    public void setSelectedIndex(int selectedIndex) {
        //这个函数的意义：比如原来的selectedIndex是 4，会让第 4 行歌词刷新一下，调用第四行的 convert 的方法起到
        //没选中的效果
        notifyItemChanged(this.selectedIndex);
        this.selectedIndex = selectedIndex;
        notifyItemChanged(this.selectedIndex);

    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
