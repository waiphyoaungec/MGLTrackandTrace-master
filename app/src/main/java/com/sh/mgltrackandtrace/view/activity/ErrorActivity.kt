package com.sh.mgltrackandtrace.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitError
import com.sh.mgltrackandtrace.R


class ErrorActivity : AppCompatActivity() {
    internal val HELLO_TOKEN_ACTIVITY_ERROR_EXTRA = "HELLO_TOKEN_ACTIVITY_ERROR_EXTRA"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_error)
        val signOut = findViewById<Button>(R.id.log_out_button)
        signOut.setOnClickListener {
            AccountKit.logOut()
            finish()
        }

        val error = intent.getParcelableExtra<AccountKitError>(HELLO_TOKEN_ACTIVITY_ERROR_EXTRA)

        val errorView = findViewById<TextView>(R.id.error)
        if (errorView != null) {
            if (error != null) {
                errorView.text = error.toString()
            } else {
                errorView.setText(R.string.na)
            }
        }
    }
}