package superui.text.loading;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.example.ypcloundmusic.Fragment.BaseDialogFragment;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.splash.fragment.TermServiceAgreementDialog;
import com.example.ypcloundmusic.util.Constant;

public class SuperRoundLoadingDialogFragment extends BaseDialogFragment implements DialogInterface.OnKeyListener {
    public static SuperRoundLoadingDialogFragment fragment;
    private View.OnClickListener onClickListener;
    private TextView textView;

    @Override
    protected void initViews() {
        super.initViews();
        textView = findViewById(R.id.loading);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        int messageInt = getArguments().getInt(Constant.ID);
        textView.setText(getString(messageInt));
    }

    @Override
    protected void initListeners() {
        super.initListeners();
    }

    //展示对话框
    public static void show(FragmentManager supportFragmentManager, @StringRes int message, View.OnClickListener onClickListener) {
        //1.创建fragment
        SuperRoundLoadingDialogFragment superRoundLoadingDialogFragment = SuperRoundLoadingDialogFragment.newInstance(message);
        //2.Tag只是用来找fragment的
        superRoundLoadingDialogFragment.show(supportFragmentManager, "SuperRoundLoadingDialogFragment");
        //保存传进来的监听器
        superRoundLoadingDialogFragment.onClickListener = onClickListener;
    }

    public static SuperRoundLoadingDialogFragment newInstance(int message) {
        Bundle args = new Bundle();
        //Bundle用于传递数据
        args.putInt(Constant.ID, message);
        fragment = new SuperRoundLoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //为DialogFragment里的方法
        Dialog dialog = getDialog();

        //外界触摸不能停
        dialog.setCanceledOnTouchOutside(false);


        //不响应左下角返回键
        dialog.setOnKeyListener(this);

        return inflater.inflate(R.layout.super_round_dialog_loading, container, false);
    }

    //使得loading框在转的时候，点击手机的返回键没反应
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            return true;//拦截返回键
        }
        return false;
    }
}
