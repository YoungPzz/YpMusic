package com.example.ypcloundmusic.Model.response;


/**
 * 通用网络请求模型
 */
public class BaseResponse  {
    /**
     * 状态码
     * 等于0表示成功
     */

    private int status;

    /**
     * 出错的提示信息
     * 发送错误了不一定有
     */
    private String message;

    /**
     * 是否成功，给外界提供一个方法，不用去直接访问status
     */
    public boolean isSucceeded() {
        return status == 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
