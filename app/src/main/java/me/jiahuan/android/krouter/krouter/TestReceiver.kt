package me.jiahuan.android.krouter.krouter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import me.jiahuan.android.krouter.annotation.Route

@Route("/main/receiver")
class TestReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "TestReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "action = ${intent?.action}")
    }

}