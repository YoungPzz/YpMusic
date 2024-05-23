package superui.text;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class SuperClickableSpan extends ClickableSpan {
    @Override
    public void onClick(@NonNull View widget) {

    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
