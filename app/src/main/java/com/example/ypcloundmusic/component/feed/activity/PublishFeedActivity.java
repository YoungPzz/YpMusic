package com.example.ypcloundmusic.component.feed.activity;

import static autodispose2.AutoDispose.autoDisposable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.ypcloundmusic.Model.Base;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.feed.Glide.GlideEngine;
import com.example.ypcloundmusic.component.feed.adapter.ImageAdapter;
import com.example.ypcloundmusic.component.feed.model.Feed;
import com.example.ypcloundmusic.component.feed.model.Resource;
import com.example.ypcloundmusic.component.feed.task.Result;
import com.example.ypcloundmusic.component.feed.task.UploadFeedImageAsyncTask;
import com.example.ypcloundmusic.component.loaction.activity.SelectedLocationActivity;
import com.example.ypcloundmusic.component.main.tab.fragments.FeedFragment;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.databinding.ActivityPublishFeedBinding;
import com.example.ypcloundmusic.util.DensityUtil;
import com.google.common.collect.Lists;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.GridDividerItemDecoration;
import superui.text.toast.SuperToast;
import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

public class PublishFeedActivity extends BaseTitleActivity<ActivityPublishFeedBinding> {
    private Feed feed;
    private ActivityPublishFeedBinding binding;
    private String content;
    private String text;
    private ImageAdapter adapter;

    private ProgressDialog progressDialog;
    private List<LocalMedia> selectedImages;
    //
//    private  FeedListener feedListener;
//
//    public  void setFeedListener(FeedListener feedListener){
//        feedListener = feedListener;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishFeedBinding.inflate(getLayoutInflater());
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

        setTitle("发布动态");

        binding.count.setText(String.format(getApplication().getString(R.string.feed_count), 0));

        GridLayoutManager layoutManager = new GridLayoutManager(PublishFeedActivity.this, 4);
        binding.list.setLayoutManager(layoutManager);

        GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(PublishFeedActivity.this, (int) DensityUtil.dip2px(PublishFeedActivity.this, 5F));
        binding.list.addItemDecoration(itemDecoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        adapter = new ImageAdapter(R.layout.item_image);
        binding.list.setAdapter(adapter);
        setData(new ArrayList<>());
    }

    private void setData(List<Object> datum) {
        if(datum.size() < 9){
            //添加选择图片按钮
            datum.add(R.drawable.add_fill);
        }

        adapter.setNewInstance(datum);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                text = binding.content.getText().toString().trim();

                binding.count.setText(String.format(getApplication().getString(R.string.feed_count), text.length()));
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(adapter.getItem(position) instanceof Integer){
                    //+号图片
                    selectImage();
                }
            }
        });

        adapter.addChildClickViewIds(R.id.close);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                adapter.removeAt(position);
            }
        });

        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishFeedActivity.this, SelectedLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectImage() {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(9)// 最大图片选择数量 int
                .setMinSelectNum(1)// 最小选择数量 int
                .setImageSpanCount(3)// 每行显示个数 int
                .setSelectionMode(SelectModeConfig.MULTIPLE)// 多选 or 单选 MULTIPLE or SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isDisplayCamera(true)// 是否显示拍照按钮 true or false
                .setCameraImageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                //压缩
                .setCompressEngine(new CompressFileEngine() {
                    @Override
                    public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
                        Luban.with(context).load(source).ignoreBy(100)
                                .setCompressListener(new OnNewCompressListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(String source, File compressFile) {
                                        if (call != null) {
                                            call.onCallback(source, compressFile.getAbsolutePath());
                                        }
                                    }

                                    @Override
                                    public void onError(String source, Throwable e) {
                                        if (call != null) {
                                            call.onCallback(source, null);
                                        }
                                    }
                                }).launch();
                    }
                })
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        setData(Lists.newArrayList(result));
                    }

                    @Override
                    public void onCancel() {

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //按钮点击
        if (item.getItemId() == R.id.publish) {
            sendClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendClick() {
        //获取输入的内容
        content = binding.content.getText().toString().trim();

        //判断是否输入了
        if (StringUtils.isBlank(content)) {
            SuperToast.error(R.string.hint_feed);
            return;
        }

        //判断长度
        //至于为什么是140
        //市面上大部分软件都是这样
        //大家感兴趣可以搜索下
        if (content.length() > 140) {
            SuperToast.error(R.string.error_content_length);
            return;
        }

        //判断有没有图片 获取选中的图片
        selectedImages = getSelectedImages();
        if(selectedImages.size() > 0) {
            //有图片
            uploadImage(selectedImages);
        } else {
            //无图片
            saveFeed(null);

        }
    }

    private void uploadImage(List<LocalMedia> selectedImages) {
        new UploadFeedImageAsyncTask(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressDialog.setMessage("正在加载第" + values[0] + "张图片");
            }

            @Override
            protected void onPostExecute(Result<List<Resource>> result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                List<Resource> results = result.getData();
                if(result.isSucceeded() && results.size() == selectedImages.size()) {
                    //图片上传成功
                    saveFeed(results);
                } else {
                    //上传图片失败 真是项目中可以实现重现 只上传失败的图片
                    if(result.getThrowable() != null){

                    }else{
                        SuperToast.show(R.string.error_upload_image);
                    }
                }

            }
        }.execute();
    }

    private List<LocalMedia> getSelectedImages() {
        List<Object> datum = adapter.getData();
        List<LocalMedia> data = new ArrayList<>();
        for(Object o : datum){
            if(o instanceof LocalMedia){
                data.add((LocalMedia) o);
            }
        }
        return data;
    }

    private void saveFeed(List<Resource> list) {
        feed = new Feed();
        feed.setContent(content);
        feed.setMedias(list);
        DefaultRepository.getInstance()
                .createFeed(feed)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<DetailResponse<Base>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Base> data) {
                        //发布通知
//                        EventBus.getDefault().post(new FeedChangedEvent());
                        FeedFragment feedFragment = (FeedFragment) getSupportFragmentManager().findFragmentById(R.id.feedFragment);
                        feedFragment.loadData(false);
                        //关闭界面
                        finish();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<Base> data, Throwable e) {
                        Log.d("error", e.toString());
                        return super.onFailed(data, e);
                    }
                });
    }



    //    public interface FeedListener {
//        void publish();
//    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        super.onAttachFragment(fragment);
//        feedListener = (FeedListener) fragment;
//    }
}