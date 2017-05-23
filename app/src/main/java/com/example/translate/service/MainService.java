package com.example.translate.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.example.translate.R;
import com.example.translate.feature.OverlayActivity;
import com.example.translate.util.Action;
import com.example.translate.util.MyUtils;

import core.util.DLog;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

public class MainService extends Service {
    public static final String TAG = MainService.class.getSimpleName();

    private final IBinder iBinder = new LocalBinder();

    private WindowManager windowManager;

    private View container;
    private ImageView overlayButton;

    private GestureDetector gestureDetector;

    private int statusBarHeight = 0;

    private int overlayButtonWidth;
    private int overlayButtonHeight;

    private int lastX = -1;
    private int lastY = -1;

    private int savedX = -1;
    private int savedY = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.d(TAG, "onCreate");
        init();
        initView();
        setAction();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            DLog.d(TAG, "onStartCommand with " + action);
            switch (action) {
                case Action.ACTION_START:
                    processStart();
                    break;
                case Action.ACTION_STOP:
                    processStop();
                    break;
                case Action.ACTION_EXPAND:
                    processExpand();
                    break;
                case Action.ACTION_COLLAPSE:
                    processCollapse();
                    break;
                case Action.ACTION_SHOW:
                    processShow();
                    break;
                case Action.ACTION_HIDE:
                    processHide();
                    break;
            }
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    //binder
    public class LocalBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    private void init() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        statusBarHeight = MyUtils.getStatusBarHeight(MainService.this);

        overlayButtonWidth = getResources().getDimensionPixelOffset(R.dimen.overlay_button_width);
        overlayButtonHeight = getResources().getDimensionPixelOffset(R.dimen.overlay_button_height);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        container = inflater.inflate(R.layout.layout_overlay_icon, null);
        overlayButton = (ImageView) container.findViewById(R.id.bt_overlay);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = statusBarHeight;
        windowManager.addView(container, params);
    }

    private void setAction() {
        gestureDetector = new GestureDetector(MainService.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                savedX = (int) e.getRawX();
                savedY = (int) e.getRawY();

                LayoutParams params = (LayoutParams) container.getLayoutParams();
                params.x = 0;
                params.y = statusBarHeight;
                windowManager.updateViewLayout(container, params);
                openOverlayView();
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (container == null) {
                    return false;
                }
                LayoutParams params = (LayoutParams) container.getLayoutParams();

                lastX = (int) e2.getRawX();
                lastY = (int) e2.getRawY();

                params.x = lastX - overlayButtonWidth / 2;
                params.y = lastY - overlayButtonHeight / 2 - statusBarHeight;
                windowManager.updateViewLayout(container, params);

                // Collapse Overlay View when moving
                //processCollapse();
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        overlayButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void processStart() {
        // TODO
    }

    private void processStop() {
        // TODO
        stopSelf();
    }

    private void processExpand() {
        Intent intent = new Intent(this, OverlayActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK
                | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void processCollapse() {
        Intent intent = new Intent();
        intent.setAction(Action.ACTION_CLOSE_OVERLAY_VIEW);
        sendBroadcast(intent);
        resetLastState();
    }

    private void processShow() {
        processCollapse();
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        }
    }

    private void processHide() {
        if (container != null) {
            container.setVisibility(View.GONE);
        }
        processStop();
    }

    private void openOverlayView() {
        Intent intent = new Intent(this, OverlayActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK
                        | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void resetLastState() {
        if (savedX >= 0 && savedY > 0) {
            LayoutParams params = (LayoutParams) container.getLayoutParams();
            params.x = savedX - overlayButtonWidth / 2;
            params.y = savedY - overlayButtonHeight / 2 - statusBarHeight;
            windowManager.updateViewLayout(container, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (container != null) {
            windowManager.removeView(container);
            container = null;
        }
    }
}
