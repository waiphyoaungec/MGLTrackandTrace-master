package com.sh.mgltrackandtrace.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName







class MessageList{
    @SerializedName("success")
    @Expose
    private var success: List<String>? = null

    @SerializedName("phone")
    @Expose
    private var phone: List<String>? = null

    @SerializedName("name")
    @Expose
    private var name: List<String>? = null
    @SerializedName("password")
    @Expose
    private var password: List<String>? = null
    @SerializedName("password_confirmation")
    @Expose
    private var passwordConfirmation: List<String>? = null

    fun getPhone(): List<String>? {
        return phone
    }

    fun setPhone(phone: List<String>) {
        this.phone = phone
    }

    fun getSuccess(): List<String>? {
        return success
    }

    fun setSuccess(success: List<String>) {
        this.success = success
    }
    fun getName(): List<String> {
        return name!!
    }

    fun setName(name: List<String>) {
        this.name = name
    }

    fun getPassword(): List<String> {
        return password!!
    }

    fun setPassword(password: List<String>) {
        this.password = password
    }

    fun getPasswordConfirmation(): List<String> {
        return passwordConfirmation!!
    }

    fun setPasswordConfirmation(passwordConfirmation: List<String>) {
        this.passwordConfirmation = passwordConfirmation
    }

}