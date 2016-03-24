package com.todayoffice.view.navigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.navigationtab.R;
import com.todayoffice.view.navigation.base.TabItem;
import com.todayoffice.view.navigation.base.TabItemView;
import com.todayoffice.view.navigation.util.NavigationUtil;

/**
 * 项目：MyApplication
 * 创建者：Aiven
 * 时间：2016/3/23 13:45
 * 邮箱：aiven163@aliyun.com
 * 描述: 底部Tab导航
 */
public class NavigationTab extends LinearLayout implements View.OnClickListener {
    private static final String STATE_CURRENT_SELECTED_TAB = "STATE_CURRENT_SELECTED_TAB";
    protected int mScreenSize;
    protected FragmentManager mFragmentManager;
    private int mFragmentContainerId;
    private SparseArray<TabItemView> tabItemViews;
    private View fragmentGroup;
    private ViewGroup rootViewGroup;
    private ViewGroup tabContainer;
    private boolean mIsComingFromRestoredState;
    private OnSelectedChangedListener mOnSelectedChangeListener;

    private int mCurrentIndex = -1;

    private NavigationTab() {
        this(null);
    }

    private NavigationTab(Context context) {
        this(context, null);
    }

    private NavigationTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        mScreenSize = NavigationUtil.getScreenWidth(context);
        View view = View.inflate(context, R.layout.tab_container, null);
        rootViewGroup = (ViewGroup) view.findViewById(R.id.rootContainer);
        tabContainer = (ViewGroup) view.findViewById(R.id.tabContainer);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, lp);
    }


    /**
     * 将底部导航添加到Activity的根布局中
     *
     * @param activity
     * @param savedInstanceState
     * @return
     */
    public static NavigationTab attachTo(@NonNull Activity activity, Bundle savedInstanceState) {
        NavigationTab navinTab = new NavigationTab(activity);
        navinTab.onRestoreInstanceState(savedInstanceState);
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View oldLayout = contentView.getChildAt(0);
        contentView.removeView(oldLayout);
        navinTab.setFragmentGroupLayout(oldLayout);
        contentView.addView(navinTab, 0);
        return navinTab;
    }

    /**
     * 重置状态
     *
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(STATE_CURRENT_SELECTED_TAB, -1);
            if (mCurrentIndex == -1) {
                mCurrentIndex = 0;
                Log.e("BottomBar", "You must override the Activity's onSave" +
                        "InstanceState(Bundle outState) and call BottomBar.onSaveInstanc" +
                        "eState(outState) there to restore the state properly.");
            }
            mIsComingFromRestoredState = true;
            setSelected(mCurrentIndex);
        }
    }

    /**
     * 设置Fragment父控件的根布局
     *
     * @param view
     */
    private void setFragmentGroupLayout(View view) {
        fragmentGroup = view;
    }


    /**
     * 添加Tab和对应的Fragment
     *
     * @param fragment
     * @param title     标题
     * @param resIds    <p>ICON资源数字，<b>注意：数组长度必须为2，第一个放普通状态的资源ID，第二个放选中状态的资源ID</b>
     * @param textColor <p>文字颜色数组，<b>注意：数组长度必须为2，第一个放普通状态的文字颜色值，第二个放选中状态的文字颜色值</b>
     * @return
     */
    public NavigationTab addFragments(@NonNull Fragment fragment, @NonNull String title, @NonNull int[] resIds, @NonNull int[] textColor) {
        if (tabItemViews == null) {
            tabItemViews = new SparseArray<>();
        }
        int size = tabItemViews.size();
        TabItem item = new TabItem(title, resIds, textColor, fragment);
        TabItemView itemView = new TabItemView(getContext(), item);
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, typedValue, true);
        int id = typedValue.resourceId;
        itemView.setBackgroundResource(id);
        itemView.setIndex(size);
        itemView.setOnClickListener(this);
        tabItemViews.put(size, itemView);
        return this;
    }

    /**
     * 提交Tab
     *
     * @param manager
     * @param containerId   <p>Fragment显示填充容器ID</p>
     * @param selectedIndex <p>初始化选中的索引，<b>注意:这个值在0到Tab的size之间</b></p>
     */
    public void commit(FragmentManager manager, int containerId, int selectedIndex) {
        this.commit(manager, containerId, selectedIndex, null);
    }

    public void commit(FragmentManager manager, int containerId, int selectedIndex, OnSelectedChangedListener listener) {
        this.mFragmentManager = manager;
        this.mCurrentIndex = selectedIndex;
        this.mFragmentContainerId = containerId;
        this.mOnSelectedChangeListener = listener;
        rootViewGroup.addView(fragmentGroup);
        initTabs();
        if (tabItemViews.size() > 0) {
            setSelected(selectedIndex);
        }
    }


    /**
     * 切换显示到第index个Tab
     *
     * @param index
     */
    public void setSelected(int index) {
        if (index != mCurrentIndex) {
            if (mOnSelectedChangeListener != null) {
                mOnSelectedChangeListener.onSelectedChanged(index);
            }
        }
        unSelected(mCurrentIndex);
        selected(index);
        updateFragment(index);
        mCurrentIndex = index;
        mIsComingFromRestoredState = false;

    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
        mCurrentIndex = -1;
    }

    /**
     * 清除上一次最后一个选中状态
     *
     * @param index
     */
    private void unSelected(int index) {
        if (index < 0 || index >= tabItemViews.size()) {
            new ArrayIndexOutOfBoundsException(index + " not between in 0 to tab's count !");
        }
        TabItemView itemView = tabItemViews.get(tabItemViews.keyAt(index));
        if (itemView != null) {
            itemView.setTabSelected(false);
        }
    }

    /**
     * 更新选中当前Tab状态
     *
     * @param index
     */
    private void selected(int index) {
        if (index < 0 || index >= tabItemViews.size()) {
            new ArrayIndexOutOfBoundsException(index + " not between in 0 to tab's count !");
        }
        TabItemView itemView = tabItemViews.get(tabItemViews.keyAt(index));
        if (itemView != null) {
            itemView.setTabSelected(true);
        }
    }

    /**
     * 初始化Tab，将所有的Tab加入到Tab容器
     */
    private void initTabs() {
        int size = tabItemViews.size();
        int width = Math.min(
                NavigationUtil.dpToPixel(getContext(), mScreenSize / size),
                NavigationUtil.dpToPixel(getContext(), 150)
        );
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < size; i++) {
            TabItemView view = tabItemViews.get(tabItemViews.keyAt(i));
            tabContainer.addView(view, lp);
            view.setGravity(Gravity.CENTER);
        }
    }


    /**
     * 更新当前应该显示的Fragment，先隐藏上一次最后显示的Fragment (调用hide())，如果当前索引的Fragment没有添加，则直添加，如果已经添加，则直接show()
     *
     * @param index 当前应显示的Fragment索引
     */
    private void updateFragment(int index) {
        if (index < 0 || index >= tabItemViews.size()) {
            new ArrayIndexOutOfBoundsException(index + " not between in 0 to tab's count !");
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        TabItemView view = tabItemViews.get(tabItemViews.keyAt(mCurrentIndex));
        TabItem tab = view.getTabItem();
        if (view != null && tab.fragment.isAdded() && tab.fragment != null && tab.fragmentIsShow) {
            transaction.hide(tab.fragment);
            tab.fragmentIsShow = false;
        }
        TabItemView updateView = tabItemViews.get(tabItemViews.keyAt(index));
        tab = updateView.getTabItem();
        if (updateView != null && tab.fragment != null && !tab.fragmentIsShow) {
            if (!tab.fragment.isAdded()) {
                transaction.add(mFragmentContainerId, tab.fragment, String.valueOf(updateView.index));
            } else {
                transaction.show(tab.fragment);
            }
            tab.fragmentIsShow = true;
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        TabItemView itemView = (TabItemView) v;
        setSelected(itemView.getIndex());
    }


    /**
     * 保存状态
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        if (mFragmentManager != null
                && mFragmentContainerId != 0
                && tabItemViews != null) {
            outState.putInt(STATE_CURRENT_SELECTED_TAB, mCurrentIndex);
            TabItemView view = tabItemViews.get(tabItemViews.keyAt(mCurrentIndex));
            if (view != null && view.getTabItem() != null) {
                Fragment bottomBarFragment = view.getTabItem().fragment;
                if (bottomBarFragment != null) {
                    bottomBarFragment.onSaveInstanceState(outState);
                }
            }
        }
    }


    /**
     * Tab切换监听器
     */
    public interface OnSelectedChangedListener {
        void onSelectedChanged(int index);
    }

}
