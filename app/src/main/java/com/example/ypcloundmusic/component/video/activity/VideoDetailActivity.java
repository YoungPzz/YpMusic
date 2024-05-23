package com.example.ypcloundmusic.component.video.activity;

import static autodispose2.AutoDispose.autoDisposable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.video.adapter.RecommendVideoAdapter;
import com.example.ypcloundmusic.component.video.model.Video;
import com.example.ypcloundmusic.databinding.ActivityVideoDetailBinding;
import com.example.ypcloundmusic.databinding.HeaderVideoDetailBinding;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.ResourceUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;
import com.example.ypcloundmusic.util.SuperStatusBarUtil;
import com.google.android.material.chip.Chip;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.util.ArrayList;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 视频详情
 */
public class VideoDetailActivity extends BaseTitleActivity<ActivityVideoDetailBinding> {

    private ActivityVideoDetailBinding binding;
    private String id;
    private Video data;
    private long seek; //记录播放进度
    private OrientationUtils orientationUtils ;
    private boolean isPlay;
    private boolean isPause;
    private RecommendVideoAdapter adapter;
    private com.example.ypcloundmusic.databinding.HeaderVideoDetailBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initViews() {
        super.initViews();
        //系统提供的方法，可以传入自己的toolbar代替actionbar
//        setSupportActionBar(binding.toolbar.toolbar);
        /**
         *显示返回按钮
         */
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        //状态栏黑色
        SuperStatusBarUtil.setStatusBarColor(getWindow(), Color.BLACK);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        id = getIntent().getStringExtra(Constant.ID);
        loadData(false);

        //相关推荐视频适配器
        adapter = new RecommendVideoAdapter(R.layout.item_video_list);

        binding.list.setAdapter(adapter);

        headerBinding = HeaderVideoDetailBinding.inflate(getLayoutInflater());
        adapter.addHeaderView(headerBinding.getRoot());


    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                binding.player.startWindowFullscreen(VideoDetailActivity.this, true, true);
            }
        });
    }

    @Override
    protected void loadData(boolean isPlaceholder) {
        super.loadData(isPlaceholder);
        DefaultRepository.getInstance()
                .videoDetail(id)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<DetailResponse<Video>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Video> data) {
                        preparePlay(data.getData());
//                       推荐列表
                        DefaultRepository.getInstance()
                                .videos(1)
                                .to(autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                                .subscribe(new HttpObserver<ListResponse<Video>>() {
                                    @Override
                                    public void onSucceeded(ListResponse<Video> data) {
                                        adapter.setNewInstance(data.getData().getData());
                                    }
                                });
                    }
                });
    }

    private void preparePlay(Video data) {
        //准备播放
        play(data);
    }

    private void play(Video data) {
        this.data = data;
        //构建者设计模式 以下构建官方文档有
        GSYVideoOptionBuilder videoOption = new GSYVideoOptionBuilder();
        orientationUtils = new OrientationUtils(this, binding.player);
        videoOption
                //.setThumbImageView(imageView)
                //小屏时不触摸滑动
                .setIsTouchWiget(false)
                //音频焦点冲突时是否释放
                .setReleaseWhenLossAudio(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setSeekOnStart(seek)
                .setNeedLockFull(true)
                .setUrl(ResourceUtil.resourceUri(data.getUri()))
                .setCacheWithPlay(false)

                //全屏切换时不使用动画
                .setShowFullAnimation(false)
                .setVideoTitle(data.getTitle())

                //设置右下角 显示切换到全屏 的按键资源  默认的图标不好看
                .setEnlargeImageRes(R.drawable.full_screen)

                //设置右下角 显示退出全屏 的按键资源
                .setShrinkImageRes(R.drawable.normal_screen)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                }).setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                }).build(binding.player);

        //开始播放
        binding.player.startPlayLogic();

        //标题
//        setTitle(data.getTitle());
        //标题
//        setTitle(data.getTitle());
        headerBinding.title.setText(data.getTitle());

        //发布时间
//        String createdAt = SuperDateUtil.yyyyMMdd(data.getCreatedAt());
        headerBinding.createAt.setText(getResources()
                .getString(R.string.video_created_at, "2024-05-20"));

        //播放次数
        String clicksCount = getResources()
                .getString(R.string.video_clicks_count, data.getClicksCount());
        headerBinding.count.setText(clicksCount);

        //头像
        ImageUtil.showAvatar(VideoDetailActivity.this, headerBinding.icon, data.getUser().getIcon());

        //昵称
        headerBinding.nickname.setText(data.getUser().getNickname());


//        显示视频标签
//        由于服务端没有实现视频标签功能
//        所以这里就写死几个标签
//        目的是讲解如果使用流式标签布局
        ArrayList<String> tags = new ArrayList<>();
        tags.add("抒情");
        tags.add("浪漫");
        tags.add("开心");
        tags.add("快乐时刻");
        tags.add("奔跑吧");
        tags.add("少年");

        headerBinding.tag.removeAllViews();

        Chip chip;
        for (String tag : tags) {
            chip = new Chip(VideoDetailActivity.this);
            chip.setText(tag);

            headerBinding.tag.addView(chip);
        }
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        binding.player.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        binding.player.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            binding.player.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            binding.player.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }
}