package com.sh.mgltrackandtrace.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.facebook.accountkit.Account
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError
import com.sh.mgltrackandtrace.R


class TokenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_token)

        val signOut = findViewById(R.id.log_out_button) as Button
        signOut.setOnClickListener {
            AccountKit.logOut()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(account: Account) {
                val userId = findViewById(R.id.user_id) as TextView
                userId.text = account.id

                val phoneNumber = findViewById(R.id.user_phone) as TextView
                val number = account.phoneNumber
                phoneNumber.text = number?.toString()

                val email = findViewById(R.id.user_email) as TextView
                email.text = account.email
            }

            override fun onError(error: AccountKitError) {}
        })
    }
}