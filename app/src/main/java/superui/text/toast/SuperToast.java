package superui.text.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.example.ypcloundmusic.R;

public class SuperToast {


    private static Context context;
    private static LayoutInflater layoutInflater;

    //显然只需要全局执行一次，自然放到AppContext里
    public static void init(Context context){
        SuperToast.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 显示文本提示
     */
    public static void show(String content) {
        show(content, R.drawable.shape_toast_background, Toast.LENGTH_LONG);
    }

    /**
     * 传入资源文件
     *
     * @param content
     */
    public static void show(@StringRes int content) {
        show(context.getString(content), R.drawable.shape_toast_background, Toast.LENGTH_LONG);
    }

    /**
     * 错误提示
     *
     * @param content
     */
    public static void error(@StringRes int content) {
        show(context.getString(content), R.drawable.shape_toast_error_background, Toast.LENGTH_LONG);
    }

    /**
     * 错误提示
     *
     * @param content
     */
    public static void error(String content) {
        show(content, R.drawable.shape_toast_error_background, Toast.LENGTH_LONG);
    }

    /**
     * 成功提示
     *
     * @param content
     */
    public static void success(@StringRes int content) {
        show(context.getString(content), R.drawable.shape_toast_success_background, Toast.LENGTH_LONG);
    }
    public static void success(String content) {
        show(content, R.drawable.shape_toast_success_background, Toast.LENGTH_LONG);
    }

    private static void show(String content, @DrawableRes int background, int duration) {
        show(content, background, duration, Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 130);
    }


    /**
     *
     * @param content 显示的内容
     * @param background 背景
     * @param duration 时长
     * @param gravity 位置
     * @param xOffset x轴偏移
     * @param yOffset y轴偏移
     */

    public static void show(String content,@DrawableRes int background, int duration, int gravity, int xOffset, int yOffset){
        Toast toast = new Toast(SuperToast.context);
        //设置时长
        toast.setDuration(duration);

        //设置位置
        toast.setGravity(gravity, xOffset, yOffset);

        //加载布局
        View view = layoutInflater.inflate(R.layout.super_toast, null);
        View containerView = view.findViewById(R.id.container);

        //设置背景
        containerView.setBackgroundResource(background);

        //设置文字
        TextView testView = view.findViewById(R.id.content);
        testView.setText(content);

        //设置自定义布局
        toast.setView(view);

        //显示
        toast.show();

    }
}
