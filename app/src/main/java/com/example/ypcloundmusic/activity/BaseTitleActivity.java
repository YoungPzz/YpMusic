package com.example.ypcloundmusic.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.example.ypcloundmusic.component.login.activity.BaseLoginActivity;

/*
    通用标题
 */
public class BaseTitleActivity<VB extends ViewBinding> extends BaseLoginActivity {
    @Override
    protected void initViews() {
        super.initViews();

    }

    /**
     * 按钮点击事件
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //返回按钮的id
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
