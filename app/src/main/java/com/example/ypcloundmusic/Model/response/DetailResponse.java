package com.example.ypcloundmusic.Model.response;


/**
 * 详情网络请求解析类
 * 继承自BaseResponse
 * @param <T>
 */
public class DetailResponse<T> extends BaseResponse{
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
