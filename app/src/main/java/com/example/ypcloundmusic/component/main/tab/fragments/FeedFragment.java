package com.example.ypcloundmusic.component.main.tab.fragments;

import static autodispose2.AutoDispose.autoDisposable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.ypcloundmusic.Fragment.BaseFragment;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.feed.activity.PublishFeedActivity;
import com.example.ypcloundmusic.component.feed.adapter.FeedAdapter;
import com.example.ypcloundmusic.component.feed.model.Feed;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.databinding.FragmentFeedBinding;
import com.example.ypcloundmusic.util.PreferenceUtil;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class FeedFragment extends BaseFragment {

    private PreferenceUtil sp;

    private FragmentFeedBinding binding;
    private String userId;

    private FeedAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFeedBinding.inflate(getLayoutInflater());
        sp = PreferenceUtil.getInstance(getContext());
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return binding.getRoot();
        
    }

    public static FeedFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        userId = sp.getUserId();
        if (userId != null) {
            loadData(false);
        }

        adapter = new FeedAdapter(R.layout.item_feed, this);
        binding.list.setAdapter(adapter);

    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PublishFeedActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void loadData(boolean isPlaceholder) {
        DefaultRepository.getInstance()
                .feeds(userId)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<ListResponse<Feed>>() {
                    @Override
                    public void onSucceeded(ListResponse<Feed> data) {
                        adapter.setNewInstance(data.getData().getData());
//                        adapter.setDatum(data.getData().getData());
                        Log.d("sb666","s");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(false);
    }

//    //监听器 FeedListener
//    @Override
//    public void publish() {
//        Log.d(" TADDA","已经发布完成啦");
//    }

}
