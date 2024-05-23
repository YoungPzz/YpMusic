package com.example.ypcloundmusic.player.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ypcloundmusic.adapter.BaseFragmentStateAdapter;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.player.fragment.RecordFragment;

/**
 * 黑胶唱片 adapter
 */
public class MusicPlayerRecordAdapter extends BaseFragmentStateAdapter<Song> {

    public MusicPlayerRecordAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return RecordFragment.newInstance(getData(position));
    }
}
