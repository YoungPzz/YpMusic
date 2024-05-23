package com.example.ypcloundmusic.guide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ypcloundmusic.MainActivity;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.component.api.DefaultService;
import com.example.ypcloundmusic.component.api.NetWorkModule;
import com.example.ypcloundmusic.databinding.ActivityGuideBinding;
import com.example.ypcloundmusic.util.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class GuideActivity extends BaseLogicActivity {

    private com.example.ypcloundmusic.databinding.ActivityGuideBinding binding;
    private DefaultService service;
    private GuideAdapter guideAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guide);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.loginOrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                intent.setAction(Constant.ACTION_LOGIN);
                startActivity(intent);
                setShowGuide();
                finish();
            }
        });
        binding.experienceNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityAfterFinishThis(MainActivity.class);
                setShowGuide();
//                testGetNetwork();
            }
        });

    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //创建适配器
        guideAdapter = new GuideAdapter(getSupportFragmentManager());

        //设置适配器到控件
        binding.list.setAdapter(guideAdapter);

        //让指示器根据列表控件配合工作
        binding.indicator.setViewPager(binding.list);

        //适配器注册数据源观察者
//        guideAdapter.registerDataSetObserver(binding.indicator.getDataSetObserver());

        OkHttpClient okHttpClient = NetWorkModule.provideOkHttpClient();
        Retrofit retrofit = NetWorkModule.provideRetrofit(okHttpClient);
        service = retrofit.create(DefaultService.class);
    }

    private void testGetNetwork() {
//        service.sheets(null, 2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ListResponse<Sheet>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ListResponse<Sheet> sheetListResponse) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        service.comments()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ListResponse<Comment>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ListResponse<Comment> commentListResponse) {
//                        Comment comment = (Comment) commentListResponse.getData().getData().get(0);
//                        Log.d("testBug", comment.getContent());
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Log.d("testBug", String.valueOf(e));
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        service.sheetDetail("","1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<DetailResponse<Sheet>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull DetailResponse<Sheet> sheetDetailResponse) {
//                        Log.d("test", sheetDetailResponse.getData().getDetail());
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        service.sheetDetail("","1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new HttpObserver<DetailResponse<Sheet>>(GuideActivity.this, true) {
//                    @Override
//                    public void onSucceeded(DetailResponse<Sheet> data) {
////                        Toast.makeText(GuideActivity.this, data.getData().getTitle(), Toast.LENGTH_LONG).show();
////                        SuperToast.success("加载中");
//                    }
//
//                });
//        SuperRoundLoadingDialogFragment.show(getSupportFragmentManager(), R.string.loading ,new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void setShowGuide() {
//        PreferenceUtil.getInstance(this).setShowGuide(false);//下次不再显示Guide页面，直接进入Main界面
        //PreferenceUtil.getInstance(this)如果觉得这种写法麻烦，可以在BaseLogic类里抽离这些个代码
    }
}
