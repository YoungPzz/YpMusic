package com.example.ypcloundmusic.component.main.tab.discovery;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ypcloundmusic.component.ad.model.Ad;
import com.example.ypcloundmusic.util.Constant;

import java.util.List;

public class BannerData implements BaseMultiItemEntity{
    private List<Ad> data;
    public BannerData(List<Ad> data) {
        this.data = data;
    }

    public List<Ad> getData() {
        return data;
    }

    public void setData(List<Ad> data) {
        this.data = data;
    }

    @Override
    public int getItemType() {
        return Constant.STYLE_BANNER;
    }
}
