package com.sh.mgltrackandtrace.api

import com.sh.mgltrackandtrace.model.*
import com.sh.mgltrackandtrace.model.otpresponse.ConfirmResponse
import com.sh.mgltrackandtrace.model.otpresponse.OtpResponse
import retrofit2.Call
import retrofit2.http.*

interface API_Connect {
    @GET("wp-json/wp/v2/posts")
    fun getPromotions(@Query("per_page")  per_page: String,
                      @Query("page") page: Int): Call<MutableList<DataItem>>

    @GET("api/waybill_trackingsearch")
    fun getWaybillTracking(@Query("code")  code: String): Call<TrackingResponse>

    @FormUrlEncoded
    @POST("api/cusapp_user/cusappregister")
    fun register(@Field("name") name : String,
                 @Field("company_name") company_name : String,
                 @Field("phone") phone : String,
                 @Field("email") email : String,
                 @Field("address") address : String,
                 @Field("password")  password : String,
                 @Field("password_confirmation") password_confirmation : String) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("api/cusapp_user/login")
    fun login(@Field("phone") phone : String,
              @Field("password") password : String) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/cusapp_user/forget_psw")
    fun resetPassword(@Field("phone")phone: String,
                      @Field("password") password: String,
                      @Field("password_confirmation") confirm: String): Call<ChangeResponse>
    @POST("api/v1/request")
    fun getOtp(@Query("access-token") token : String,
               @Query("number") phone : String,
               @Query("brand_name") brand : String,
               @Query("code_length") code : Int,
               @Query("numbersender_name") name : String
               ) : Call<OtpResponse>

    @POST("api/v1/verify")
    fun confirmOTP(
        @Query("access-token") token : String,
        @Query("request_id") request_id : Int,
        @Query("code") code : String
    ) : Call<ConfirmResponse>
}