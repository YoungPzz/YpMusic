package com.example.ypcloundmusic.component.feed.activity;

import static autodispose2.AutoDispose.autoDisposable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ypcloundmusic.Model.Base;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.feed.model.Feed;
import com.example.ypcloundmusic.component.main.tab.fragments.FeedFragment;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.databinding.ActivityPublishFeedBinding;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.toast.SuperToast;

public class PublishFeedActivity extends BaseTitleActivity<ActivityPublishFeedBinding> {
    private Feed feed;
    private ActivityPublishFeedBinding binding;
    private String content;
    private String text;
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

        saveFeed();
    }

    private void saveFeed() {
        feed = new Feed();
        feed.setContent(content);
        feed.setId("245932161");
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