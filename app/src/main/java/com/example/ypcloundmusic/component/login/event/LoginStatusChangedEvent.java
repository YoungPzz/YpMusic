package com.example.ypcloundmusic.component.login.event;


/**
 * 登录状态改变事件
 */
public class LoginStatusChangedEvent {
    /**
     * 是否登录了
     */
    private boolean isLogin;

    public LoginStatusChangedEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
