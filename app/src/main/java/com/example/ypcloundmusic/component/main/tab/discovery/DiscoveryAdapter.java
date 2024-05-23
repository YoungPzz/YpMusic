package com.example.ypcloundmusic.component.main.tab.discovery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.ad.model.Ad;
import com.example.ypcloundmusic.databinding.DiscoveryButtonBinding;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.DensityUtil;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.ScreenUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import superui.text.GridDividerItemDecoration;

public class DiscoveryAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder> {
    private Fragment fragment;

    private OnBannerListener onBannerListener;
    private com.example.ypcloundmusic.databinding.DiscoveryButtonBinding binding;

    public DiscoveryAdapter(Fragment fragment, OnBannerListener onBannerListener) {
        super(new ArrayList<>());
        this.fragment = fragment;
        this.onBannerListener = onBannerListener;
        //添加多类型布局

        //banner类型
        addItemType(Constant.STYLE_BANNER, R.layout.item_discovery_banner);

        //快捷按钮
        addItemType(Constant.STYLE_BUTTON, R.layout.item_discovery_button);

        //歌单
        addItemType(Constant.STYLE_SHEET, R.layout.item_discovery_sheet);

        //单曲
        addItemType(Constant.STYLE_SONG, R.layout.item_discovery_sheet);
    }

    //在这个地方显示数据
    @Override
    protected void convert(@NonNull BaseViewHolder holder, BaseMultiItemEntity baseMultiItemEntity) {
        switch (holder.getItemViewType()) {
            case Constant.STYLE_BANNER:
                //banner
                BannerData data = (BannerData) baseMultiItemEntity;

                Banner bannerView = holder.getView(R.id.banner);

                BannerImageAdapter<Ad> bannerImageAdapter = new BannerImageAdapter<Ad>(data.getData()) {

                    @Override
                    public void onBindView(BannerImageHolder holder, Ad data, int position, int size) {
                        //把网络上请求到的图片数据显示在控件上
                        //比较复杂 要用到第三方框架
                        ImageUtil.show(getContext(), (ImageView) holder.itemView, data.getIcon());
                    }
                };

                bannerView.setAdapter(bannerImageAdapter);
                bannerView.setOnBannerListener(onBannerListener);
//                bannerView.setBannerRound(DensityUtil.dip2px(getContext(), 10));

                //添加生命周期观察者
                bannerView.addBannerLifecycleObserver(fragment);

                bannerView.setIndicator(new CircleIndicator(getContext()));
                break;

            case Constant.STYLE_BUTTON:
                bindButtonData(holder, (ButtonData) baseMultiItemEntity);
                break;

            case Constant.STYLE_SHEET:
                bindSheetData(holder, (SheetData) baseMultiItemEntity);
                break;

            case Constant.STYLE_SONG:
                bindSongData(holder, (SongData) baseMultiItemEntity);
                break;
        }
    }

    private void bindSongData(BaseViewHolder holder, SongData data) {
        SongAdapter songAdapter = new SongAdapter(R.layout.item_discovery_song);

        //设置标题
        holder.setText(R.id.title, R.string.recommend_song);
        //获取list
        RecyclerView recyclerView = holder.getView(R.id.rvlist);
        if(recyclerView.getAdapter() == null){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(songAdapter);

        }
        songAdapter.setNewInstance(data.getData());
    }


    private void bindSheetData(BaseViewHolder holder, SheetData data) {
        SheetAdapter sheetAdapter = new SheetAdapter(R.layout.item_sheet);
        //设置标题
        holder.setText(R.id.title, R.string.recommend_sheet);

        //获取list
        RecyclerView recyclerView = holder.getView(R.id.rvlist);

        if (recyclerView.getAdapter() == null) {
            //显示成3列
            GridLayoutManager gm = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(gm);

            //设置适配器
            recyclerView.setAdapter(sheetAdapter);
            //别人开源的一个分割线
            GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(getContext(), (int) DensityUtil.dip2px(getContext(), 5F));
            recyclerView.addItemDecoration(itemDecoration);
        }
        sheetAdapter.setNewInstance(data.getData());

    }

    private void bindButtonData(BaseViewHolder holder, ButtonData data) {
        //先找到控件 目的是给他添加内容
        LinearLayout container = holder.getView(R.id.container);
        if (container.getChildCount() > 0) {
            //因为每次把该部分滑动到视图内，都会执行convert函数，所以这里做一层判断，有内容就不添加了
            return;
        }

        //让界面显示五个半按钮
        //获取屏幕宽度 因为有个padding需要减去
        float containerWidth = ScreenUtil.getScreenWith(container.getContext()) - 20;

        //计算单个宽度
        int itemWidth = (int) (containerWidth / 5.5);
        //每次循环创建一个控件
        for (IconTitleButtonData it : data.getData()) {
            binding = DiscoveryButtonBinding.inflate(LayoutInflater.from(container.getContext()));
            binding.icon.setImageResource(it.getIcon());
            binding.title.setText(it.getTitle());
            if (it.getIcon() == R.drawable.day_recommend) {
                binding.more.setText(String.valueOf(SuperDateUtil.currentDay()));
            }
            //设置点击事件
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(binding.getRoot(), layoutParams);
        }

    }
}
