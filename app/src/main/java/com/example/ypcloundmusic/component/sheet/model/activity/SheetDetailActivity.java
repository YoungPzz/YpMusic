package com.example.ypcloundmusic.component.sheet.model.activity;

import static autodispose2.AutoDispose.autoDisposable;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.ActivitySheetDetailBinding;
import com.example.ypcloundmusic.databinding.HeaderSheetDetailBinding;
import com.example.ypcloundmusic.service.MusicPlayerService;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.ResourceUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.toast.SuperToast;

/**
 * 歌单详情页面
 */
public class SheetDetailActivity extends BaseTitleActivity<ActivitySheetDetailBinding> {
    private String id;

    private Sheet data;

    private ActivitySheetDetailBinding binding;
    private SongAdapter adapter;
    private com.example.ypcloundmusic.databinding.HeaderSheetDetailBinding headerBinding;
    private MenuItem deleteMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySheetDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        id = extraId();
        loadData();

        //创建适配器
        adapter = new SongAdapter(R.layout.item_song);

        //添加头部
        adapter.addHeaderView(createHeaderView());

        //设置适配器
        binding.list.setAdapter(adapter);
    }

    @Override
    protected void initViews() {
        super.initViews();
        //系统提供的方法，可以传入自己的toolbar代替actionbar
        binding.toolbar.toolbar.setBackgroundResource(R.color.sheetDetail);
        setSupportActionBar(binding.toolbar.toolbar);
        /**
         *显示返回按钮
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                play(position);
            }
        });
    }

    private View createHeaderView() {
        headerBinding = HeaderSheetDetailBinding.inflate(getLayoutInflater());

//        播放全部
        headerBinding.controlContainer.setOnClickListener(v -> {
            //点击播放全部就是播放第0首音乐
            play(0);
        });

        //用户容器点击
        headerBinding.userContainer.setOnClickListener(v -> {
            Intent intent = new Intent(SheetDetailActivity.this, UserDetailActivity.class);
            intent.putExtra(Constant.ID, data.getUserId());
            startActivity(intent);
        });

        headerBinding.collect.setOnClickListener(v -> {
//            processCollectClick();
        });
        return headerBinding.getRoot();
    }

    /**
     * 处理收藏和取消收藏逻辑
     */
//    private void processCollectClick() {
//        //用户未登录
////        if (!sp.isLogin()) {
////            startActivity(LoginHomeActivity.class);
////        }
//
//        if (data.isCollect()) {
//            //已经收藏了
//
//            //取消收藏
//            DefaultRepository.getInstance()
//                    .deleteCollect(data.getId())
//                    .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
//                    .subscribe(new HttpObserver<DetailResponse<Base>>() {
//                        @Override
//                        public void onSucceeded(DetailResponse<Base> d) {
//                            //弹出提示
//                            SuperToast.success(R.string.cancel_success);
//
//                            //重新加载数据
//                            //目的是显示新的收藏状态
//                            //loadData();
//
//                            //取消收藏成功
//                            data.setCollectId(null);
//
//                            //收藏数-1
//                            data.setCollectsCount(data.getCollectsCount() - 1);
//
//                            //刷新状态
//                            showCollectStatus();
//
//                            //发布歌单改变了事件
//                            publishSheetChangedEvent();
//                        }
//                    });
//        } else {
//            //没有收藏
//
//            //收藏
//            DefaultRepository.getInstance()
//                    .collect(data.getId())
//                    .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
//                    .subscribe(new HttpObserver<DetailResponse<Base>>() {
//                        @Override
//                        public void onSucceeded(DetailResponse<Base> d) {
//                            //弹出提示
//                            SuperToast.success(R.string.collection_success);
//
//                            //重新加载数据
//                            //目的是显示新的收藏状态
//                            //loadData();
//
//                            //收藏状态变更后
//                            //可以重新调用歌单详情界面接口
//                            //获取收藏状态
//                            //但对于收藏来说
//                            //收藏数可能没那么重要
//                            //所以不用及时刷新
//                            data.setCollectId("1");
//
//                            //收藏数+1
//                            data.setCollectsCount(data.getCollectsCount() + 1);
//
//                            //刷新状态
//                            showCollectStatus();
//
//                            //发布歌单改变了事件
//                            publishSheetChangedEvent();
//                        }
//                    });
//        }
//    }
    private void play(int position) {
        //获取当前位置的音乐
        Song data = adapter.getItem(position);

        //把当前歌单所有音乐设置到播放列表
        MusicPlayerService.getListManager(getApplicationContext()).setDatum(adapter.getData());

        //播放当前音乐
        MusicPlayerService.getListManager(getApplicationContext()).play(data);

        startMusicPlayerActivity(SheetDetailActivity.this);
    }

    protected void loadData() {
        DefaultRepository.getInstance()
                .sheetDetail(id)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<DetailResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        showData(data.getData());
                    }
                });

    }

    private void showData(Sheet data) {
        this.data = data;

        if (data.getSongs() != null && data.getSongs().size() > 0) {
            //有音乐才设置
            adapter.setNewInstance(data.getSongs());
        } else {
            adapter.setNewInstance(new ArrayList<>());
        }

        //header数据载入
        if (StringUtils.isBlank(data.getIcon())) {
            //图片为空

            //使用默认图片
            headerBinding.icon.setImageResource(R.drawable.placeholder);

//            setDefaultColor();
        } else {
            //有图片

            //获取图片绝对路径
            String uri = ResourceUtil.resourceUri(data.getIcon());

            //使用Glide
            Glide.with(SheetDetailActivity.this)
                    //加载图片
                    .load(uri)
                    //将图片设置到图片控件
                    .into(headerBinding.icon);
        }

        showCollectStatus();
        //显示标题
        headerBinding.title.setText(data.getTitle());
        //头像
        ImageUtil.showAvatar(SheetDetailActivity.this, headerBinding.avatar, data.getUser().getIcon());
        //昵称
        headerBinding.nickname.setText(data.getUser().getNickname());
        //评论数
        headerBinding.commentCount.setText(String.valueOf(data.getCommentsCount()));
        //音乐数
        headerBinding.count.setText(getResources().getString(R.string.music_count, data.getSongs().size()));

        //显示删除按钮状态
//        deleteMenuItem.setVisible(data.getUser().getId().equals(sp.getUserId()));
    }


    /**
     * 创建菜单方法
     * 有点类似显示布局要在onCreate方法中
     * 使用setContentView设置布局
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载按钮布局
        getMenuInflater().inflate(R.menu.menu_sheet_detail, menu);

        //查找按钮
        deleteMenuItem = menu.findItem(R.id.delete);

        //隐藏
        deleteMenuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单点击了回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //获取点击的菜单id
        int id = item.getItemId();

        if (R.id.search == id) {
            //搜索按钮点击了
            return true;
        } else if (R.id.sort == id) {
            //排序按钮点击了
            return true;
        } else if (R.id.delete == id) {
            //删除按钮点击了
            deleteSheet();
            return true;
        } else if (R.id.report == id) {
            //举报按钮点击了
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * TODO 删除歌单
     */
    private void deleteSheet() {

    }

    /**
     * 显示收藏状态
     */
    private void showCollectStatus() {
        if (data.isCollect()) {
            //收藏了

            //将按钮文字改为取消
            headerBinding.collect.setText(getResources()
                    .getString(R.string.cancel_collect, data.getCollectsCount()));

            //弱化取消收藏按钮
            //因为我们的本质是想让用户收藏歌单
            //所以去掉背景
            headerBinding.collect.setBackground(null);

            //设置文字颜色为灰色
            headerBinding.collect.setTextColor(getResources().getColor(R.color.black80));
        } else {
            //没有收藏

            //将按钮文字改为收藏
            headerBinding.collect.setText(getResources().getString(R.string.collect, data.getCollectsCount()));

            //设置按钮颜色为主色调
            headerBinding.collect.setBackgroundColor(getResources().getColor(R.color.primary));

            //将文字颜色设置为白色
            headerBinding.collect.setTextColor(getResources().getColor(R.color.white));
        }
    }
}