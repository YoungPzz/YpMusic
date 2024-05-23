package com.example.ypcloundmusic.adapter;


import androidx.viewpager.widget.ViewPager;

//因为当左右滑动切换viewpage的时候，想要下面的指示器同时跟着移动
//要在binding.list.addOnPageChangeListener里传入OnPageChangeListener接口的实例，这个接口要求必须实现三个方法
//有时候我们只需要其中一个，并且里面的方法可以通用
//所以创造一个接口的实现类
public class OnPageChangeListenerAdapter implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
