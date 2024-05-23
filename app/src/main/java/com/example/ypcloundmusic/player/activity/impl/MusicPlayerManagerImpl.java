package com.example.ypcloundmusic.player.activity.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.lyric.parser.LyricParser;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerListener;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerManager;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl instance;
    private final Context context;
    private final MediaPlayer player;

    private String uri;

    private Song data;

    /**
     * 播放器监听器
     * TODO 这个类的作用是可以并发，就是在遍历的时候同时删除不会报错
     */
    private CopyOnWriteArrayList<MusicPlayerListener> listeners = new CopyOnWriteArrayList<>();
    private TimerTask timeTask;

    private final static int MESSAGE_PROGRESS = 0;
    /**
     * TODO 这里使用了 handler
     * 用来将事件转到主线程
     */
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PROGRESS:
                //播放进度回调
                    //将进度设置到音乐对象
                    data.setProgress(player.getCurrentPosition());
                    //回调监听
                    for (MusicPlayerListener listener : listeners) {
                        listener.onProgress(data);
                    }
                    break;
            }
        }
    };
    private Timer timer;
    private boolean isPrepare;

    //构造方法私有化，规定只能通过getInstance创建,外界不能通过new来创建对象
    private MusicPlayerManagerImpl(Context context) {
        //保存context 因为后面可能会用到
        this.context = context;

        //初始化播放器
        player = new MediaPlayer();

        //设置播放器的监听器，可以监听音乐的时长、播放到哪里了
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //播放时长
                data.setDuration(mp.getDuration());
                //准备完成之后，向监听器发送事件，说我已经准备完毕啦，可以把音乐时长填上啦
                publishPrepareStatus();
            }
        });
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public synchronized static MusicPlayerManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicPlayerManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            startPublishProgress();
        }
    }

    @Override
    public void removeMusicPlayerListener(MusicPlayerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void play(String uri, Song data) throws IOException {
        //保存信息
        this.uri = uri;
        this.data = data;

        //释放播放器？
        player.reset();

        playNow();
    }

    /**
     * TODO 为什么需要解析为URI对象
     * @throws IOException
     */
    private void playNow() {
        isPrepare = true;
        try {
            if (uri.startsWith("content://")) {
                //内容提供者格式
                //播放本地音乐
                //uri示例:content://media/external/ad
                //需要解析为Uri对象
                player.setDataSource(context, Uri.parse(uri));
            } else {
                //设置数据源
                player.setDataSource(uri);
            }
            //同步准备
            //真是项目可能会使用异步 因为如果网络不好 同步可能会卡住
            //用同步可住ANR了
            player.prepare();
//           player.prepareAsync();
            player.start();
            //回调监听器
            publishPlayingStatus();
            startPublishProgress();

            //歌词处理
            if(data.getParsedLyric() != null){
                //通知歌词改变了
                onLyricReady();
            } else if (StringUtils.isNotBlank(data.getLyric())){
                data.setParsedLyric(LyricParser.parse(data.getStyle(), data.getLyric()));
                //通知歌词改变了
                onLyricReady();
            } else {
                DefaultRepository.getInstance()
                        .songDetail(data.getId())
                        .subscribe(new HttpObserver<DetailResponse<Song>>() {
                            @Override
                            public void onSucceeded(DetailResponse<Song> songDetailResponse) {
                                //请求成功
                                if (songDetailResponse != null && songDetailResponse.getData() != null) {
                                    //数据设置歌曲对象
                                    data.setStyle(songDetailResponse.getData().getStyle());
                                    data.setLyric(songDetailResponse.getData().getLyric());
                                }
                                if (StringUtils.isNotBlank(data.getLyric())){
                                    data.setParsedLyric(LyricParser.parse(data.getStyle(), data.getLyric()));
                                }
                                //通知歌词改变了
                                onLyricReady();
                            }
                        });
            }
        } catch (IOException e){
            //TODO 播放错误处理
        }

    }




    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        if(isPlaying()){
            //如果在播放才暂停
            player.pause();
            publishPauseStatus();
            stopPublishProgress();

        }
    }

    @Override
    public void resume() {
        if(!isPlaying()){
            resumeNow();
        }
    }

    @Override
    public Song getData() {
        return data;
    }

    @Override
    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    private void resumeNow() {
        player.start();
        //回调监听器
        publishPlayingStatus();

        //点击继续播放之后，进度条继续播放,发布一个进度条走的
        startPublishProgress();

    }

    //发布回调
    /**
     * 发布播放中状态
     */
    private void publishPlayingStatus() {
        for (MusicPlayerListener listener : listeners) {
            listener.onPlaying(data);
        }
    }

    private void publishPauseStatus() {
        for (MusicPlayerListener listener : listeners) {
            listener.onPaused(data);
        }
        //用工具类的方法没有写124
    }

    private void publishPrepareStatus() {
        for (MusicPlayerListener listener : listeners) {
            listener.onPrepared(player, data);
        }
    }

    private void onLyricReady() {
        for (MusicPlayerListener listener : listeners) {
            listener.onLyricReady(data);
        }
    }
    /**
     * 启动进度播放通知
     * 定时器
     */
    private void startPublishProgress() {
        if (isEmptySize()){
            return;
        }
        if(!isPlaying()){
            return;
        }
        if(timeTask != null) {
            //已经启动了
            return;
        }
        timeTask = new TimerTask(){
            @Override
            public void run() {
                //如果没有监听器就停止计时器
                if(isEmptySize()){
                    stopPublishProgress();
                    return;
                }
                //这是子线程，不能直接操作 ui，所以使用 handler切换到主线程
                handler.obtainMessage(MESSAGE_PROGRESS).sendToTarget();
            }
        };

        //创建一个定时器
        timer = new Timer();

        //启动一个持续的任务 16 毫秒 为什么是 16 毫秒？因为后面要实现卡拉 ok 歌词为了保持
        //页面的连贯性 应该保持一秒 60 帧，1/60 就是一帧的事件，1000/60 = 16
        timer.schedule(timeTask, 0, 16);


    }

    private boolean isEmptySize() {
        return listeners.size() == 0;
    }
    /**
     * 停止播放进度通知
     */
    private void stopPublishProgress(){
        //停止定时器任务
        if(timeTask != null) {
            timeTask.cancel();
            timeTask = null;
        }

        //停止定时器
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 播放完毕回调
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        isPrepare = false;
        for (MusicPlayerListener listener : listeners) {
            listener.onCompletion(mp);
        }
    }

    @Override
    public void setLooping(boolean b) {
        player.setLooping(true);
    }
}
