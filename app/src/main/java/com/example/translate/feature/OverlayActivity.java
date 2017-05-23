package com.example.translate.feature;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;

import com.example.translate.R;
import com.example.translate.receiver.MainReceiver;
import com.example.translate.service.MainService;
import com.example.translate.util.Action;
import com.example.translate.util.MyUtils;

import butterknife.BindView;
import core.base.BaseActivity;
import core.util.DLog;

public class OverlayActivity extends BaseActivity {
    public static final String TAG = OverlayActivity.class.getSimpleName();

    @BindView(R.id.overlay_main_container)
    View mainContainer;
    @BindView(R.id.main_view)
    View mainView;

    private BroadcastReceiver mainReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_overlay);
    }

    @Override
    public void onBaseCreate() {
        // create Receiver
        mainReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Action.ACTION_CLOSE_OVERLAY_VIEW:
                        collapseOverlay();
                        break;
                }
            }
        };
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
        registerSingleAction(mainContainer);
    }

    private void setupView() {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainView.getLayoutParams().height = getOverlayViewHeight();
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        // onResume
        if (mainReceiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Action.ACTION_CLOSE_OVERLAY_VIEW);
            registerReceiver(mainReceiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // onStop
        if (mainReceiver != null) {
            unregisterReceiver(mainReceiver);
        }
        collapseOverlay();
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.overlay_main_container:
                collapseOverlay();
                break;
        }
    }

    public int getOverlayViewHeight() {
        return MyUtils.getScreenHeight(OverlayActivity.this)
                - (2 * MyUtils.getStatusBarHeight(OverlayActivity.this) + getResources().getDimensionPixelOffset(R.dimen.overlay_button_height));
    }

    private void collapseOverlay() {
        finish();
        overridePendingTransition(0, 0);
    }
}
