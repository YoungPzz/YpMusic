package com.example.ypcloundmusic.component.login.activity;

import static autodispose2.AutoDispose.autoDisposable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.song.model.User;
import com.example.ypcloundmusic.databinding.ActivityRegisterBinding;
import com.example.ypcloundmusic.util.RegularUtil;
import com.example.ypcloundmusic.util.StringUtil;

import org.apache.commons.lang3.StringUtils;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.toast.SuperToast;

public class RegisterActivity extends BaseLoginActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.primary.setOnClickListener(v -> {
            //类型检测
            processNext();
        });
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        binding.nickname.setText("ypyp");
        binding.phone.setText("13342567865");
        binding.email.setText("76sjdb@163.com");
        binding.password.setText("666666");
        binding.confirmPassword.setText("666666");
    }

    private void processNext() {
        User data = new User();
        //获取昵称
        String nickname = binding.nickname.getText().toString().trim();

        if (StringUtils.isBlank(nickname)) {
            SuperToast.show(R.string.enter_nickname);
            return;
        }

        //判断昵称格式
        if (!StringUtil.isNickname(nickname)) {
            SuperToast.show(R.string.error_nickname_format);
            return;
        }

        data.setNickname(nickname);

        //手机号
        String phone = binding.phone.getText().toString().trim();
        if (StringUtils.isBlank(phone)) {
            SuperToast.show(R.string.enter_phone);
            return;
        }

        //手机号格式
        if (!RegularUtil.isPhone(phone)) {
            SuperToast.show(R.string.error_phone_format);
            return;
        }

        //邮箱
        String email = binding.email.getText().toString().trim();
        if (StringUtils.isBlank(email)) {
            SuperToast.show(R.string.enter_email);
            return;
        }

        //邮箱格式
        if (!RegularUtil.isEmail(email)) {
            SuperToast.show(R.string.error_email_format);
            return;
        }

        //密码
        String password = binding.password.getText().toString().trim();
        if (StringUtils.isBlank(password)) {
            SuperToast.show(R.string.enter_password);
            return;
        }

        //密码格式
        if (!StringUtil.isPassword(password)) {
            SuperToast.show(R.string.error_password_format);
            return;
        }

        //确认密码
        String confirmPassword = binding.confirmPassword.getText().toString().trim();
        if (StringUtils.isBlank(confirmPassword)) {
            SuperToast.show(R.string.enter_confirm_password);
            return;
        }

        //确认密码格式
        if (!StringUtil.isPassword(confirmPassword)) {
            SuperToast.show(R.string.error_confirm_password_format);
            return;
        }

        //判断密码和确认密码是否一样
        if (!password.equals(confirmPassword)) {
            SuperToast.show(R.string.error_confirm_password);
            return;
        }
        data.setNickname(nickname);
        data.setPhone(phone);
        data.setEmail(email);
        data.setPassword(password);

        registerClick(data);
    }

    private void registerClick(User data) {
        //调用注册接口
        DefaultRepository.getInstance().register(data)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<DetailResponse<BaseId>>() {
                    @Override
                    public boolean onFailed(DetailResponse<BaseId> data, Throwable e) {
                        Log.d("TAFDD", String.valueOf(e));
                        return super.onFailed(data, e);
                    }

                    @Override
                    public void onSucceeded(DetailResponse<BaseId> d) {
                        login(User.createLogin(data.getPhone(), data.getEmail(), data.getPassword()));
                    }
                });
    }
}