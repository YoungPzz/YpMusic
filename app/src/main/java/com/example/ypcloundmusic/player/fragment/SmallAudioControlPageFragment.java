package com.example.ypcloundmusic.player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ypcloundmusic.Fragment.BaseFragment;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.FragmentSmallAudioControlPageBinding;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerListener;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.ImageUtil;

import java.util.List;

//TODO 感觉这里可以用监听器监听数据
public class SmallAudioControlPageFragment extends BaseFragment{
    private FragmentSmallAudioControlPageBinding binding;

    private List<Song> datum;
    private Song data;
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSmallAudioControlPageBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initDatum() {
        super.initDatum();
       showSmallPlayerStatus();
    }

    public static SmallAudioControlPageFragment newInstance(Song data) {

        Bundle args = new Bundle();

        SmallAudioControlPageFragment fragment = new SmallAudioControlPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        showSmallPlayerStatus();
    }

    private void showSmallPlayerStatus(){
        datum = MusicPlayerService.getListManager(getContext()).getDatum();
        if(datum != null && datum.size() > 0){
//            data = datum.get(0);
            data = MusicPlayerService.getMusicPlayerManager(getContext()).getData();
            //显示封面
            ImageUtil.show(getActivity(), binding.icon, data.getIcon());

            //显示标题
            binding.text.setText(data.getTitle());
            binding.play.setClickable(true);

        } else {
            binding.text.setText("暂时无音乐播放");
            binding.play.setClickable(false);
        }
        if(MusicPlayerService.getMusicPlayerManager(getContext()).isPlaying()){
            binding.play.setSelected(true);
        }else{
            binding.play.setSelected(false);
        }
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MusicPlayerService.getMusicPlayerManager(getContext()).isPlaying()){
                    MusicPlayerService.getMusicPlayerManager(getContext()).pause();
                    binding.play.setSelected(false);
                }else{
                    MusicPlayerService.getMusicPlayerManager(getContext()).resume();
                    binding.play.setSelected(true);
                }
            }
        });
    }
}
