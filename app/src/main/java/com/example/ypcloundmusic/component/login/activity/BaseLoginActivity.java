package com.example.ypcloundmusic.component.login.activity;

import static autodispose2.AutoDispose.autoDisposable;

import android.os.Build;
import android.text.TextUtils;

import com.example.ypcloundmusic.AppContext;
import com.example.ypcloundmusic.MainActivity;
import com.example.ypcloundmusic.Model.response.DetailResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.activity.BaseLogicActivity;
import com.example.ypcloundmusic.component.api.HttpObserver;
import com.example.ypcloundmusic.component.login.model.Session;
import com.example.ypcloundmusic.component.repository.DefaultRepository;
import com.example.ypcloundmusic.component.song.model.User;
import com.example.ypcloundmusic.util.MyActivityManager;
import com.example.ypcloundmusic.util.PreferenceUtil;
import com.example.ypcloundmusic.util.RegularUtil;
import com.example.ypcloundmusic.util.StringUtil;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import superui.text.toast.SuperToast;

public class BaseLoginActivity extends BaseLogicActivity {

    PreferenceUtil sp = PreferenceUtil.getInstance(this);

    /**
     * 登录
     *
     * @param user
     */
    protected void login(User user) {
        //设备名称
        user.setDevice(Build.DEVICE);

        //调用登录接口
        DefaultRepository.getInstance().login(user)
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new HttpObserver<DetailResponse<Session>>() {

                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {
                        //调用登录方法
                        onLogin(data.getData());
                    }

                    @Override
                    public boolean onFailed(DetailResponse<Session> data, Throwable e) {
                        SuperToast.show(R.string.error_password);
                        return super.onFailed(data, e);
                    }
                });
    }

    /**
     * 通过手机号和验证码登录
     * @param username
     * @param text
     */
    protected void login(String username, String text) {
        User user = new User();
        user.setPhone(username);
        user.setCode(text);

//        login(user);
    }

    /**
     * 保存登录信息
     *
     * @param data
     */
    private void onLogin(Session data) {
        //保存登录信息
        sp.setSession(data.getSession());
        sp.setUserId(data.getUserId());
        sp.setChatToken(data.getChatToken());

        //保存到appcontext
        AppContext.getInstance().onLogin(data);

        MyActivityManager.getInstance().finishToMain();


    }
}
