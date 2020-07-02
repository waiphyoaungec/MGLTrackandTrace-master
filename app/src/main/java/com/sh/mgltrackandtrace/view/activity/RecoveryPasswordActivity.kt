package com.sh.mgltrackandtrace.view.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.sh.mgltrackandtrace.R

import com.sh.mgltrackandtrace.model.Result
import com.sh.mgltrackandtrace.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_recovery_password.*


class RecoveryPasswordActivity : AppCompatActivity() {
    lateinit var viewModel : RegisterViewModel
    var phno:String=""
    var password: String=""
    var paswordConfirmation: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password)
                toolbar.setTitle("ChangePassword")
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_white_36dp)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        phno = intent.getStringExtra("phone")!!
        var phoneno : String  = phno.removeRange(0,1)
        Log.d("test",phoneno)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        checkPassword()
        btnchange.setOnClickListener {
            viewModel.changePassword(phno, edtenterpassword.text.toString(), edtconfirmpassword.text.toString())
        }
        with(viewModel){
            changePasswordData.observe(this@RecoveryPasswordActivity, Observer {
                when(it){
                    is Result.Progress->{
                        progressBar.visibility=View.VISIBLE
                    }
                    is Result.Success->{
                        if(it.data.message.success!=null){
                            finish()
                        }else{
                            if(it.data.message.password!=null){
                                Toast.makeText(this@RecoveryPasswordActivity, it.data.message.password[0],Toast.LENGTH_SHORT).show()
                            }
                            if(it.data.message.password_confirm!=null){
                                Toast.makeText(this@RecoveryPasswordActivity, it.data.message.password_confirm[0],Toast.LENGTH_SHORT).show()
                            }
                            progressBar.visibility=View.GONE
                        }

                    }
                }
            })
        }
    }

    fun checkPassword(){
        edtconfirmpassword.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.toString().equals(edtenterpassword.text.toString())){
                    btnchange.visibility=View.VISIBLE
                    //Toast.makeText(this@RecoveryPasswordActivity, phno+"\n"+edtenterpassword.text.toString()+"\n"+edtconfirmpassword.text.toString(), Toast.LENGTH_SHORT).show()
                }else{
                    btnchange.visibility=View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onResume() {
        super.onResume()

    }
}
