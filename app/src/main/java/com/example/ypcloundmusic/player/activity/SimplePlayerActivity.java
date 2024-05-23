package com.example.ypcloundmusic.player.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.ActivitySimplePlayerBinding;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerListener;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerManager;
import com.example.ypcloundmusic.player.adapter.SimplePlayerAdapter;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.SuperDateUtil;

public class SimplePlayerActivity extends BaseTitleActivity<ActivitySimplePlayerBinding> implements MusicPlayerListener {
    private ActivitySimplePlayerBinding binding;
    private MusicPlayerManager playerManager;

    private int duration;
    private boolean isSeekTracking;
    private SimplePlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimplePlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initViews() {
        super.initViews();
        //系统提供的方法，可以传入自己的toolbar代替actionbar
        setSupportActionBar(binding.toolbar.toolbar);
        /**
         *显示返回按钮
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        playerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
//        String songUrl = "http://course-music-dev.ixuea.com/assets/s1.mp3";
//
//        Song song = new Song();
//        song.setUri(songUrl);
//
//        //播放音乐
//        try {
//            playerManager.play(songUrl, song);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        //使用系统自带的布局
        adapter = new SimplePlayerAdapter(android.R.layout.simple_list_item_1);
        binding.list.setAdapter(adapter);
        adapter.setNewInstance(MusicPlayerService.getListManager(getApplication()).getDatum());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
//        binding.next.setOnClickListener(v -> {
//            NotificationUtil.showAlert(R.string.channel_chat_message);
//        });
        //播放点击
        binding.play.setOnClickListener(v -> {
            playOrPause();
        });
//        拖拽监听器
        binding.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             *
             * @param seekBar
             * @param progress 当前改变后的进度
             * @param fromUser 是否是用户触发的
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    //判断是否来自于用户，如果不加这个，进度条会很卡顿，因为进度条自己动也会触发这个
                    playerManager.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //设置这个变量的目的是，当检测到这个变量为 true 时，进度条不更新
                isSeekTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekTracking = false;
            }
        });

        //循环模式点击
        binding.loopModel.setOnClickListener(v -> {
            //更改循环模式
            MusicPlayerService.getListManager(getApplication()).changeLoopModel();
            //显示循环模式
            showLoopModel();
        });

        //上一曲点击
        binding.previous.setOnClickListener(v -> {
            MusicPlayerService.getListManager(getApplication()).play( MusicPlayerService.getListManager(getApplication()).previous());
        });

         //下一曲点击
        binding.next.setOnClickListener(v -> {
            MusicPlayerService.getListManager(getApplication()).play( MusicPlayerService.getListManager(getApplication()).next());
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            //获取这一首音乐
            Song data = getMusicListManager().getDatum().get(position);
            //播放音乐
            getMusicListManager().play(data);
        });
    }

    private void showLoopModel() {
        int model = MusicPlayerService.getListManager(getApplication()).getLoopModel();
        switch (model){
            case 0:
                binding.loopModel.setText("列表循环");
                break;
            case 1:
                binding.loopModel.setText("单曲循环");
                break;
            case 2:
                binding.loopModel.setText("随机循环");
                break;
        }
    }

    /**
     * 播放或者暂停
     */
    private void playOrPause() {
        if (playerManager.isPlaying()){
            playerManager.pause();
        } else {
            playerManager.resume();
        }
    }

    //当页面可见时执行，与activity的生命周期有关
    @Override
    protected void onResume() {
        super.onResume();

        //显示初始化数据
        ShowInitData();

        //显示音乐时长
        showDuration();

        showProgress();

        showMusicPlayStatus();

        showLoopModel();
        //选中当前播放的音乐
        scrollPosition();
        //设置音乐监听器
        playerManager.addMusicPlayerListener(this);

        showLyricData();
    }

    /**
     * 显示音乐播放状态
     */
    private void showMusicPlayStatus() {
        if(playerManager.isPlaying()){
            showPauseStatus();
        } else {
            showPlayStatus();
        }
    }

    private void ShowInitData() {
        //获取当前播放的音乐
        Song data = MusicPlayerService.getListManager(getApplication()).getData();

        setTitle(data.getTitle());
    }

    /**
     * 选中当前音乐
     */
    private void scrollPosition() {
        //选中当前播放的音乐
        //TODO post？
        //使用post方法是
        //将方法放到了消息循环
        //如果不这样做
        //在onCreate这样的方法中滚动无效
        //因为这时候列表的数据还没有显示完成
        //具体的这部分我们在《详解View》课程中讲解了
        binding.list.post(new Runnable() {
            @Override
            public void run() {
                //获取当前音乐的位置
                int index = getMusicListManager().getDatum().indexOf(getMusicListManager().getData());

                if (index != -1) {
                    //滚动到该位置
                    binding.list.smoothScrollToPosition(index);

                    //选中
                    adapter.setSelectedIndex(index);
                }
            }
        });
    }
    //当界面不可见
    @Override
    protected void onPause() {
        super.onPause();
        //取消播放监听器
        playerManager.removeMusicPlayerListener(this);
    }

    //下面几个函数 让该类实现监听器接口


    @Override
    public void onProgress(Song data) {
        if(isSeekTracking){
            return;
        }
        showProgress();
    }



    @Override
    public void onPaused(Song data) {
        showPlayStatus();
    }


    @Override
    public void onPlaying(Song data) {
        showPauseStatus();
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        //显示音乐时长
        showDuration();
        //设置标题
        setTitle(data.getTitle());

        scrollPosition();
        showLyricData();
    }

    @Override
    public void onLyricReady(Song data) {
        showLyricData();
    }

    private void showDuration() {
        Song data = playerManager.getData();
        //转化为分秒形式
        String time = SuperDateUtil.ms2ms((int) data.getDuration());
        duration = (int) data.getDuration();
        binding.end.setText(time);
        binding.progress.setMax(duration);
    }

    //显示歌词
    private void showLyricData() {
        binding.lyricList.setData(MusicPlayerService.getListManager(getApplication()).getData().getParsedLyric());
    }
    //显示播放状态
    private void showPlayStatus() {
        binding.play.setText(R.string.play);
    }
    //显示暂停状态
    private void showPauseStatus() {
        binding.play.setText(R.string.pause);
    }
    //显示播放进度
    private void showProgress() {
         int progress = (int) playerManager.getData().getProgress();
        //格式化进度
        String time = SuperDateUtil.ms2ms(progress);
        binding.start.setText(time);
        binding.progress.setProgress(progress);

        //显示歌词进度
        binding.lyricList.setProgress(progress);
    }
}