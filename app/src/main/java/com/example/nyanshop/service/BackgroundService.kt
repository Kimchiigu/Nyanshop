package com.example.nyanshop.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BackgroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d("BackgroundService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        Thread {
            for (i in 1..5) {
                Log.d("BackgroundService", "Running Operation $i")
                Thread.sleep(1000)
            }
            stopSelf()
        }.start()

// threadnya bakal diulang kalau ada yang mau stop threadnya secara paksa
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
//        misalnya kita punya 2 service yang berjalan berbarengan,
//        terus mau komunikasi pakenya intent
//        tapi krn kita gk sekompleks itu, kita bisa cuekin dulu
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BackgroundService", "Service Destroyed")
    }
}