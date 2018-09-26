package com.pb.leadmanagement.core.requestbuilders

import com.pb.leadmanagement.core.RetroRequestBuilder
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.requestentity.LoginRequestEntity
import com.pb.leadmanagement.core.response.LoginResponse
import com.pb.leadmanagement.core.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
open class AuthenticationRequestBuilder : RetroRequestBuilder() {

    fun getService(): AuthenticationRequestBuilder.AuthenticationNetworkService {
        return super.build().create(AuthenticationRequestBuilder.AuthenticationNetworkService::class.java)
    }

    interface AuthenticationNetworkService {

        @POST("LeadGenration.svc/LoginDetails")
        fun login(@Body body: LoginRequestEntity): Call<LoginResponse>

        @POST("LeadGenration.svc/RegistrationDetails")
        fun registration(@Body body: LoginEntity): Call<RegisterResponse>

        @POST("LeadGenration.svc/UpdateProfile")
        fun updateProfile(@Body body: LoginEntity): Call<LoginResponse>

    }
}