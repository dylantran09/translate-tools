package com.example.translate.feature;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.translate.R;
import com.example.translate.service.MainService;
import com.example.translate.util.Action;

import butterknife.BindView;
import core.base.BaseActivity;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_view)
    View mainView;
    @BindView(R.id.bt_open)
    FloatingActionButton btOpen;

    private BroadcastReceiver mainReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBaseCreate() {
        // create Receiver
        mainReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Action.ACTION_CLOSE_OVERLAY_VIEW:
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
        registerSingleAction(btOpen);
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
        startMainService(Action.ACTION_HIDE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // onStop
        if (mainReceiver != null) {
            unregisterReceiver(mainReceiver);
        }
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                if (checkDrawOverlayPermission()) {
                    startMainService(Action.ACTION_START);
                    finish();
                }
                break;
        }
    }

    private void startMainService(String action) {
        Intent service = new Intent(this, MainService.class);
        if (action != null) {
            service.setAction(action);
        }
        startService(service);
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 139);
            return false;
        } else {
            return true;
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 139) {
            if (Settings.canDrawOverlays(this)) {
                startMainService(Action.ACTION_START);
                finish();
            }
        }
    }
}
