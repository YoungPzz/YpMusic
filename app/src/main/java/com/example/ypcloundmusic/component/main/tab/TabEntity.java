package com.example.ypcloundmusic.component.main.tab;

import com.flyco.tablayout.listener.CustomTabEntity;

//自定义指示器/table模型
public class TabEntity implements CustomTabEntity {

    private String title;
    private int selectedIcon;
    private int tabUnselectedIcon;

    public TabEntity(String title, int selectedIcon, int tabUnselectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.tabUnselectedIcon = tabUnselectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return tabUnselectedIcon;
    }
}
