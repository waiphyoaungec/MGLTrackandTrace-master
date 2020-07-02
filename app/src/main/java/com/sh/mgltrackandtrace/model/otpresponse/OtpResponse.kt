package com.sh.mgltrackandtrace.model.otpresponse

data class OtpResponse(
    val number: String,
    val request_id: Int,
    val status: Boolean
)