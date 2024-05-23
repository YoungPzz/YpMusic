package com.example.ypcloundmusic.component.login.activity;

import static autodispose2.AutoDispose.autoDisposable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.ypcloundmusic.AppContext;
import com.example.ypcloundmusic.MainActivity;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.Model.response.ListResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.activity.BaseTitleActivity;
import com.example.ypcloundmusic.component.ad.model.Ad;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.login.model.Session;
import com.example.ypcloundmusic.component.main.tab.discovery.BannerData;
import com.example.ypcloundmusic.component.main.tab.discovery.ButtonData;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.song.model.User;
import com.example.ypcloundmusic.config.Config;
import com.example.ypcloundmusic.databinding.ActivityUsernameLoginBinding;
import com.example.ypcloundmusic.util.PreferenceUtil;
import com.example.ypcloundmusic.util.RegularUtil;
import com.example.ypcloundmusic.util.StringUtil;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.toast.SuperToast;

public class UsernameLoginActivity extends BaseTitleActivity<ActivityUsernameLoginBinding> {
    private ActivityUsernameLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsernameLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        binding.username.setText("13141111222");
        binding.password.setText("ixueaedu");
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
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.primary.setOnClickListener(v -> {
            //获取用户名
            String username = binding.username.getText().toString().trim();
            if (username.isEmpty()) {
                SuperToast.show(R.string.enter_username);
            }

            //检测用户名如果不是手机号或者邮箱 报格式错误
            if (!(RegularUtil.isPhone(username) || RegularUtil.isEmail(username))) {
                SuperToast.show(R.string.error_username_format);
                return;
            }
            //获取密码
            String password = binding.password.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                //Toast.makeText(getMainActivity(), R.string.enter_password, Toast.LENGTH_SHORT).show();

                SuperToast.show(R.string.enter_password);
                return;
            }

            //判断密码格式
            if (!StringUtil.isPassword(password)) {
                SuperToast.show(R.string.error_password_format);
                return;
            }

            //判断是手机号还有邮箱
            String phone = null;
            String email = null;

            if (RegularUtil.isPhone(username)) {
                //手机号
                phone = username;
            } else {
                //邮箱
                email = username;
            }

            //调用父类的登录方法
            login(User.createLogin(phone, email, password));
        });
        //注册按钮点击
        binding.register.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        //忘记密码点击
        binding.forgotPassword.setOnClickListener(v -> InputUserIdentityActivity.startWithForgotPassword(UsernameLoginActivity.this));
    }




}