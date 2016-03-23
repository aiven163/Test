package com.example.administrator.myapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todayoffice.main.R;

/**
 * 项目：MyApplication
 * 创建者：Aiven
 * 时间：2016/3/23 10:21
 * 邮箱：aiven163@aliyun.com
 * 描述:
 */
public class MyFragment extends Fragment {
    View rootView;
    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.myfragment, null);
            Log.e("TAG", "onCreate......1");
            tv = (TextView) rootView.findViewById(R.id.tv);
            tv.setText("fragment 1");
        }
        return rootView;
    }



}
