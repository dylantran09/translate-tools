package core.util.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import core.base.BaseActivity;

public class PermissionHelper {
    private static String TAG = PermissionHelper.class.getSimpleName();

    private static final int REQUEST_ALL = 5;
    private static final int REQUEST_APP_SETTING = 6;
    private static final String[] PERMISSIONS_ALL = {
            // Permissions for melonSDK
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            // Permissions for subscription
            Manifest.permission.SEND_SMS,
            // Permissions for OTP
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            // Permissions for TestFairy
//            Manifest.permission.READ_PHONE_STATE
    };

    private static final String[] PERMISSIONS_FOR_OTP = {
            // Permissions for OTP
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
    };

    private BaseActivity activity;
    private AlertDialog alertDialog;

    public PermissionHelper(BaseActivity activity) {
        this.activity = activity;
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //checkStoragePermission();
            if (!PermissionUtil.hasPermissions(activity, PERMISSIONS_ALL)) {
                Log.d(TAG, "All required Permission have not been granted.");
                return false;
            } else {
                Log.d(TAG, "All required Permission have been granted.");
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkPermissionForOtp() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!PermissionUtil.hasPermissions(activity, PERMISSIONS_FOR_OTP)) {
                Log.d(TAG, "Permission for receive and read sms have not been granted.");
                return false;
            } else {
                Log.d(TAG, "Permission for receive and read sms have been granted.");
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkPermissionReadSms() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!PermissionUtil.hasPermissions(activity, Manifest.permission.READ_SMS)) {
                Log.d(TAG, "Permission for read sms have not been granted.");
                return false;
            } else {
                Log.d(TAG, "Permission for read sms have been granted.");
                return true;
            }
        } else {
            return true;
        }
    }

    public void requestAllPermission() {
        if (PermissionUtil.shouldShowPermissionRationale(activity, PERMISSIONS_ALL)) {
            Log.d(TAG, "Displaying permission rationale");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setMessage("This application need all the required permission to run. Do you want to allow it now?")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ActivityCompat
                                            .requestPermissions(activity, PERMISSIONS_ALL,
                                                    REQUEST_ALL);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    activity.exitApp();
                                }
                            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_ALL, REQUEST_ALL);
        }
    }

    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ALL:
                Log.d(TAG, "Received response for All permission request.");
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Log.i(TAG, "All permissions were granted.");
                    return true;
                } else {
                    Log.i(TAG, "All permissions were NOT granted.");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setMessage("This application need all the required permission to run. Do you want to allow it now?")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            goToSettings();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            activity.exitApp();
                                        }
                                    });

                    alertDialog = alertDialogBuilder.create();
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return false;
                }
            default:
                return false;
        }
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myAppSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        myAppSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivityForResult(myAppSettings, REQUEST_APP_SETTING);
    }
}
