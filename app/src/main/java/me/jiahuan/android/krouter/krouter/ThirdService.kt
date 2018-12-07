package me.jiahuan.android.krouter.krouter

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import me.jiahuan.android.krouter.annotation.Route

@Route(path = "/main/third")
class ThirdService : Service() {

    companion object {
        private const val TAG = "ThirdService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}