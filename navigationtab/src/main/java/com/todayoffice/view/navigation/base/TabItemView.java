package com.todayoffice.view.navigation.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.navigationtab.R;

/**
 * 项目：MyApplication
 * 创建者：Aiven
 * 时间：2016/3/23 14:15
 * 邮箱：aiven163@aliyun.com
 * 描述:
 */
public class TabItemView extends LinearLayout {

    private TextView mTitleTv;
    private ImageView mIconIv;

    public TabItemView(Context context, TabItem item) {
        this(context, null, item);
    }

    public TabItemView(Context context, AttributeSet attrs, TabItem item) {
        this(context, attrs, 0, item);
    }

    public TabItemView(Context context, AttributeSet attrs, int defStyleAttr, TabItem item) {
        super(context, attrs, defStyleAttr);
        this.item = item;
        setOrientation(LinearLayout.VERTICAL);
        init();
    }

    private TabItem item;
    public int index;
    public boolean tabSelected;

    private void init() {
        View view =inflate(getContext(),R.layout.view_tab_item, this);
        mTitleTv = (TextView) view.findViewById(R.id.titleTv);
        mIconIv = (ImageView) view.findViewById(R.id.tagIv);
        update();
    }

    private void update() {
        mTitleTv.setText(item.title);
        if (tabSelected) {
            mIconIv.setImageResource(item.selectedResId);
            mTitleTv.setTextColor(item.selectedTextColor);
        } else {
            mIconIv.setImageResource(item.resId);
            mTitleTv.setTextColor(item.defaultTextColor);
        }
    }

    public boolean isTabSelected() {
        return tabSelected;
    }

    public void setTabSelected(boolean tabSelected) {
        this.tabSelected = tabSelected;
        update();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TabItem getTabItem() {
        return item;
    }
}
