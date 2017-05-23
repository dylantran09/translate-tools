package com.example.translate.util;

public class Action {

    // Service Actions
    private static final String SERVICE_PACKAGE = "com.example.translate.service.action";

    public static final String ACTION_START = SERVICE_PACKAGE + ".START";
    public static final String ACTION_STOP = SERVICE_PACKAGE + ".STOP";
    public static final String ACTION_EXPAND = SERVICE_PACKAGE + ".EXPAND";
    public static final String ACTION_COLLAPSE = SERVICE_PACKAGE + ".COLLAPSE";
    public static final String ACTION_SHOW = SERVICE_PACKAGE + ".SHOW";
    public static final String ACTION_HIDE = SERVICE_PACKAGE + ".HIDE";

    // Broadcast Receiver Actions
    private static final String RECEIVER_PACKAGE = "com.example.translate.receiver.action";

    public static final String ACTION_CLOSE_OVERLAY_VIEW = RECEIVER_PACKAGE + ".CLOSE_OVERLAY_VIEW";
    public static final String ACTION_CLOSE_MAIN_VIEW = RECEIVER_PACKAGE + ".CLOSE_MAIN_VIEW";
}
