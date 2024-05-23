package com.example.ypcloundmusic.component.main.tab.fragments;

import static autodispose2.AutoDispose.autoDisposable;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ypcloundmusic.Fragment.BaseFragment;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.video.activity.VideoDetailActivity;
import com.example.ypcloundmusic.component.video.adapter.VideoAdapter;
import com.example.ypcloundmusic.component.video.model.Video;
import com.example.ypcloundmusic.databinding.FragmentVideoBinding;
import com.example.ypcloundmusic.util.Constant;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class VideoFragment extends BaseFragment {
    private VideoAdapter adapter;

    private FragmentVideoBinding binding;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return binding.getRoot();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
    }

    public static VideoFragment newInstance() {
        
        Bundle args = new Bundle();
        
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        adapter = new VideoAdapter(R.layout.item_video);
        binding.list.setAdapter(adapter);

        loadData(false);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Video data = (Video) adapter.getItem(position);
                Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(Constant.ID, data.getId());
                startActivity(intent);
//                startActivityExtraId(VideoDetailActivity.class, data.getId());
            }
        });
    }

    @Override
    protected void loadData(boolean isPlaceholder) {
        DefaultRepository.getInstance()
                .videos(1)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<ListResponse<Video>>() {
                    @Override
                    public void onSucceeded(ListResponse<Video> data) {
                        adapter.setNewInstance(data.getData().getData());
                    }
                });
    }

}
