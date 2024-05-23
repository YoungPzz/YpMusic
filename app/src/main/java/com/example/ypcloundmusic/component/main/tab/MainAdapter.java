package com.example.ypcloundmusic.component.main.tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ypcloundmusic.component.main.tab.fragments.DiscoveryFragment;
import com.example.ypcloundmusic.component.main.tab.fragments.FeedFragment;
import com.example.ypcloundmusic.component.main.tab.fragments.MeFragment;
import com.example.ypcloundmusic.component.main.tab.fragments.RoomFragment;
import com.example.ypcloundmusic.component.main.tab.fragments.VideoFragment;

public class MainAdapter extends FragmentStatePagerAdapter {
    public MainAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return VideoFragment.newInstance();
            case 2:
                return MeFragment.newInstance();
            case 3:
                return FeedFragment.newInstance();
            case 4:
                return RoomFragment.newInstance();
            default:
                return DiscoveryFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
