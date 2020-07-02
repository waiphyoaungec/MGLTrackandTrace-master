package com.sh.mgltrackandtrace.model

import com.google.gson.annotations.SerializedName

data class ChangeResponse(
    @SerializedName("response")
    var response: String,
    @SerializedName("message")
    var message: Msg
)

data class Msg(
    @SerializedName("password")
    var password: List<String>,
    @SerializedName("password_confirmation")
    var password_confirm : List<String>,
    @SerializedName("success")
    var success: List<String>
)