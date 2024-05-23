package com.example.ypcloundmusic.component.splash.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.example.ypcloundmusic.MainActivity;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.component.splash.fragment.TermServiceAgreementDialog;
import com.example.ypcloundmusic.config.Config;
import com.example.ypcloundmusic.databinding.ActivitySplashBinding;
import com.example.ypcloundmusic.guide.GuideActivity;
import com.example.ypcloundmusic.util.DefaultPreferenceUtil;
import com.example.ypcloundmusic.util.PreferenceUtil;
import com.example.ypcloundmusic.util.SuperDarkUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

//启动界面
@RuntimePermissions
public class SplashActivity extends BaseLogicActivity {

//    private TextView copyRightView;
    private com.example.ypcloundmusic.databinding.ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initViews() {
        super.initViews();
//      copyRightView = findViewById(R.id.copyright);
        //QMUI 设置沉浸状态栏 没有电量啊这些
        QMUIStatusBarHelper.translucent(this);
        if (SuperDarkUtil.isDark(this)) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        } else {
            //白色状态栏
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }
    }

    @Override
    protected void initDatum() {
        //设置版本年份
        super.initDatum();
        int year = SuperDateUtil.currentYear();
//        copyRightView.setText(getResources().getString(R.string.copyright, year));
        binding.copyright.setText(getResources().getString(R.string.copyright, year));

        if (DefaultPreferenceUtil.getInstance(this).isAcceptTermsServiceAgreement()) {
            //不是第一次进入
            checkPermission();
            prepareNext();
        } else {
            //第一次进入
            showTermServiceAgreementDialog();
        }
    }

    private void showTermServiceAgreementDialog() {
        TermServiceAgreementDialog.show(getSupportFragmentManager(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultPreferenceUtil.getInstance(SplashActivity.this).setAcceptTermsServiceAgreement();
                checkPermission();
                prepareNext();//下一步
            }
        });
    }

    private void prepareNext() {
        if(PreferenceUtil.getInstance(this).isShowGuide()){
            startActivityAfterFinishThis(GuideActivity.class);
            return;
        }
        postNext();
    }

    private void postNext() {
        binding.copyright.postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, Config.SPLASH_DEFAULT_DELAY_TIME);
    }

    private void next() {
        startActivityAfterFinishThis(MainActivity.class);
    }


    private void checkPermission() {
        //让动态框架检查是否授权了
        //如果不使用框架就使用系统提供的API检查
        //它内部也是使用系统API检查
        //只是使用框架就更简单了
        SplashActivityPermissionsDispatcher.onPermissionGrantedWithPermissionCheck(this);
    }
    /**
     * 权限授权了就会调用该方法
     * 请求相机权限目的是扫描二维码，拍照
     */
    @NeedsPermission({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    void onPermissionGranted() {
        //如果有权限就进入下一步
//        prepareNext();
    }

    /**
     * 显示权限授权对话框
     * 目的是提示用户
     */
    @OnShowRationale({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    void showRequestPermission(PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_hint)
                .setPositiveButton(R.string.allow, (dialog, which) -> request.proceed())
                .setNegativeButton(R.string.deny, (dialog, which) -> request.cancel()).show();
    }

    /**
     * 拒绝了权限调用
     */
    @OnPermissionDenied({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    void showDenied() {
        //退出应用
        finish();
    }

    /**
     * 再次获取权限的提示
     */
    @OnNeverAskAgain({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    void showNeverAsk() {
        //继续请求权限
        checkPermission();
    }

    /**
     * 授权后回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将授权结果传递到框架
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}