package com.sh.mgltrackandtrace.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.sh.mgltrackandtrace.R

import com.sh.mgltrackandtrace.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_confirm.*

class ConfirmActivity : AppCompatActivity() {
    private val registerViewModel : RegisterViewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }
    private val token = "esEkWfVXsqbZW9e2ifWY9utevpZYCEZfcZakVZnB34M2Tl41K_egKREmTTBgmGGr"
    lateinit var progressDilog : ProgressDialog
    lateinit var phone : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val title = intent.getStringExtra("title")
        val request_id = intent.getIntExtra("request_id",0)
        phone = intent.getStringExtra("phone")!!

        setSupportActionBar(confirm_tool)
        confirm_tool.title = title
        confirm_tool.navigationIcon = getDrawable(R.drawable.outline_arrow_back_white_36dp)
        confirm_tool.setNavigationOnClickListener {
            finish()
        }
        progressDilog = ProgressDialog(this)
        registerViewModel.confirmresponse.observe(this, Observer {
            if(it.status){
               progressDilog.dismiss()
                if(title.equals("register")){
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.putExtra("phone",phone)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, RecoveryPasswordActivity::class.java)
                    intent.putExtra("phone",phone)
                    startActivity(intent)

                }
                finish();
            }
            else{
                progressDilog.dismiss()
                Toast.makeText(applicationContext,"error",Toast.LENGTH_SHORT).show()
            }
        })
        confirm.setOnClickListener {
            if(codeno.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please Enter Code Number",Toast.LENGTH_SHORT).show()

            }
            else{
                progressDilog.show()
                registerViewModel.checkOtp(token,request_id,codeno.text.toString().trim())
            }
        }

    }
}
