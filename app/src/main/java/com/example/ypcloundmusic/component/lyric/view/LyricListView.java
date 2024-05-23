package com.example.ypcloundmusic.component.lyric.view;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.lyric.adapter.LyricAdapter;
import com.example.ypcloundmusic.component.lyric.model.Line;
import com.example.ypcloundmusic.component.lyric.model.Lyric;
import com.example.ypcloundmusic.databinding.LyricListViewBinding;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.DensityUtil;
import com.example.ypcloundmusic.util.LyricUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 垂直显示多行歌词
 * 里面使用 RecyclerView 实现
 */
public class LyricListView extends LinearLayout {
    private LyricListViewBinding binding;
    private LinearLayoutManager layoutManager;
    private LyricAdapter lyricAdapter;
    private Lyric parsedLyric;
    //当前歌词行
    private int lyricLineNumber;
    private int lyricPlaceHolderSize;
    private int lyricOffsetX;
    private boolean isDrag;
    private TimerTask timerTask;
    private Timer timer;
    private Line scrollSelectedLyricLine;

    private LyricListListener lyricListListener;

    public LyricListView(Context context) {
        super(context);
        init();
    }

    public LyricListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LyricListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //TODO 学习自定义 VIEW 应该会学到
        //如果在某个控件里去获取整个 view 的高度这些，再实现歌词前后有空行（空一半）会不准确 在这里是最准确的
        //重写 onLayout 方法。这个方法会重写布局，所谓布局就是例如当一个控件里有两个子控件，当绘制这些的时候
        if(lyricOffsetX != 0) {
//            已经计算过了不需要再计算
            return;
        }

        //获取偏移高度
        //getMeasuredHeight()获取当前控间高度
        //item的高度是 40dp 要转回 dp
        lyricOffsetX = (int) (getMeasuredHeight() / 2 - (DensityUtil.dip2px(getContext(),40) / 2));



        //显示多少个
        lyricPlaceHolderSize = (int) Math.ceil(getMeasuredHeight() / 1.0 / 2 / DensityUtil.dip2px(getContext(), 40));
        next();
    }

    //TODO 这个 parent 代表什么 true自动把布局加载到 this 里
    private void init() {
        //加载布局
         binding = LyricListViewBinding.inflate(LayoutInflater.from(getContext()), this, true);
         initViews();
         initDatum();
         initListeners();
    }

    private void initListeners() {
        //添加歌词列表滚动事件
        binding.lyircList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * 滚动状态改变了
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState
             */
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == SCROLL_STATE_DRAGGING){
                    //拖拽状态
                    showDragView();
                }else if(newState == SCROLL_STATE_IDLE)
                    //空闲状态
                    prepareScrollLyricView();
            }

            /**
             * 用 dx dy 判断滚动到的地方是哪一个 item
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx            滚动的距离
             * @param dy           滚动的距离
             */
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //这里的dy是当前这一次滚动的距离
                //向上滚动+
                //向下滚动-

                //当前RecyclerView可视的第一个Item位置
                //+填充占位数
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() + lyricPlaceHolderSize - 1;

