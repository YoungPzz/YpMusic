package com.example.ypcloundmusic.component.api;

import android.view.View;

import com.example.ypcloundmusic.Model.response.BaseResponse;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.guide.GuideActivity;
import com.example.ypcloundmusic.util.HttpUtil;

import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Response;
import superui.text.loading.SuperRoundLoadingDialogFragment;

public abstract class HttpObserver<T> extends ObserverAdapter<T> {

    private  GuideActivity guideActivity = null;
    //判断是否显示loading框
    private boolean isShowLoading;
    /**
     * 无参构造方法
     */
    public HttpObserver(GuideActivity guideActivity, boolean isShowLoading) {
        this.guideActivity = guideActivity;
        this.isShowLoading = isShowLoading;
    }

    public HttpObserver() {

    }


    /**
     * 请求成功
     * 抽象方法，必须要重写
     * @param data
     */
    public abstract void onSucceeded(T data);

    /**
     * 请求失败
     *
     * @param data
     * @param e
     * @return true:自己处理；false:框架处理
     */
    public boolean onFailed(T data, Throwable e) {

        return false;
    }

    /**
     * 请求结束，成功失败都会调用（调用前调用），使用在这里隐藏加载提示
     * 隐藏对话框
     */
    public void onEnd() {
        if(isShowLoading){
            //隐藏对话框
            SuperRoundLoadingDialogFragment.fragment.dismiss();
            SuperRoundLoadingDialogFragment.fragment = null;
        }
    }

    /**
     * 开启网络请求前
     * @param d the {@link Disposable} instance whose {@link Disposable#dispose()} can
     * be called anytime to cancel the connection
     */
    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if(isShowLoading) {
            //显示加载对话框
            SuperRoundLoadingDialogFragment.show(guideActivity.getSupportFragmentManager(), R.string.loading ,new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    @Override
    public void onNext(T t) {
        super.onNext(t);

        onEnd();

        if (isSucceeded(t)) {
            //请求正常
            onSucceeded(t);
        } else {
            //请求出错了 业务错误 onError是系统错误
            handlerRequest(t, null);
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        onEnd();

        //处理错误
        handlerRequest(null, e);
    }

    /**
     * 网络请求是否成功了
     *
     * @param t
     * @return
     */
    private boolean isSucceeded(T t) {
        if (t instanceof Response) {
            //retrofit里面的响应对象

            //获取响应对象
            Response response = (Response) t;

            //获取响应码
            int code = response.code();

            //判断响应码
            if (code >= 200 && code <= 299) {
                //网络请求正常
                return true;
            }

        } else if (t instanceof BaseResponse) {
            //判断具体的业务请求是否成功
            BaseResponse response = (BaseResponse) t;

            return response.isSucceeded();
        }

        return false;
    }

    /**
     * 处理错误网络请求
     *
     * @param data
     * @param error
     */
    private void handlerRequest(T data, Throwable error) {
        if (onFailed(data, error)) {
            //回调了请求失败方法
            //并且该方法返回了true

            //返回true就表示外部手动处理错误
            //那我们框架内部就不用做任何事情了
        } else {
            HttpUtil.handlerRequest(data, error);
        }

    }

}
