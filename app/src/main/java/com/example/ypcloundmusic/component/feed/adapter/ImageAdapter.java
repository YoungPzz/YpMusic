package com.example.ypcloundmusic.component.feed.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.feed.model.Resource;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.ResourceUtil;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public List<String> imagesUrl = new ArrayList<>();
    public ImageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Object data) {
        ImageView iconView = holder.getView(R.id.icon);
        holder.setGone(R.id.close, true);
        if(data instanceof Resource){
            //动态界面显示动态的图片
            Resource d = (Resource) data;
            imagesUrl.add(ResourceUtil.resourceUri(d.getUri()));
            ImageUtil.show(getContext(), iconView, d.getUri());
        } else if (data instanceof LocalMedia) {
            //这个 LocalMedia 是第三方框架里提供的类
            LocalMedia d = (LocalMedia) data;
            //选择的图片
            ImageUtil.showLocalImage(getContext(), iconView, d.getCompressPath());

            //显示删除按钮
            holder.setGone(R.id.close, false);
        } else {
            //添加图片时，最后一个空格
            iconView.setImageResource((int) data);
        }
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}
