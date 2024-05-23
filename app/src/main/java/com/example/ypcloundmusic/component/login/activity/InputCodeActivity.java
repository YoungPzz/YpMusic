package com.example.ypcloundmusic.component.login.activity;

import static autodispose2.AutoDispose.autoDisposable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ypcloundmusic.Model.Base;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.login.model.CodeRequest;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.databinding.ActivityInputCodeBinding;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.MyActivityManager;
import com.king.view.splitedittext.SplitEditText;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.toast.SuperToast;

public class InputCodeActivity extends BaseLoginActivity {
    private ActivityInputCodeBinding binding;

    private String username;
    private CodeRequest codeRequest;

    //1:登录 2:忘记密码
    private int codeStyle;

    //服务端返回的验证码，当然项目中不能这样做
    private String code;
    private CountDownTimer countDownTimer;

    /**
     * 手机号登录
     *
     * @param context
     * @param username
     */
    public static void startWithPhoneLogin(Context context, String username) {
        start(context, Constant.STYLE_PHONE_LOGIN, username);
    }

    /**
     * 忘记密码
     * @param context
     */
    public static void startWithForgotPassword(Context context, String username) {
        start(context, Constant.STYLE_FORGOT_PASSWORD, username);
    }

    private static void start(Context context, int style, String username) {
        Intent intent = new Intent(context, InputCodeActivity.class);
        intent.putExtra("Style", style);
        intent.putExtra("Username", username);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInputCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        codeStyle = getIntent().getIntExtra("Style", Constant.STYLE_PHONE_LOGIN);
    }

    @Override
    protected void initViews() {
        super.initViews();
        username = getIntent().getStringExtra("Username");
        binding.codeSendTarget.setText("验证码已发送到" + username);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        codeRequest = new CodeRequest();
        if(codeStyle == Constant.STYLE_PHONE_LOGIN){
            codeRequest.setPhone(username);
        }else {
            codeRequest.setEmail(username);
        }
        sendCode();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.code.setOnTextInputListener(new SplitEditText.OnSimpleTextInputListener() {
            @Override
            public void onTextInputCompleted(@NonNull String text) {
                processNext(text);
            }
        });
        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void processNext(String text) {
        if(codeStyle == Constant.STYLE_PHONE_LOGIN){
            //手机号验证码登录
            login(username, text);
        }else {
            //先校验验证码
            codeRequest.setCode(text);
            DefaultRepository.getInstance()
                    .checkCode(codeRequest)
                    .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                    .subscribe(new HttpObserver<DetailResponse<Base>>() {
                        @Override
                        public void onSucceeded(DetailResponse<Base> d) {
                            //重设密码

                        }

                        @Override
                        public boolean onFailed(DetailResponse<Base> data, Throwable e) {
                            //清除验证码输入的内容
                            binding.code.setText("");
                            return super.onFailed(data, e);
                        }
                    });

        }
    }


    private void sendCode() {
        DefaultRepository.getInstance()
                .sendCode(codeStyle, codeRequest)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<DetailResponse<Base>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Base> data) {
                        //发送成功了
                        Log.d("TAF",data.toString());
                        //开始倒计时
                      startCountDown();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<Base> data, Throwable e) {
                        Log.d("TAF",e.toString());
                        return super.onFailed(data, e);
                    }
                });
    }

    /**
     * 开启倒计时
     * 现在没有保存退出的状态
     * 也就是说，返回再进来就可以点击
     */
    private void startCountDown() {
        //系统提供的类
        countDownTimer = new CountDownTimer(60000, 1000) {

            //1000毫秒倒计时一次
            @Override
            public void onTick(long millisUntilFinished) {
                binding.resend.setText(getString(R.string.resend_count, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                binding.resend.setText(R.string.resend);
                binding.resend.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //倒计时取消
        countDownTimer.onFinish();
    }
}