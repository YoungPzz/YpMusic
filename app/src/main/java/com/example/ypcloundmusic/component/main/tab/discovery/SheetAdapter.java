package com.example.ypcloundmusic.component.main.tab.discovery;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.MainActivity;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.main.tab.fragments.DiscoveryFragment;
import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.component.sheet.model.activity.SheetDetailActivity;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.ImageUtil;

import java.util.List;

/**
 * 发现界面歌单适配器
 */
public class SheetAdapter extends BaseQuickAdapter<Sheet, BaseViewHolder> {
    //使用这个第三方的适配器 前面实现多类型使用的是BaseMultiItemQuickAdapter,单类型使用BaseQuickAdapter

    public SheetAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Sheet data) {
        ImageUtil.show(getContext(), holder.getView(R.id.icon), data.getIcon());
        holder.setText(R.id.textHere, data.getTitle());
        holder.setText(R.id.more_sheet, String.valueOf(data.getClicksCount()));
        holder.getView(R.id.icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() ,SheetDetailActivity.class);
                intent.putExtra(Constant.ID, data.getId());
                v.getContext().startActivity(intent);
            }
        });

    }


}
