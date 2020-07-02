package com.sh.mgltrackandtrace.view.activity


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.sh.mgltrackandtrace.R
import com.sh.mgltrackandtrace.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    var number : String = ""
    //var TAG = "RegisterActivity"
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbar.setTitle("Register")
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_white_36dp)
        setSupportActionBar(toolbar)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...") // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
        toolbar.setNavigationOnClickListener {
            finish()
        }
        //progress.visibility = View.GONE
        number = intent.getStringExtra("phone")
        btnConfirm.setOnClickListener {
            if (isNetworkAvailable()){
                when {
                    edtName.text.isEmpty() -> edtName.error = "Please enter name"
                    edtPassword.text!!.length < 6 -> edtPassword.error = "The password must be at least 6 characters."
                    edtConfirmPassword.text!!.length < 6 -> edtConfirmPassword.error = "The password confirmation must be at least 6 characters."
                    edtPassword.text!!.equals(edtConfirmPassword.text) -> edtConfirmPassword.error = "Please type same password"
                    else -> {
                        progressDialog.show()
                        //progress.visibility = View.VISIBLE
//                        btnConfirm.visibility = View.GONE
//                        edtName.visibility = View.GONE
//                        edtCompanyName.visibility = View.GONE
//                        edtAddress.visibility = View.GONE
//                        edtEmail.visibility = View.GONE
//                       // edtConfirmPassword.visibility = View.GONE
//                        edtPassword.visibility = View.GONE
                        val registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
                        registerViewModel.register(edtName.text.toString(), edtCompanyName.text.toString(), number, edtEmail.text.toString(), edtAddress.text.toString(), edtPassword.text.toString(), edtConfirmPassword.text.toString())
                        with(registerViewModel){
                            registerData.observe(this@RegisterActivity, Observer {
                                //Log.i(TAG, "${it?.response}")
                                //progress.visibility = View.GONE
                                try{
                                    if(it?.response == "success"){
                                        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString("phone", number).apply()
                                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        progressDialog.dismiss()
                                    }else{
                                        if(it?.message?.getPhone() != null){
                                            val text = it.message?.getPhone()!![0]
                                            btnConfirm.visibility = View.VISIBLE
                                            edtName.visibility = View.VISIBLE
                                            edtCompanyName.visibility = View.VISIBLE
                                            edtAddress.visibility = View.VISIBLE
                                            edtEmail.visibility = View.VISIBLE
                                            edtConfirmPassword.visibility = View.VISIBLE
                                            edtPassword.visibility = View.VISIBLE
                                            Toast.makeText(this@RegisterActivity, text, Toast.LENGTH_LONG).show()
                                            progressDialog.dismiss()
                                        }
                                    }
                                }catch (e:Exception){
                                    Log.d(TAG, "REGISTER"+e.toString())
                                    progressDialog.dismiss()
                                }

                            })
                        }
                    }
                }
            }else{
                showNetworkError("No Internet Connection")
            }

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

    override fun onResume() {
        super.onResume()

    }
}