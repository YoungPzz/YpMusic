package com.example.ypcloundmusic.component.login.model;
public class Session {
    /**
     * 用户Id
     */
    private String userId;

    /**
     * 登录后的Session
     */
    private String session;

    /**
     * 聊天token
     */
    private String chatToken;

    public Session(String userId, String session, String chatToken) {
        this.userId = userId;
        this.session = session;
        this.chatToken = chatToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }
}
