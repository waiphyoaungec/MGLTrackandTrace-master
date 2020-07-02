package com.sh.mgltrackandtrace.core

import android.annotation.SuppressLint
import android.app.Application
import com.kirillr.strictmodehelper.kotlin.dsl.initStrictMode
import com.sh.mgltrackandtrace.BuildConfig


@SuppressLint("Registered")
class SampleApplicationKt : Application() {

    override fun onCreate() {
        super.onCreate()

        initStrictMode(enable = BuildConfig.DEVELOPER_MODE, enableDefaults = false) {
            threadPolicy {
                resourceMismatches = true
                customSlowCalls = true
                unbufferedIo = true

                penalty {
                    log = true
                }
            }

            vmPolicy {
                fileUriExposure = true
                leakedRegistrationObjects = true
                cleartextNetwork = true
                cleartextNetwork = true
                untaggedSockets = true
                contentUriWithoutPermission = true

                penalty {
                    log = true
                }
            }
        }
    }
}