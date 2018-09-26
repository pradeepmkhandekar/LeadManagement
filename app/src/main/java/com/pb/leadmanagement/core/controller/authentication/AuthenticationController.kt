package com.pb.leadmanagement.core.controller.authentication

import android.content.Context
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.requestbuilders.AuthenticationRequestBuilder
import com.pb.leadmanagement.core.requestentity.LoginRequestEntity
import com.pb.leadmanagement.core.response.LoginResponse
import com.pb.leadmanagement.core.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
open class AuthenticationController : IAuthentication {

    var mContext: Context
    lateinit var mAuthNetwork: AuthenticationRequestBuilder.AuthenticationNetworkService

    constructor(context: Context) {
        mContext = context
        mAuthNetwork = AuthenticationRequestBuilder().getService()
    }

    fun errorStatus(statusCode: Int): String {
        when (statusCode) {
            400 -> {
                return "Bad request :The server cannot or will not process the request due to an apparent client errorStatus"
            }
            403 -> {
                return "Forbidden :Server is refusing action"
            }
            404 -> {
                return "Not found :The requested resource could not be found "
            }
            500 -> {
                return " Internal Server Error : Unexpected condition was encountered"
            }

            502 -> {
                return " Bad Gateway : Invalid response from the upstream server"
            }

            503 -> {
                return " Service Unavailable : The server is currently unavailable"
            }

            504 -> {
                return " Gateway Timeout : The server is currently unavailable"
            }
        }
        return ""
    }

    override fun login(loginRequestEntity: LoginRequestEntity, iResponseSubcriber: IResponseSubcriber) {


        mAuthNetwork.login(loginRequestEntity).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body()?.StatusNo == 0) {

                        if (UserFacade(mContext).storeUser(response.body()!!.Result)) {
                            iResponseSubcriber.OnSuccess(response.body(), response.message())
                        } else {
                            iResponseSubcriber.OnFailure(response.body()?.Message)
                        }
                    } else {
                        iResponseSubcriber.OnFailure(response.body()?.Message)
                    }
                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }

            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                if (t is ConnectException) {
                    iResponseSubcriber.OnFailure("Check your internet connection")
                } else if (t is SocketTimeoutException) {
                    iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {
                    iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {
                    iResponseSubcriber.OnFailure("Unknown response from server")
                } else {
                    iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }

    override fun register(loginEntity: LoginEntity, iResponseSubcriber: IResponseSubcriber) {

        mAuthNetwork.registration(loginEntity).enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(call: Call<RegisterResponse>?, response: Response<RegisterResponse>?) {
                if (response!!.isSuccessful) {
                    iResponseSubcriber.OnSuccess(response.body(), response.message())
                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>?, t: Throwable?) {
                if (t is ConnectException) {
                    iResponseSubcriber.OnFailure("Check your internet connection")
                } else if (t is SocketTimeoutException) {
                    iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {
                    iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {
                    iResponseSubcriber.OnFailure("Unknown response from server")
                } else {
                    iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }

    override fun updateProfile(loginEntity: LoginEntity, iResponseSubcriber: IResponseSubcriber) {

        mAuthNetwork.updateProfile(loginEntity).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                if (response!!.isSuccessful) {

                    if (response.body()?.StatusNo == 0) {
                        if (UserFacade(mContext).storeUser(response.body()!!.Result)) {
                            iResponseSubcriber.OnSuccess(response.body(), response.message())
                        } else {
                            iResponseSubcriber.OnFailure("User not found")
                        }
                    }
                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                if (t is ConnectException) {
                    iResponseSubcriber.OnFailure("Check your internet connection")
                } else if (t is SocketTimeoutException) {
                    iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {
                    iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {
                    iResponseSubcriber.OnFailure("Unknown response from server")
                } else {
                    iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }
}