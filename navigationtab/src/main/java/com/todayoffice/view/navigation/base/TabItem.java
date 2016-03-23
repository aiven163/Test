package com.todayoffice.view.navigation.base;

import android.support.v4.app.Fragment;

/**
 * 项目：MyApplication
 * 创建者：Aiven
 * 时间：2016/3/23 14:12
 * 邮箱：aiven163@aliyun.com
 * 描述:
 */
public class TabItem {


    public TabItem(String title, int[] resId, int[] textColors, Fragment fragment) {
        this.title = title;
        this.resId = resId[0];
        this.selectedResId = resId[1];
        this.defaultTextColor = textColors[0];
        this.selectedTextColor = textColors[1];
        this.fragment = fragment;
    }

    public String title;
    public int resId;
    public int selectedResId;
    public int defaultTextColor;
    public int selectedTextColor;
    public Fragment fragment;
    public boolean fragmentIsShow;
}
