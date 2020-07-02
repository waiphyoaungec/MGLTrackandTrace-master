package com.sh.mgltrackandtrace.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sh.mgltrackandtrace.R

import com.sh.mgltrackandtrace.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register_reset_page.*

class RegisterResetPage : AppCompatActivity() {
    val registerViewModel : RegisterViewModel by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }
    val token = "esEkWfVXsqbZW9e2ifWY9utevpZYCEZfcZakVZnB34M2Tl41K_egKREmTTBgmGGr"
    lateinit var progressDialog : ProgressDialog
    lateinit var phone : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_reset_page)

        val title = intent.getStringExtra("s")

        setSupportActionBar(phonetool)
        phonetool.title = title
        phonetool.navigationIcon = getDrawable(R.drawable.outline_arrow_back_white_36dp);
        phonetool.setNavigationOnClickListener {
            finish()
        }

        progressDialog = ProgressDialog(this)


        registerViewModel.optresponse.observe(this, Observer {
            if (it.status) {
                progressDialog.dismiss()
               val intent = Intent(this,ConfirmActivity::class.java)
                intent.putExtra("request_id",it.request_id)
                intent.putExtra("title",title)
                intent.putExtra("phone","09$phone")
                startActivity(intent)
                finish()
            }
            else{
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"error",Toast.LENGTH_SHORT).show()
            }
        })

        confirm.setOnClickListener {
            if(codeno.text.toString().trim().isEmpty()){
                Toast.makeText(applicationContext,"Enter your Phone Number",Toast.LENGTH_SHORT).show()
            }
            else {
                progressDialog.show()
                 phone = codeno.text.toString()
                registerViewModel.getOtp(token, "09$phone", "MGL Express", 5, "Wai Phyo Aung")

            }

        }


    }
}