//                Timber.d("onPageScrolled dy:%d firstVisibleItemPosition:%d",dy,firstVisibleItemPosition);

                if (isDrag) {
                    //只有拖拽的时候才处理
                    Object data = lyricAdapter.getItem(firstVisibleItemPosition);

                    if (data instanceof String) {
                        //填充数据

                        //判断是开始还是末尾
                        if (firstVisibleItemPosition < lyricPlaceHolderSize) {
                            //开始位置的填充

                            //第一行歌词
                            scrollSelectedLyricLine = (Line) lyricAdapter.getItem(lyricPlaceHolderSize);
                        } else {
                            //末尾的填充

                            //最后一行歌词
                            int index = lyricAdapter.getItemCount() - 1 - lyricPlaceHolderSize;
                            scrollSelectedLyricLine = (Line) lyricAdapter.getItem(index);
                        }
                    } else {
                        //真实数据
                        scrollSelectedLyricLine = (Line) data;
                    }

                    //显示当前歌词开始时间
                    binding.lyricTime.setText(SuperDateUtil.ms2ms((int) scrollSelectedLyricLine.getStartTime()));
                }
            }


            //
        });
        binding.lyricPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消歌曲定时器
                cancelLyricTask();

                //马上滚动歌词
                showScrollLyricView();
                MusicPlayerService.getMusicPlayerManager(getContext()).seekTo((int) scrollSelectedLyricLine.getStartTime());
            }
        });

        binding.getRoot().setOnClickListener(v -> {
            if(lyricListListener != null) {
                lyricListListener.onLyricClick();
            }
        });

        lyricAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(lyricListListener != null) {
                lyricListListener.onLyricClick();
            }
        });
    }


    private void initDatum() {
        lyricAdapter = new LyricAdapter(R.layout.item_lyric);
        binding.lyircList.setAdapter(lyricAdapter);

    }

    private void initViews() {
        binding.lyircList.setHasFixedSize(true);//固定高度

        //设置布局管理器
        layoutManager = new LinearLayoutManager(getContext());
        binding.lyircList.setLayoutManager(layoutManager);

    }

    private void showDragView() {
        if(isLyricEmpty()){
            //没有歌词
            return;
        }
        //拖拽状态
        //是否是在拖拽中 意义是当为 true 的时候，歌词不要滚动
        isDrag = true;

        //显示歌词拖拽控件
        binding.lyricDragContainer.setVisibility(VISIBLE);
    }


    private void prepareScrollLyricView() {
        //准备滚动歌词 延迟一会再隐藏
        //创建延迟任务
        //当拖拽一会后停了，但没到 3 秒，这期间开启了定时任务，但又拖拽了，得把定时任务取消
        cancelLyricTask();


        timerTask = new TimerTask(){
            @Override
            public void run() {
                showScrollLyricView();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 3000);

    }

    private void cancelLyricTask() {
        //每次只能有一个定时器
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    private void showScrollLyricView() {
        isDrag = false;

        //隐藏拖拽控件
        binding.lyricDragContainer.setVisibility(INVISIBLE);

    }

    //没有歌词数据
    private boolean isLyricEmpty() {
        return lyricAdapter.getItemCount() == 0;
    }

    public void setData(Lyric parsedLyric) {
        this.parsedLyric = parsedLyric;

        if(lyricPlaceHolderSize > 0) {
            //已经计算了填充数量
            next();
        }
    }

    private void next() {
        if(parsedLyric == null) {
            //清空原来的歌词
            lyricAdapter.setNewInstance(new ArrayList<>());
        }else {
//            lyricAdapter.setNewInstance(parsedLyric.getDatum());

            //创建一个列表
            ArrayList<Object> datum = new ArrayList<>();

            //添加占位数据
            addLyricFillData(datum);

            datum.addAll(parsedLyric.getDatum());

            addLyricFillData(datum);

            lyricAdapter.setNewInstance(datum);
        }
    }

    /**
     * 添加歌词占位数据
     */
    public void addLyricFillData(List<Object> datum) {
        for (int i = 0; i < lyricPlaceHolderSize; i++) {
            datum.add("fill");
        }
    }
    /**
     * 设置播放角度
     * @param progress
     */
    public void setProgress(int progress) {
        if(parsedLyric == null || lyricAdapter.getData() == null || lyricAdapter.getData().size() == 0){
            //没有歌词
            return;
        }
        if(isDrag) {
            //正在拖拽 歌词不要滚动
            return;
        }

        //需要一个工具类，通过当前的输入的歌词的时间，返回对应歌词的索引
        int newLineNumber = LyricUtil.getLineNumber(parsedLyric, progress);

        if(newLineNumber != lyricLineNumber){
            //滑动到当前行
            scrollPosition(newLineNumber + lyricPlaceHolderSize);
            lyricLineNumber = newLineNumber;
        }

    }

    private void scrollPosition(int newLineNumber) {
        //因为歌词可能还没绘制完成，所以需要 post
        binding.lyircList.post(new Runnable() {
            @Override
            public void run() {
                //选中高亮效果
                //将索引设置到适配器
                lyricAdapter.setSelectedIndex(newLineNumber);

                //要让歌词显示到中间
                if(lyricOffsetX > 0) layoutManager.scrollToPositionWithOffset(newLineNumber, lyricOffsetX);
//                binding.lyircList.scrollToPosition(newLineNumber);

            }
        });
    }

    public void setLyricListListener(LyricListListener lyricListListener) {
        this.lyricListListener = lyricListListener;
    }

    /**
     * TODO 异步时间 接口？
     * 歌词列表控件监听器
     */
    public interface LyricListListener {
        /**
         * 歌词点击
         */
        default void onLyricClick() {

        }
    }

}
