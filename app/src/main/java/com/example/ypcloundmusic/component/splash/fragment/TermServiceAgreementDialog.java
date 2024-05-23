package com.example.ypcloundmusic.component.splash.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.ypcloundmusic.Fragment.BaseDialogFragment;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.util.ScreenUtil;
import com.example.ypcloundmusic.util.SuperProcessUtil;
import com.google.android.material.button.MaterialButton;

public class TermServiceAgreementDialog extends BaseDialogFragment {

    private TextView contentView;
    private MaterialButton primaryButton;
    private Button disagreeButton ;
    private View.OnClickListener onClickListener;

    @Override
    protected void initViews() {
        setCancelable(false);
        contentView = findViewById(R.id.content);
        primaryButton = findViewById(R.id.primary);
        disagreeButton = findViewById(R.id.disagree);
    }

    @Override
    protected void initDatum() {
        //对content里的内容格式化，同时增加里面可点击的按钮
        Spanned content = Html.fromHtml(getString(R.string.term_service_privacy_content), 1);
        //用工具类，抽离这些操作
        //SuperTextUtil.setHtmlLinkClick(content,);
        //注释掉的这些操作，可以给里面的link设置点击事件
        contentView.setText(content);
    }

    @Override
    protected void initListeners() {
        disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();//关闭弹窗
                SuperProcessUtil.killApp();//杀死整个程序（因为是可以复用的，所以抽离出去）
            }
        });
        primaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onClickListener.onClick(v);
            }
        });
    }

    /*
    给外界一个接口，通过new Instance方法去创造一个TermServiceAgreementDialog
    */
    public static TermServiceAgreementDialog newInstance() {
        Bundle args = new Bundle();
        TermServiceAgreementDialog fragment = new TermServiceAgreementDialog();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param supportFragmentManager
     * @param onClickListener
     */
    //展示对话框
    public static void show(FragmentManager supportFragmentManager, View.OnClickListener onClickListener) {
        //1.创建fragment
        TermServiceAgreementDialog termServiceAgreementDialog = TermServiceAgreementDialog.newInstance();
        //2.Tag只是用来找fragment的
        termServiceAgreementDialog.show(supportFragmentManager, "TermServiceAgreementDialog");
        //保存传进来的监听器
        termServiceAgreementDialog.onClickListener = onClickListener;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frament_dialog_term_service, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //修改Dialog的宽度
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (ScreenUtil.getScreenWith(getContext()) * 0.9);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }
}
