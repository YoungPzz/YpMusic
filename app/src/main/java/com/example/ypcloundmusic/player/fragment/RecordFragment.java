package com.example.ypcloundmusic.player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ypcloundmusic.Fragment.BaseFragment;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.FragmentRecordBinding;
import com.example.ypcloundmusic.player.activity.impl.MusicPlayerManagerImpl;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerListener;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerManager;
import com.example.ypcloundmusic.player.event.RecordClickEvent;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 音乐黑胶唱片界面
 */
public class RecordFragment extends BaseFragment implements MusicPlayerListener {
    private FragmentRecordBinding binding;
    private Song data;
    private MusicPlayerManager musicPlayerManager;
    private float recordRotation;

    /**
     * 每16毫秒旋转的角度
     * 16毫秒是通过
     * 每秒60帧计算出来的
     * 也就是1000/60=16
     * 也就是说绘制一帧要在16毫秒中完成
     * 不然就能感觉卡顿
     * 用秒表测转一圈的时间
     */
    private static final float ROTATION_PER = 0.2304F;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecordBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public static RecordFragment newInstance(Song data) {

        Bundle args = new Bundle();
        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);
        fragment.data = data;
        return fragment;
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //显示封面
        ImageUtil.show(getActivity(), binding.icon, data.getIcon());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.container.setOnClickListener(v -> {
            //因为现在是在 fragment 里面，歌词控件是在 Activity 里
            //所以得发布一个异步事件，在那个界面监听
            //TODO 异步事件
            EventBus.getDefault().post(new RecordClickEvent());
        });
    }

    /**
     * 界面可见了
     */
    @Override
    public void onResume() {
        super.onResume();
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getActivity());
        //设置播放监听器
        musicPlayerManager.addMusicPlayerListener(this);
    }

    /**
     * 界面不可见了
     */
    @Override
    public void onPause() {
        super.onPause();
        //取消播放监听器
        musicPlayerManager.addMusicPlayerListener(this);
    }


    @Override
    public void onProgress(Song data) {
        if (!data.isRotate()) {
            return;
        }
        if(recordRotation >= 360) {
            recordRotation = 0;
        }

        //每次旋转的角度
        recordRotation += ROTATION_PER;

        //旋转
        binding.icon.setRotation(recordRotation);
    }

}
