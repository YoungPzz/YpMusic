package com.example.ypcloundmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.adapter.OnPageChangeListenerAdapter;
import com.example.ypcloundmusic.component.login.activity.LoginHomeActivity;
import com.example.ypcloundmusic.component.main.tab.MainAdapter;
import com.example.ypcloundmusic.component.main.tab.TabEntity;
import com.example.ypcloundmusic.component.splash.activity.SplashActivity;
import com.example.ypcloundmusic.databinding.ActivityMainBinding;
import com.example.ypcloundmusic.util.Constant;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;

public class MainActivity extends BaseLogicActivity {

    private com.example.ypcloundmusic.databinding.ActivityMainBinding binding;

    /**
     * 底部指示器（tab）文本，图标，选中的图标
     */
    private static final int[] indicatorTitles = new int[]{R.string.discovery, R.string.video, R.string.me, R.string.feed, R.string.live};
    private static final int[] indicatorIcons = new int[]{R.drawable.discovery, R.drawable.video, R.drawable.me, R.drawable.feed, R.drawable.live};
    private static final int[] indicatorSelectedIcons = new int[]{R.drawable.discovery_selected, R.drawable.video_selected, R.drawable.me_selected, R.drawable.feed_selected, R.drawable.live_selected};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initViews() {
        super.initViews();
        //状态栏透明，内容显示到状态栏
//        QMUIStatusBarHelper.translucent(this);
        //缓存页面数量*/
        // 默认是缓存一个
        binding.list.setOffscreenPageLimit(5);

        //对于这个FlycoTabLayout框架来说，就是要传入一个列表数据
        ArrayList<CustomTabEntity> indicatorTabs = new ArrayList<>();
        for (int i = 0; i < indicatorTitles.length; i++) {
            indicatorTabs.add(
                    new TabEntity(getString(indicatorTitles[i]), indicatorSelectedIcons[i], indicatorIcons[i])
            );
        }
        binding.indicator.setTabData(indicatorTabs);
        //动态tab显示消息提醒
        binding.indicator.showDot(3);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        binding.list.setAdapter(adapter);

        String action = getIntent().getAction();
        if (Constant.ACTION_LOGIN.equals(action)) {
            //跳转到启动界面
            startActivity(new Intent(this, LoginHomeActivity.class));
        }
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.indicator.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.list.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        binding.list.addOnPageChangeListener(new OnPageChangeListenerAdapter(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.indicator.setCurrentTab(position);
            }
        });
    }

}