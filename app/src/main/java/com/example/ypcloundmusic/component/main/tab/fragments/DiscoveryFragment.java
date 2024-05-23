package com.example.ypcloundmusic.component.main.tab.fragments;

import static autodispose2.AutoDispose.autoDisposable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ypcloundmusic.Fragment.BaseCommonFragment;
import com.example.ypcloundmusic.Fragment.BaseFragment;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.ad.model.Ad;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.main.tab.discovery.BannerData;
import com.example.ypcloundmusic.component.main.tab.discovery.BaseMultiItemEntity;
import com.example.ypcloundmusic.component.main.tab.discovery.ButtonData;
import com.example.ypcloundmusic.component.main.tab.discovery.DiscoveryAdapter;
import com.example.ypcloundmusic.component.main.tab.discovery.SheetData;
import com.example.ypcloundmusic.component.main.tab.discovery.SongData;
import com.example.ypcloundmusic.component.placeholder.PlaceholderView;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.component.song.model.Song;
import com.example.ypcloundmusic.databinding.FragmentDiscoveryBinding;
import com.example.ypcloundmusic.util.Constant;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoveryFragment extends BaseCommonFragment {

    /**
     * 列表数据对象
     */
    private List<BaseMultiItemEntity> datum;
    private com.example.ypcloundmusic.databinding.FragmentDiscoveryBinding binding;
    private LinearLayoutManager layoutManager;
    private DiscoveryAdapter discoveryAdapter;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiscoveryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initViews() {
        super.initViews();

        //高度固定
        //可以提升性能
        binding.list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getHostActivity());
        binding.list.setLayoutManager(layoutManager);

//分割线
        DividerItemDecoration decoration =
                new DividerItemDecoration(binding.list.getContext(), RecyclerView.VERTICAL);
        binding.list.addItemDecoration(decoration);

    }


    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        discoveryAdapter = new DiscoveryAdapter(this, new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {

            }
        });
        // 设置适配器
        binding.list.setAdapter(discoveryAdapter);

        loadData(false);


    }

    @Override
    protected void loadData(boolean isPlaceholder) {
        super.loadData(isPlaceholder);
        //广告API
        datum = new ArrayList<>();
        Observable<ListResponse<Ad>> ads = DefaultRepository.getInstance().bannerAd();
        ads
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<ListResponse<Ad>>() {
                    @Override
                    public void onSucceeded(ListResponse<Ad> data) {
                        //添加轮播图
                        datum.add(new BannerData(
                                data.getData().getData()
                        ));

                        //请求歌单数据
                        loadSheetData();

                        datum.add(new ButtonData());

                        loadSongData();

                        if(isPlaceholder){
                            binding.placeholder.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public boolean onFailed(ListResponse<Ad> data, Throwable e) {
                        binding.placeholder.setVisibility(View.VISIBLE);
                        return super.onFailed(data, e);
                    }
                });

//        OkHttpClient client = new OkHttpClient.Builder().build();
//        Request request = new Request.Builder()
//                .url("http://my-cloud-music-api-sp3-dev.ixuea.com/v1/ads?&position=0")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//            }
//        });
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //点击 placeholder
        PlaceholderView placeholderView = getPlaceholderView();
    }

    private void loadSongData() {
        Observable<ListResponse<Song>> songs = DefaultRepository.getInstance().songs();
        songs
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<ListResponse<Song>>() {

                    @Override
                    public void onSucceeded(ListResponse<Song> data) {
                        datum.add(new SongData(data.getData().getData()));
                    }
                });

        //把数据添加到适配器中
        discoveryAdapter.setNewInstance(datum);
    }

    private void loadSheetData() {
        Observable<ListResponse<Sheet>> sheets = DefaultRepository.getInstance().sheets(Constant.SIZE12);
        sheets
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> data) {
                        //添加到歌单数据
                        datum.add(new SheetData(data.getData().getData()));
//                        //设置数据到适配器
//                        discoveryAdapter.setNewInstance(datum);
                    }

                    @Override
                    public boolean onFailed(ListResponse<Sheet> data, Throwable e) {
                        Log.d("sd", String.valueOf(e));
                        return super.onFailed(data, e);
                    }
                });
    }

    public static DiscoveryFragment newInstance() {

        Bundle args = new Bundle();

        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
