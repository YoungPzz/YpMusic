package com.example.ypcloundmusic.component.login.model;

import com.example.ypcloundmusic.Model.Base;

/**
 * 验证码请求参数模型
 */
public class CodeRequest extends Base {
    private String phone;
    private String email;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
