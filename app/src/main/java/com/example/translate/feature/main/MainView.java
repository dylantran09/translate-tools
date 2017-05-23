package com.example.translate.feature.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.translate.R;

import butterknife.ButterKnife;
import core.base.BaseActivity;

public class MainView extends LinearLayout {

    private BaseActivity activity;

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public MainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context) {
        if (context instanceof BaseActivity) {
            activity = (BaseActivity) context;
        }
        // Inflate menu layout
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_overlay, this, true);
        // Bind view into variables by ButterKnife
        ButterKnife.bind(this);
        // Bind view and assign behavior
        bindView();
    }

    private void bindView() {

    }
}
