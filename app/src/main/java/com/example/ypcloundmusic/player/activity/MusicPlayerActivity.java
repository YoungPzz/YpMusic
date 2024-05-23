package com.example.ypcloundmusic.player.activity;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.BlurMaskFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.lyric.view.LyricListView;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.ActivityMusicPlayerBinding;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerListener;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerManager;
import com.example.ypcloundmusic.player.event.RecordClickEvent;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.ResourceUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import org.apache.commons.lang3.StringUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

//黑胶唱片
public class MusicPlayerActivity extends BaseTitleActivity<ActivityMusicPlayerBinding> implements MusicPlayerListener, LyricListView.LyricListListener {
    public ActivityMusicPlayerBinding binding;
    private MusicPlayerManager playerManager;
    private int duration;
    private boolean isSeekTracking;

    //异步事件

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        playerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

        binding.record.initAdapter(MusicPlayerActivity.this);
//        设置数据
        binding.record.setData(getMusicListManager().getDatum());
    }

    @Override
    protected void initViews() {
        super.initViews();

        //沉浸式状态栏，内容显示到状态栏
        QMUIStatusBarHelper.translucent(this);

        //状态栏文字白色
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //黑胶唱片滚动监听
        binding.record.binding.list.registerOnPageChangeCallback(pageChangeCallback);

        //播放按钮点击
        binding.play.setOnClickListener(v -> {
            playOrPause();
        });

        //循环模式点击
        binding.loopModel.setOnClickListener(v -> {
            //更改循环模式
            getMusicListManager().changeLoopModel();

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

        //拖拽监听器
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

        binding.listButton.setOnClickListener(v -> {
            //TODO显示弹出来歌单
        });

        //歌词控件监听器
        binding.lyricList.setLyricListListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    //TODO 监听黑胶唱片点击的异步事件TODO
    public void onRecordClickEvent(RecordClickEvent event){
        //隐藏黑胶唱片
        binding.record.setVisibility(View.GONE);
        binding.lyricList.setVisibility(View.VISIBLE);
    }


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

        showLyricData();

        //设置音乐监听器 因为只在 onResume 里显示第一次数据，后续的数据要根据监听回调回来
        playerManager.addMusicPlayerListener(this);
    }

    //当界面不可见
    @Override
    protected void onPause() {
        super.onPause();
        //取消播放监听器
        playerManager.removeMusicPlayerListener(this);
    }

    /**
     * 歌曲滚动监听器
     */
    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            if (SCROLL_STATE_DRAGGING == state) {
//                拖拽状态
//                停止当前item滚动
                Song currentSong = getMusicListManager().getDatum().get(binding.record.binding.list.getCurrentItem());
                recordRotate(currentSong, false);
            } else if (SCROLL_STATE_IDLE == state) {
                //空闲状态

                //判断黑胶唱片位置对应的音乐是否和现在播放的是一首
                Song song = getMusicListManager().getDatum().get(binding.record.binding.list.getCurrentItem());
                if (getMusicListManager().getData().getId().equals(song.getId())) {
                    //一样
                    //判断播放状态
                    if (playerManager.isPlaying()) {
                        recordRotate(song, true);
                    }
                } else {
                    //不一样

                    //播放当前位置的音乐
                    getMusicListManager().play(song);
                }
            }
        }
    };

    private void recordRotate(Song currentSong, boolean isRotate) {
        currentSong.setRotate(isRotate);
        //控制指针旋转
        binding.record.setPlaying(isRotate);

    }

    private void showMusicPlayStatus() {
        if(playerManager.isPlaying()){
            showPauseStatus();
        } else {
            showPlayStatus();
        }
    }


    private void showProgress() {
        int progress = (int) playerManager.getData().getProgress();
        //格式化进度
        String time = SuperDateUtil.ms2ms(progress);
        binding.start.setText(time);
        binding.progress.setProgress(progress);

        //显示歌词进度
        binding.lyricList.setProgress(progress);
    }

    private void showDuration() {
        Song data = playerManager.getData();
        //转化为分秒形式
        String time = SuperDateUtil.ms2ms((int) data.getDuration());
        duration = (int) data.getDuration();
        binding.end.setText(time);
        binding.progress.setMax(duration);
    }

    private void playOrPause() {
        if (playerManager.isPlaying()){
            playerManager.pause();
        } else {
            playerManager.resume();
        }
    }

    private void showPlayStatus() {
        binding.play.setImageResource(R.drawable.music_play);
        binding.record.setPlaying(false);
    }

    private void showPauseStatus() {
        binding.play.setImageResource(R.drawable.music_pause);
        binding.record.setPlaying(true);

    }
    private void showLoopModel() {
        //获取循环模式，使用不同的图片
        int model = MusicPlayerService.getListManager(getApplication()).getLoopModel();
        switch (model){
            case 0:
                binding.loopModel.setImageResource(R.drawable.music_repeat_list);
                break;
            case 1:
                binding.loopModel.setImageResource(R.drawable.music_repeat_one);
                break;
            case 2:
                binding.loopModel.setImageResource(R.drawable.music_repeat_random);
                break;
        }

    }

    private void ShowInitData() {
        //获取当前播放的音乐
        Song data = MusicPlayerService.getListManager(getApplication()).getData();

        setTitle(data.getTitle());

        setSupportActionBar(binding.toolbar.toolbar);
        /**
         *显示返回按钮
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //显示子标题
        binding.toolbar.toolbar.setSubtitle(data.getSinger().getNickname());

        //显示高斯模糊背景
        showGoshIcon(data);

    }

    public void showGoshIcon(Song data){
        //TODO 了解一下 Glide 配合这个第三方框架实现高斯模糊
        //显示背景
//        ImageUtil.show(MusicPlayerActivity.this, binding.background, data.getIcon());
        //实现背景高斯模糊
        RequestBuilder<Drawable> requestBuilder = Glide.with(this).asDrawable();

        if(StringUtils.isBlank(data.getIcon())){
            //没有封面图使用默认的封面图
            requestBuilder.load(R.drawable.default_cover);
        } else {
            requestBuilder.load(ResourceUtil.resourceUri(data.getIcon()));
        }
        //第三方框架new BlurTransformation(25, 3)
        //radius:模糊半径；值越大越模糊
        //sampling:采样率；值越大越模糊
        RequestOptions options = RequestOptions.bitmapTransform(new BlurTransformation(30, 5));
        requestBuilder.
                apply(options)
                .into(binding.background);
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

        //设置背景
        showGoshIcon(data);

        //选中当前音乐
        scrollPosition();

        //歌词
        showLyricData();
    }

    @Override
    public void onProgress(Song data) {
        if(isSeekTracking){
            return;
        }
        showProgress();
    }

    /**
     * 选中当前音乐
     */
    private void scrollPosition() {
        int index = getMusicListManager().getDatum().indexOf(getMusicListManager().getData());
        binding.record.scrollPosition(index);
    }

    private void showLyricData() {
        binding.lyricList.setData(MusicPlayerService.getListManager(getApplication()).getData().getParsedLyric());
    }

    @Override
    public void onLyricReady(Song data) {
        showLyricData();
    }

    @Override
    public void onLyricClick() {
        //把黑胶唱片显示出来
        binding.record.setVisibility(View.VISIBLE);
        binding.lyricList.setVisibility(View.GONE);
    }
}