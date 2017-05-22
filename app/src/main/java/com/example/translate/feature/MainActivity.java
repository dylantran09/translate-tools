package com.example.translate.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.translate.R;
import com.example.translate.feature.main.MainService;
import com.example.translate.util.MyUtils;

import butterknife.BindView;
import core.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_container)
    View mainContainer;
    @BindView(R.id.main_view)
    View mainView;
    @BindView(R.id.bt_open)
    FloatingActionButton btOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBaseCreate() {
        // do nothing
    }

    @Override
    public void onDeepLinking(Intent data) {
        // do nothing
    }

    @Override
    public void onNotification(Intent data) {
        // do nothing
    }

    @Override
    public void onBindView() {
        setupView();
        registerSingleAction(mainContainer, btOpen);
    }

    private void setupView() {
        mainView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        btOpen.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        // onResume
    }

    @Override
    public void onBaseFree() {
        // onStop
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.main_container:
                finish();
                break;
            case R.id.bt_open:
                Intent service = new Intent(this, MainService.class);
                startService(service);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }

    public int getOverlayViewHeight() {
        return MyUtils.getScreenHeight(MainActivity.this)
                - (MyUtils.getStatusBarHeight(MainActivity.this) + getResources().getDimensionPixelOffset(R.dimen.overlay_button_height));
    }
}
