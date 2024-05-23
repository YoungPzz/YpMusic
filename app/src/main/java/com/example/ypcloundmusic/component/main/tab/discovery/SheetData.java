package com.example.ypcloundmusic.component.main.tab.discovery;

import com.example.ypcloundmusic.component.sheet.model.Sheet;
import com.example.ypcloundmusic.util.Constant;

import java.util.List;

public class SheetData implements BaseMultiItemEntity{

    private List<Sheet> data;

    public SheetData(List<Sheet> data) {
        this.data = data;
    }

    public List<Sheet> getData() {
        return data;
    }

    public void setData(List<Sheet> data) {
        this.data = data;
    }

    @Override
    public int getItemType() {
        return Constant.STYLE_SHEET;
    }
}
