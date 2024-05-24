package com.example.ypcloundmusic.component.feed.task;

import android.os.AsyncTask;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.ypcloundmusic.AppContext;
import com.example.ypcloundmusic.aliyun.AliyunStorage;
import com.example.ypcloundmusic.component.feed.model.Resource;
import com.example.ypcloundmusic.config.Config;
import com.example.ypcloundmusic.util.UUIDUtil;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//第二个是进度 第三个是结果
public class UploadFeedImageAsyncTask extends AsyncTask<List<LocalMedia>, Integer, Result<List<Resource>>> {
    @Override
    protected Result<List<Resource>> doInBackground(List<LocalMedia>... params) {
        //创建结果数组
        List<Resource> results = new ArrayList<>();

        //获取 oss 客户端
        OSSClient storage = AliyunStorage.getInstance(AppContext.getInstance()).getOssClient();

        List<LocalMedia> resources = params[0];

        LocalMedia it;
        try {
            for(int i = 0; i < resources.size(); i++){
                it = resources.get(i);

                //加上 DEV 的目的是方便清理数据
                String destFileName = String.format("%s%s.jpg", UUIDUtil.getUUID(), "DEV");

                //创建上传文件请求
                PutObjectRequest request = new PutObjectRequest(
                        Config.ALIYUN_OSS_BUCKET_NAME,
                        destFileName,
                        it.getCompressPath()
                );

                //上传
                PutObjectResult putObjectResult = storage.putObject(request);

                //如果上传成功
                //将文件名添加到结果集合里
                results.add(new Resource(destFileName));

                //更新进度
                publishProgress(i + 1);
            }

            return Result.success(results);

        } catch (Exception e) {
            return Result.error(e);
        }

    }
}
