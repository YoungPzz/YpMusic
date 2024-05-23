package com.example.ypcloundmusic.component.video.adapter;

import android.app.Activity;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.video.model.Video;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;

public class RecommendVideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {


    public RecommendVideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Video data) {
        //封面
        ImageView iconView = holder.getView(R.id.icon);

//        if (data.getHeight() > data.getWidth()) {
//            //竖屏视频
//
//            //封面等比缩放全部显示
//            iconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        } else {
//            //横屏视频
//
//            //封面从中心等比裁剪显示
//            iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        }

        ImageUtil.show((Activity) getContext(), iconView, data.getIcon());


        //标题
        holder.setText(R.id.title, data.getTitle());

        String filledText = String.format(getContext().getResources().getString(R.string.video_info), SuperDateUtil.s2ms((int) data.getDuration()), "yp" );
//        String filledText = String.format(SuperDateUtil.s2ms((int) data.getDuration()),"yp", R.string.video_info);
        //视频时长
        holder.setText(R.id.info,
                filledText);


    }
}
