package com.example.ypcloundmusic.component.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.databinding.ActivityLoginHomeBinding;

public class LoginHomeActivity extends BaseLogicActivity {

    private ActivityLoginHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initViews() {
        super.initViews();
//        手机号登录点击事件，因为即将跳转的activity可能在多个地方复用，所以耍了一下
        binding.phoneLogin.setOnClickListener(v -> {
            InputUserIdentityActivity.startWithPhoneLogin(LoginHomeActivity.this);
        });
        binding.usernameLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, UsernameLoginActivity.class));
        });
    }
}