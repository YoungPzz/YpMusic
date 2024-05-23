package com.example.ypcloundmusic.player.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.RecordPageViewBinding;
import com.example.ypcloundmusic.player.adapter.MusicPlayerRecordAdapter;
import com.example.ypcloundmusic.util.DensityUtil;

import java.util.List;

/**
 * 黑胶唱片左右滚动 View
 */
public class RecordPageView extends LinearLayout implements ValueAnimator.AnimatorUpdateListener {
    private static final float THUMB_ROTATION_PAUSE = -25F;
    private static final float THUMB_ROTATION_PLAY = 0;
    public RecordPageViewBinding binding;
    private MusicPlayerRecordAdapter adapter;
    private boolean isPlaying;
    //属性动画 TODO 动画相关
    private ObjectAnimator playThumbAnimator;
    private ValueAnimator pauseThumbAnimator;

    public RecordPageView(Context context) {
        super(context);
        init();
    }

    public RecordPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RecordPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        binding = RecordPageViewBinding.inflate(LayoutInflater.from(getContext()), this, true);
        initView();
        initData();
    }

    private void initData() {
        setPlaying(false);
    }

    private void initView() {

        //黑胶唱片指针旋转点
        //旋转点为15dp
        //而设置需要单位为px
        //所以要先转换
        int rotate = (int) DensityUtil.dip2px(getContext(), 15);
        binding.recordThumb.setPivotX(rotate);
        binding.recordThumb.setPivotY(rotate);

        //缓存页面数量 也就是说 左边右边滑动 即使不滑倒底也能显示效果
        binding.list.setOffscreenPageLimit(3);

        //从暂停到播放状态动画
        //从-25到0
        playThumbAnimator = ObjectAnimator.ofFloat(binding.recordThumb, "rotation", THUMB_ROTATION_PAUSE, THUMB_ROTATION_PLAY);

        //设置执行时间300ms
        playThumbAnimator.setDuration(300);

        //从播放到暂停状态动画
        pauseThumbAnimator = ValueAnimator.ofFloat(THUMB_ROTATION_PLAY, THUMB_ROTATION_PAUSE);

        //设置执行时间
        pauseThumbAnimator.setDuration(300);

        //设置更新监听器
        pauseThumbAnimator.addUpdateListener(this);
    }

    public void initAdapter(FragmentActivity fragmentActivity) {
        //创建黑胶唱片列表适配器
        adapter = new MusicPlayerRecordAdapter(fragmentActivity);

        //设置到控件
        binding.list.setAdapter(adapter);

    }


    public void setData(List<Song> datum) {
        adapter.setDatum(datum);
    }

    /**
     * 选中的音乐
     */
    public void scrollPosition(int index) {
        binding.list.post(new Runnable() {
            @Override
            public void run() {
                if (index != -1) {
                    //滚动到该位置
                    binding.list.setCurrentItem(index, false);
                }
            }
        });
    }

    public void setPlaying(boolean isPlaying) {
        if (this.isPlaying == isPlaying) {
            return;
        }

        this.isPlaying = isPlaying;
        invalidatePlayingStatus();
    }

    private void invalidatePlayingStatus() {
        if (isPlaying) {
            playThumbAnimator.start();
        } else {
            //获取黑胶唱片指针旋转的角度
            float thumbRotation = binding.recordThumb.getRotation();

            //如果不判断
            //当前已经停止了
            //还会执行动画
            //就有跳动问题
            if (THUMB_ROTATION_PAUSE == thumbRotation) {
                //已经是停止状态了

                //就返回
                return;
            }

            pauseThumbAnimator.start();
        }
    }

    /**
     * 属性动画回调
     *
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        binding.recordThumb.setRotation((Float) animation.getAnimatedValue());
    }
}
