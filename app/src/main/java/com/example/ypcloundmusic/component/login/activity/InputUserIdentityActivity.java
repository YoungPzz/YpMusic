package com.example.ypcloundmusic.component.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.databinding.ActivityPhoneLoginBinding;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.RegularUtil;

import org.apache.commons.lang3.StringUtils;

import superui.text.toast.SuperToast;

public class InputUserIdentityActivity extends BaseLogicActivity {
    private ActivityPhoneLoginBinding binding;
    private int style;

    /**
     * 手机号登录
     *
     * @param context
     */
    public static void startWithPhoneLogin(Context context) {
        start(context, Constant.STYLE_PHONE_LOGIN);
    }

    /**
     * 忘记密码
     *
     * @param context
     */
    public static void startWithForgotPassword(Context context) {
        start(context, Constant.STYLE_FORGOT_PASSWORD);
    }

    private static void start(Context context, int style) {
        Intent intent = new Intent(context, InputUserIdentityActivity.class);
        intent.putExtra("Style", style);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /**
     * 初始化
     */
    @Override
    protected void initDatum() {
        super.initDatum();
        binding.username.setText("15218211218");
        style = getIntent().getIntExtra("Style", Constant.STYLE_PHONE_LOGIN);
        switch (style) {
            case Constant.STYLE_PHONE_LOGIN:
                binding.username.setHint(R.string.enter_phone);
                binding.username.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                break;

            case Constant.STYLE_FORGOT_PASSWORD:
                binding.username.setHint(R.string.enter_phone_or_email);
                break;
        }
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.primary.setEnabled(StringUtils.isNotBlank(s.toString().trim()));
            }
        });
        binding.primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.username.getText().toString().trim();
                boolean isPhone = RegularUtil.isPhone(content);
                String username = binding.username.getText().toString().trim();
                if (isPhone || RegularUtil.isEmail(content)) {
                    if (style == Constant.STYLE_PHONE_LOGIN)
                        InputCodeActivity.startWithPhoneLogin(InputUserIdentityActivity.this, username);
                    else
                        InputCodeActivity.startWithForgotPassword(InputUserIdentityActivity.this, username);

                } else {
                    SuperToast.show(R.string.enter_phone_or_email);
                }
            }
        });
    }


}