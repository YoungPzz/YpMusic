package com.example.ypcloundmusic.component.feed.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.feed.model.Resource;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<Resource, BaseViewHolder> {

    public List<String> imagesUrl = new ArrayList<>();
    public ImageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Resource data) {
        imagesUrl.add(ResourceUtil.resourceUri(data.getUri()));
        ImageView iconView = holder.getView(R.id.icon);
        ImageUtil.show(getContext(), iconView, data.getUri());
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}
