package com.example.translate.feature;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import com.example.translate.R;
import com.example.translate.feature.main.MainService;
import com.example.translate.util.MyUtils;

import butterknife.BindView;
import core.base.BaseActivity;
import core.util.DLog;

public class OverlayActivity extends BaseActivity {
    public static final String TAG = OverlayActivity.class.getSimpleName();

    @BindView(R.id.main_container)
    View mainContainer;
    @BindView(R.id.main_view)
    View mainView;
    @BindView(R.id.bt_open)
    FloatingActionButton btOpen;

    private MainService mainService;
    private boolean isServiceBound;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DLog.d(TAG, "musicConnection");
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            //get service
            mainService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_overlay);
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
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainView.getLayoutParams().height = getOverlayViewHeight();
        btOpen.setVisibility(View.GONE);
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
                overridePendingTransition(0, 0);
                break;
            case R.id.bt_open:
                Intent service = new Intent(this, MainService.class);
                startService(service);
                bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
                finish();
                break;
        }
    }

    public int getOverlayViewHeight() {
        return MyUtils.getScreenHeight(OverlayActivity.this)
                - (2 * MyUtils.getStatusBarHeight(OverlayActivity.this) + getResources().getDimensionPixelOffset(R.dimen.overlay_button_height));
    }
}
