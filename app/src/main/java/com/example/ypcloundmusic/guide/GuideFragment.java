package com.example.ypcloundmusic.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ypcloundmusic.Fragment.BaseCommonFragment;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.databinding.FramentGuideBinding;
import com.example.ypcloundmusic.util.Constant;

public class GuideFragment extends BaseCommonFragment {

    private com.example.ypcloundmusic.databinding.FramentGuideBinding binding;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FramentGuideBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        int data = getArguments().getInt("ID");
        binding.icon.setImageResource(data);
    }

    public static GuideFragment newInstance(Integer data) {
        
        Bundle args = new Bundle();
        args.putInt("ID", data);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
