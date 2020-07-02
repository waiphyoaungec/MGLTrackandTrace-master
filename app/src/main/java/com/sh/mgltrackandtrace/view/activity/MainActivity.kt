package com.sh.mgltrackandtrace.view.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.sh.mgltrackandtrace.R


import java.util.*

class MainActivity : AppCompatActivity() {
    private val FRAMEWORK_REQUEST_CODE = 1
    private val TAG : String = "MainActivity"
    private var toastMessage : String = ""
    private var nextPermissionsRequestCode = 4000
    private val permissionsListeners = HashMap<Int, OnCompleteListener>()
    private var type: String?=""

    private interface OnCompleteListener {
        fun onComplete()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        type = intent.getStringExtra("type")
        //Log.i(TAG, "onCreate")
        onLogin(LoginType.PHONE)

        if (AccountKit.getCurrentAccessToken() != null && savedInstanceState == null) {
            //startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != FRAMEWORK_REQUEST_CODE) {
            return
        }

        //val toastMessage: String
        val loginResult = AccountKit.loginResultWithIntent(data)
        if (loginResult == null || loginResult.wasCancelled()) {
            toastMessage = "Login Cancelled"
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else if (loginResult.error != null) {
            //toastMessage = loginResult.error!!.errorType.message
            val intent = Intent(this, ErrorActivity::class.java)
            intent.putExtra("HELLO_TOKEN_ACTIVITY_ERROR_EXTRA", loginResult.error)
            startActivity(intent)
        } else {
            val accessToken = loginResult.accessToken
            //val tokenRefreshIntervalInSeconds = loginResult.tokenRefreshIntervalInSeconds
            if (accessToken != null) {
                /*toastMessage = ("Success:" + accessToken.accountId
                        + tokenRefreshIntervalInSeconds)*/
                if(type.equals("change")){
                    startActivity(Intent(this, RecoveryPasswordActivity::class.java))
                }else{
                    startActivity(Intent(this, RegisterActivity::class.java))
                }

                this.finish()
            } else {
                //toastMessage = "Unknown response type"
            }
        }

        /*Toast.makeText(
            this,
            toastMessage,
            Toast.LENGTH_LONG
        )
            .show()*/
    }

    fun onLogin(loginType : LoginType){
        Log.i(TAG, "login")
        val intent = Intent(this, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            loginType,
            AccountKitActivity.ResponseType.TOKEN
        )
        val configuration = configurationBuilder.build()
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configuration
        )
        var completeListener: OnCompleteListener = object :
            OnCompleteListener {
            override fun onComplete() {
                startActivityForResult(intent, FRAMEWORK_REQUEST_CODE)
            }
        }

        when (loginType) {
            LoginType.PHONE -> {
                if ( !canReadSmsWithoutPermission()) {
                    val receiveSMSCompleteListener = completeListener
                    completeListener = object : OnCompleteListener {
                        override fun onComplete() {
                            requestPermissions(
                                Manifest.permission.RECEIVE_SMS,
                                R.string.permissions_receive_sms_title,
                                R.string.permissions_receive_sms_title,
                                receiveSMSCompleteListener
                            )
                        }
                    }
                }
                if (configuration.isReadPhoneStateEnabled && !isGooglePlayServicesAvailable()) {
                    val readPhoneStateCompleteListener = completeListener
                    completeListener = object : OnCompleteListener {
                        override fun onComplete() {
                            requestPermissions(
                                Manifest.permission.READ_PHONE_STATE,
                                R.string.permissions_read_phone_state_message,
                                R.string.permissions_read_phone_state_message,
                                readPhoneStateCompleteListener
                            )
                        }
                    }
                }
            }
        }
        completeListener.onComplete()
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this)
        return googlePlayServicesAvailable == ConnectionResult.SUCCESS
    }

    private fun canReadSmsWithoutPermission(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this)
        return googlePlayServicesAvailable == ConnectionResult.SUCCESS
    }

    private fun requestPermissions(
        permission: String,
        rationaleTitleResourceId: Int,
        rationaleMessageResourceId: Int,
        listener: OnCompleteListener?
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener?.onComplete()
            return
        }

        checkRequestPermissions(
            permission,
            rationaleTitleResourceId,
            rationaleMessageResourceId,
            listener
        )
    }

    @TargetApi(23)
    private fun checkRequestPermissions(
        permission: String,
        rationaleTitleResourceId: Int,
        rationaleMessageResourceId: Int,
        listener: OnCompleteListener?
    ) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            listener?.onComplete()
            return
        }

        val requestCode = nextPermissionsRequestCode++
        permissionsListeners[requestCode] = listener!!

        if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(this)
                .setTitle(rationaleTitleResourceId)
                .setMessage(rationaleMessageResourceId)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ -> requestPermissions(arrayOf(permission), requestCode) }
                .setNegativeButton(android.R.string.no) { _, _ ->
                    // ignore and clean up the listener
                    permissionsListeners.remove(requestCode)
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        } else {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val permissionsListener = permissionsListeners.remove(requestCode)
        if (permissionsListener != null
            && grantResults.size > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            permissionsListener.onComplete()
        }
    }
}
