package com.example.translate.feature;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import core.base.BaseActivity;
import core.util.permission.PermissionHelper;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionHelper = new PermissionHelper(this);
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
        // do nothing
    }

    @Override
    public void onInitializeViewData() {
        // do nothing
    }

    @Override
    public void onBaseResume() {
        // do nothing
    }

    @Override
    public void onBaseFree() {
        // do nothing
    }

    @Override
    public void onSingleClick(View v) {
        // do nothing
    }

    @Override
    protected void onStart() {
        if (!permissionHelper.checkPermission()) {
            permissionHelper.requestAllPermission();
        } else {
            goToNextScreen();
        }
        super.onStart();
    }

    private void goToNextScreen() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
