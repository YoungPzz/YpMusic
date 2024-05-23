package com.example.ypcloundmusic.component.sheet.model;


import com.example.ypcloundmusic.Model.response.Meta;

/**
 * 用来解析歌单列表数据
 */
public class SheetWrapper {
    /**
     * 状态码
     */
    public int status;

    /**
     * data数据
     */
    public Meta data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Meta getData() {
        return data;
    }

    public void setData(Meta data) {
        this.data = data;
    }
}
