package com.sh.mgltrackandtrace.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.sh.mgltrackandtrace.R


import com.sh.mgltrackandtrace.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val FRAMEWORK_REQUEST_CODE = 1
    private val TAG : String = "MainActivity"
    private var toastMessage : String = ""
    private var nextPermissionsRequestCode = 4000
    private val permissionsListeners = HashMap<Int, OnCompleteListener>()
    private var type: String?=""
    private var phno: String?=""
    lateinit var progressDialog: ProgressDialog

    private interface OnCompleteListener {
        fun onComplete()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginProgress.visibility = View.GONE
        edtPhone.setSelection(edtPhone.text.length)
        progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.setMessage("Loading...") // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner

        btnLogin.setOnClickListener {
            if (isNetworkAvailable()){
                when {
                    edtPhone.text.isEmpty() -> edtPhone.error = "Please enter phone number!"
                    edtPassword.text.isEmpty() -> edtPassword.error = "Please type password!"
                    else -> {
                        //loginProgress.visibility = View.VISIBLE
                        progressDialog.show()
//                        btnLogin.visibility = View.GONE
//                        edtPassword.visibility = View.GONE
//                        edtPhone.visibility = View.GONE
//                        txtRegister.visibility = View.GONE
//                        txtforgotpassword.visibility = View.GONE
                        //txtPhone.visibility = View.GONE
                        val registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
                        phno = "${edtPhone.text.toString()}"
                        registerViewModel.login(phno!!, edtPassword.text.toString())
                        with(registerViewModel){
                            loginData.observe(this@LoginActivity, Observer {
                                loginProgress.visibility = View.GONE
                                if (it?.response == "login success") {
                                    progressDialog.dismiss()
                                    PreferenceManager.getDefaultSharedPreferences(this@LoginActivity).edit()
                                        .putString("phone", it.message.phone).apply()
                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    progressDialog.dismiss()
//                                    btnLogin.visibility = View.VISIBLE
//                                    edtPassword.visibility = View.VISIBLE
//                                    edtPhone.visibility = View.VISIBLE
//                                    txtRegister.visibility = View.VISIBLE
                                    //txtPhone.visibility = View.VISIBLE
                                    //txtforgotpassword.visibility = View.VISIBLE
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Phone number or Password is invalid",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                        }
                    }
                }
            }else{
                progressDialog.dismiss()
                showNetworkError("No Internet Connection")
            }
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this,RegisterResetPage::class.java)
            intent.putExtra("s","register")
            startActivity(intent)


        }
        txtforgotpassword.setOnClickListener {
            val intent = Intent(this,RegisterResetPage::class.java)
            intent.putExtra("s","reset")
            startActivity(intent)

        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    private fun showNetworkError(des : String){
        Toast.makeText(this, des, Toast.LENGTH_LONG).show()
    }

    fun onLogins(loginType : LoginType){
        //loginProgress.visibility=View.VISIBLE
        progressDialog.show()
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



                        }
                    }
                }
                if (configuration.isReadPhoneStateEnabled && !isGooglePlayServicesAvailable()) {
                    val readPhoneStateCompleteListener = completeListener
                    completeListener = object : OnCompleteListener {
                        override fun onComplete() {


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




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        progressDialog.dismiss()
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

                //this.finish()
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
}