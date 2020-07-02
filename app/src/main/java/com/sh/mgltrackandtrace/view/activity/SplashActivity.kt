package com.sh.mgltrackandtrace.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kirillr.strictmodehelper.StrictModeCompat
import com.sh.mgltrackandtrace.R


import me.myatminsoe.mdetect.MDetect

class SplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        StrictModeCompat.noteSlowCall("Sample")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        MDetect.init(this)


        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val settings = PreferenceManager.getDefaultSharedPreferences(this)
            val phone = settings.getString("phone", "")
            if (phone == null || phone == "") {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }







}