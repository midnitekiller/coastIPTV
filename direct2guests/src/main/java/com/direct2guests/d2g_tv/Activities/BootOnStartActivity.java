package com.direct2guests.d2g_tv.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.direct2guests.d2g_tv.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class BootOnStartActivity extends BroadcastReceiver{



//    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

//
//            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
//            Bundle params = new Bundle();
//            params.putString("app_name", String.valueOf(R.string.app_name));
//            params.putString("app_version", "v1.0");
//            mFirebaseAnalytics.logEvent("app_open", params);

        }
    }

}
