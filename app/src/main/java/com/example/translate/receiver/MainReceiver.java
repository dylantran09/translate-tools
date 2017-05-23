package com.example.translate.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        switch (intent.getAction()) {

        }
    }
}
