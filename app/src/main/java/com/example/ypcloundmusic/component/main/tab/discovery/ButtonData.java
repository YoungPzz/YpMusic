package com.example.ypcloundmusic.component.main.tab.discovery;

import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.util.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * 发现界面快捷按钮数据
 */
public class ButtonData implements BaseMultiItemEntity{

    //理论上按钮数据不会变化 所以这样写
    private static ArrayList<IconTitleButtonData> results = new ArrayList<IconTitleButtonData>();;
    static {
        results.add(new IconTitleButtonData(R.drawable.day_recommend, R.string.day_recommend));
        results.add(new IconTitleButtonData(R.drawable.person_fm, R.string.person_fm));
        results.add(new IconTitleButtonData(R.drawable.sheet, R.string.sheet));
        results.add(new IconTitleButtonData(R.drawable.rank, R.string.rank));
        results.add(new IconTitleButtonData(R.drawable.button_live, R.string.live));
        results.add(new IconTitleButtonData(R.drawable.digital_album, R.string.digital_album));
        results.add(new IconTitleButtonData(R.drawable.digital_album, R.string.digital_album));
    }

    public List<IconTitleButtonData> getData(){
        return results;
    }


    //这里要返回一个常量 这样在显示内容的时候 才能知道显示哪一块内容
    @Override
    public int getItemType() {
        return Constant.STYLE_BUTTON;
    }
}
