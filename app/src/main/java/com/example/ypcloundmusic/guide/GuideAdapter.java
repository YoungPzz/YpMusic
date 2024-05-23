package com.example.ypcloundmusic.guide;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ypcloundmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导界面适配器
 */
public class GuideAdapter extends FragmentStatePagerAdapter {
    private List<Integer> datum = new ArrayList<>();

    public GuideAdapter(@NonNull FragmentManager fm) {
        super(fm);
        datum.add(R.drawable.guide1);
        datum.add(R.drawable.guide2);
        datum.add(R.drawable.guide3);
        datum.add(R.drawable.guide4);
        datum.add(R.drawable.guide5);

    }

    /**
     * 每个位置显示什么内容呢？这个函数返回的fragment决定
     * 返回当前位置Fragment
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Integer data = datum.get(position);
        return GuideFragment.newInstance(data);
    }

    /**
     * 有多少个
     * @return
     */
    @Override
    public int getCount() {
        return datum.size();
    }
}
