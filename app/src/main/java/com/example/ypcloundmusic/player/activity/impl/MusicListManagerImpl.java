package com.example.ypcloundmusic.player.activity.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.player.activity.manager.MusicListManager;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerListener;
import com.example.ypcloundmusic.player.activity.manager.MusicPlayerManager;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.ResourceUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//列表管理器实现
public class MusicListManagerImpl implements MusicListManager, MusicPlayerListener {

    private static final int MODEL_LOOP_LIST = 0;
    private static final int MODEL_LOOP_ONE = 1;
    private static final int MODEL_LOOP_RANDOM = 2;


    private static MusicListManagerImpl instance;
    private final Context context;
    private final MusicPlayerManager musicPlayerManager;

    /**
     * 列表
     * 之所以不用 arraylist 是因为里面的数据要经常删除，效率 ll 高
     */
    private List<Song> datum = new LinkedList<>();
    private boolean isPlay;
    private Song data;
    /**
     * 循环模式
     */
    private int model = MODEL_LOOP_LIST;
    private MusicListManagerImpl(Context context) {
        this.context = context.getApplicationContext();

        //一些功能还是要用到音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);

        //添加一个监听器，监听歌曲播放完成后发来的事件
        musicPlayerManager.addMusicPlayerListener(new MusicPlayerListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (model == MODEL_LOOP_ONE) {
                    //如果是单曲循环
                    //就不会处理了
                    //因为我们使用了MediaPlayer的循环模式
                    //如果使用的第三方框架
                    //如果没有循环模式
                    //那就要在这里继续播放当前音乐
                } else {
                    Song data = next();
                    if (data != null) {
                        play(data);
                    }
                }
            }
        });
    }
    //依旧是单例模式
    public synchronized static MusicListManager getInstance(Context context) {
        if(instance == null) {
            instance = new MusicListManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void setDatum(List<Song> datum) {
        //清空原来的数据
        this.datum.clear();;

        //添加新的数据
        this.datum.addAll(datum);
    }

    @Override
    public List<Song> getDatum() {
        return datum;
    }

    @Override
    public void play(Song data) {
        //当前黑胶唱片滚动
        data.setRotate(true);

        //标记已经播放了
        isPlay = true;
        //保存数据
        this.data = data;
        //播放在线音乐
        String path = ResourceUtil.resourceUri(data.getUri());

        try {
            musicPlayerManager.play(path, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pause() {
        musicPlayerManager.pause();
    }

    @Override
    public void resume() {
        if (isPlay) {
            //原来已经播放过
            //也就说播放器已经初始化了
            musicPlayerManager.resume();
        } else {
            //到这里，是应用开启后，第一次点继续播放
            //而这时内部其实还没有准备播放，所以应该调用播放
            play(data);

            //判断是否需要继续播放
            if (data.getProgress() > 0) {
                //有播放进度
                //就从上一次位置开始播放
                musicPlayerManager.seekTo((int) data.getProgress());
            }
        }
    }

    @Override
    public Song getData() {

        return data;
    }

    @Override
    public int getLoopModel() {
        return model;
    }

    @Override
    public int changeLoopModel() {
        model++;
        if(model > MODEL_LOOP_RANDOM){
            model = MODEL_LOOP_LIST;
        }


        //判断是否是单曲循环
        if (MODEL_LOOP_ONE == model) {
            //单曲循环模式
            //设置到mediaPlay
            musicPlayerManager.setLooping(true);
        } else {
            //其他模式关闭循环
            musicPlayerManager.setLooping(false);
        }
        return model;
    }

    @Override
    public Song next() {
        if (datum.size() == 0) {
            //如果没有音乐了
            //直接返回null
            return null;
        }

        //音乐索引
        int index = 0;

        //判断循环模式
        switch (model) {
            case MODEL_LOOP_RANDOM:
                //随机循环

                //在0~datum.size()中
                //不包含datum.size()
                index = new Random().nextInt(datum.size());
                break;
            default:
                //找到当前音乐索引
                index = datum.indexOf(data);

                if (index != -1) {
                    //找到了

                    //如果当前播放是列表最后一个
                    if (index == datum.size() - 1) {
                        //最后一首音乐

                        //那就从0开始播放
                        index = 0;
                    } else {
                        index++;
                    }
                } else {
                    //抛出异常
                    //因为正常情况下是能找到的
                    throw new IllegalArgumentException("Cant'found current song");
                }
                break;
        }
        //获取音乐
        return datum.get(index);
    }
    @Override
    public Song previous() {

        //音乐索引
        int index = 0;

        //判断循环模式
        switch (model) {
            case MODEL_LOOP_RANDOM:
                //随机循环

                //在0~datum.size()中
                //不包含datum.size()
                index = new Random().nextInt(datum.size());
                break;
            default:
                //找到当前音乐索引
                index = datum.indexOf(data);

                if (index != -1) {
                    //找到了

                    //如果当前播放是列表第一个
                    if (index == 0) {
                        //第一首音乐

                        //那就从最后开始播放
                        index = datum.size() - 1;
                    } else {
                        index--;
                    }
                } else {
                    //抛出异常
                    //因为正常情况下是能找到的
                    throw new IllegalArgumentException("Cant't find current song");
                }
                break;
        }

        //获取音乐
        return datum.get(index);
    }

}
