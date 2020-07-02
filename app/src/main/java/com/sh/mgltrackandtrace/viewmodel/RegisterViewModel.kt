package com.sh.mgltrackandtrace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.google.gson.Gson
import com.sh.mgltrackandtrace.api.API_Client
import com.sh.mgltrackandtrace.api.API_Connect
import com.sh.mgltrackandtrace.model.ChangeResponse
import com.sh.mgltrackandtrace.model.LoginResponse
import com.sh.mgltrackandtrace.model.RegisterResponse
import com.sh.mgltrackandtrace.model.Result
import com.sh.mgltrackandtrace.model.TrackingResponse
import com.sh.mgltrackandtrace.model.otpresponse.ConfirmResponse
import com.sh.mgltrackandtrace.model.otpresponse.OtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    val TAG = "RegisterViewModel"
    val loginData: SingleLiveEvent<LoginResponse> by lazy { SingleLiveEvent<LoginResponse>() }
    val registerData: SingleLiveEvent<RegisterResponse> by lazy { SingleLiveEvent<RegisterResponse>() }
    val checkpointData: MutableLiveData<TrackingResponse> by lazy { MutableLiveData<TrackingResponse>() }
    val changePasswordData: SingleLiveEvent<Result<ChangeResponse>> by lazy { SingleLiveEvent<Result<ChangeResponse>>() }
    val optresponse: SingleLiveEvent<OtpResponse> by lazy { SingleLiveEvent<OtpResponse>() }
    val confirmresponse : SingleLiveEvent<ConfirmResponse> by lazy { SingleLiveEvent<ConfirmResponse>() }

    fun login(phone: String, password: String) {
        val client = API_Client.getNewRetrofit().create(API_Connect::class.java)
        client.login(phone, password).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(
                    TAG,
                    "onFailure : " + t.message
                )
                Log.d("test", "Failure")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(
                    TAG,
                    "isSuccessful : " + response.isSuccessful
                )
                Log.d("test", "is successful")
                if (response.isSuccessful) {
                    if (response.isSuccessful) {
                        loginData.postValue(response.body())
                    }
                }
            }

        })
    }

    fun register(
        name: String,
        company_name: String,
        phone: String,
        email: String,
        address: String,
        password: String,
        password_confirmation: String
    ) {
        val client = API_Client.getNewRetrofit().create(API_Connect::class.java)
        client.register(name, company_name, phone, email, address, password, password_confirmation)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d(
                        TAG,
                        "onFailure : " + t.message
                    )
                }

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    Log.d(
                        TAG,
                        "isSuccessful : " + response.isSuccessful
                    )
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            registerData.postValue(response.body())
                        }
                    }
                }
            })
    }

    fun changePassword(phone: String, password: String, password_confirmation: String) {
        val client = API_Client.getNewRetrofit().create(API_Connect::class.java)
        changePasswordData.postValue(Result.loading(true))
        client.resetPassword(phone, password, password_confirmation)
            .enqueue(object : Callback<ChangeResponse> {
                override fun onFailure(call: Call<ChangeResponse>, t: Throwable) {
                    changePasswordData.postValue(Result.failure(t))
                    Log.d(TAG, "changePassword" + t.message)
                }

                override fun onResponse(
                    call: Call<ChangeResponse>,
                    response: Response<ChangeResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "changePassword" + Gson().toJson(response.body()))
                        changePasswordData.postValue(Result.success(response.body()!!))
                    }
                }
            })
    }

    fun getWaybillTracking(code: String) {
        val client = API_Client.getNewRetrofit().create(API_Connect::class.java)
        client.getWaybillTracking(code).enqueue(object : Callback<TrackingResponse> {
            override fun onFailure(call: Call<TrackingResponse>, t: Throwable) {
                Log.d(
                    TAG,
                    "onFailure : " + t.message
                )
                Log.d("test", "failure")
            }

            override fun onResponse(
                call: Call<TrackingResponse>,
                response: Response<TrackingResponse>
            ) {
                Log.d(
                    TAG,
                    "isSuccessful : " + response.isSuccessful
                )

                if (response.isSuccessful) {
                    Log.d("test", "successful")
                    if (response.isSuccessful) {
                        checkpointData.postValue(response.body())
                        Log.d("test", "${response.message()}")
                    }
                } else {
                    Log.d("test", "Not Successfully")
                }
            }

        })
    }

    fun getOtp(token: String, phone: String, company: String, code_length: Int, name: String) {
        val client = API_Client.getApiRetrofit().create(API_Connect::class.java)
        client.getOtp(token, phone, company, code_length, name)
            .enqueue(object : Callback<OtpResponse> {
                override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                    Log.d("test", "successfully")
                    optresponse.postValue(OtpResponse(phone,1,false))
                }

                override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                    if (response.isSuccessful)
                        optresponse.postValue(response.body())
                    else
                        optresponse.postValue(OtpResponse(phone,1,false))


                }

            })
    }
    fun checkOtp(token : String,id : Int,code : String){
        val client = API_Client.getApiRetrofit().create(API_Connect::class.java)
        client.confirmOTP(token,id,code)
            .enqueue(object : Callback<ConfirmResponse>{
                override fun onFailure(call: Call<ConfirmResponse>, t: Throwable) {
                    Log.d("test",t.localizedMessage.toString())
                    confirmresponse.postValue(ConfirmResponse(id,false))
                }

                override fun onResponse(
                    call: Call<ConfirmResponse>,
                    response: Response<ConfirmResponse>
                ) {
                    if(response.isSuccessful){
                        confirmresponse.postValue(response.body())
                    }
                    else{
                        confirmresponse.postValue(ConfirmResponse(id,false))
                    }
                }

            })
    }
}