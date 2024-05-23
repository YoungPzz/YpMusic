package com.example.ypcloundmusic.component.song.model;

public class User {
    private String id;
    private String nickname;
    private String icon;
    private int followingsCount;
    private int followersCount;

    private String phone;

    private String email;

    private String password;

    private String device;

    private String code;

    public User(String userId) {
        this.id = userId;
    }
    public User() {

    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(int followingsCount) {
        this.followingsCount = followingsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * 创建用户名登录参数
     * @param phone
     * @param email
     * @param password
     * @return
     */
    public static User createLogin(String phone, String email, String password) {
        User result = new User();
        result.setPhone(phone);
        result.setEmail(email);
        result.setPassword(password);
        return result;
    }
}
