package com.pb.leadmanagement.core.controller.authentication

import android.content.Context
import com.google.gson.Gson
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.BaseController
import com.pb.leadmanagement.core.controller.save.SaveLeadController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.model.SaveError
import com.pb.leadmanagement.core.requestbuilders.AuthenticationRequestBuilder
import com.pb.leadmanagement.core.requestentity.LoginRequestEntity
import com.pb.leadmanagement.core.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class AuthenticationController : BaseController, IAuthentication {


    companion object {
        fun errorStatus(context: Context, saveError: SaveError): String {
            when (saveError.ErrorCode) {

                "400" -> {
                    SaveLeadController(context).saveError(saveError)
                    return "Bad request :The server cannot or will not process the request due to an apparent client error status"
                }
                "403" -> {
                    SaveLeadController(context).saveError(saveError)
                    return "Forbidden :Server is refusing action"
                }
                "404" -> {
                    SaveLeadController(context).saveError(saveError)
                    return "Not found :The requested resource could not be found "
                }
                "500" -> {
                    SaveLeadController(context).saveError(saveError)
                    return "Internal Server Error : Unexpected condition was encountered"
                }

                "502" -> {
                    return "Bad Gateway : Invalid response from the upstream server"
                }

                "503" -> {
                    return "Service Unavailable : The server is currently unavailable"
                }

                "504" -> {
                    return "Gateway Timeout : The server is currently unavailable"
                }
            }
            return ""
        }

    }

    var mContext: Context
    var mAuthNetwork: AuthenticationRequestBuilder.AuthenticationNetworkService

    constructor(context: Context) {
        mContext = context
        mAuthNetwork = AuthenticationRequestBuilder().getService()

    }

    override fun fetchLeadInterest(iResponseSubcriber: IResponseSubcriber) {
        var url = "http://49.50.95.141:2001/LeadCollection.svc/ShowProducts"

        val map = HashMap<String, String>()
        map.put("ReferenceNo", UserFacade(mContext).getUser()!!.ReferenceCode)


        mAuthNetwork.fetchLeadInterest(url, map).enqueue(object : Callback<LeadInterestPolicyResponse> {

            override fun onResponse(call: Call<LeadInterestPolicyResponse>?, response: Response<LeadInterestPolicyResponse>?) {

                if (response!!.isSuccessful) {
                    if (response.body()?.StatusNo == 0) {

                        if (UserFacade(mContext).updateLeadInterest(response?.body()?.Products)) {
                            iResponseSubcriber.OnSuccess(response.body(), response.message())
                        } else {
                            UserFacade(mContext).clearUser()
                            iResponseSubcriber.OnFailure(response.body()?.Message)
                        }
                    } else {
                        iResponseSubcriber.OnFailure(response.body()?.Message)
                    }
                } else {

                    var saveError = errorStatus(mContext, SaveError(response.code().toString(), "", "", response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)

                }
            }

            override fun onFailure(call: Call<LeadInterestPolicyResponse>?, t: Throwable?) {
                if (t is ConnectException) {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "ConnectException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "UnknownHostException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "NumberFormatException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "IOException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "Exception", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
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

                    var saveError = errorStatus(mContext, SaveError(response.code().toString(), "", Gson().toJson(loginRequestEntity), response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)

                }

            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {

                if (t is ConnectException) {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "ConnectException", Gson().toJson(loginRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", Gson().toJson(loginRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "UnknownHostException", Gson().toJson(loginRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "NumberFormatException", Gson().toJson(loginRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "IOException", Gson().toJson(loginRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "Exception", Gson().toJson(loginRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }

    override fun register(loginEntity: LoginEntity, iResponseSubcriber: IResponseSubcriber) {
        mAuthNetwork.registration(loginEntity).enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(call: Call<RegisterResponse>?, response: Response<RegisterResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body()?.StatusNo == 0)
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                    else
                        iResponseSubcriber.OnFailure(response.body()?.Message)
                } else {

                    var saveError = errorStatus(mContext, SaveError(response.code().toString(),
                            "", Gson().toJson(loginEntity), response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>?, t: Throwable?) {
//                if (t is ConnectException) {
//                    iResponseSubcriber.OnFailure("Check your internet connection")
//                } else if (t is SocketTimeoutException) {
//                    iResponseSubcriber.OnFailure("Socket time-out")
//                } else if (t is UnknownHostException) {
//                    iResponseSubcriber.OnFailure("Unknown host exception")
//                } else if (t is NumberFormatException) {
//                    iResponseSubcriber.OnFailure("Unknown response from server")
//                } else if (t is IOException) {
//                    iResponseSubcriber.OnFailure("Server Time-out")
//                } else {
//                    iResponseSubcriber.OnFailure(t?.message)
//                }

                if (t is ConnectException) {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "ConnectException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "UnknownHostException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "NumberFormatException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "IOException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "Exception", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
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
                    } else {
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                    }
                } else {

                    var saveError = errorStatus(mContext, SaveError(response.code().toString(),
                            "", Gson().toJson(loginEntity), response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                /*if (t is ConnectException) {
                    iResponseSubcriber.OnFailure("Check your internet connection")
                } else if (t is SocketTimeoutException) {
                    iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {
                    iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {
                    iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {
                    iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    iResponseSubcriber.OnFailure(t?.message)
                }*/


                if (t is ConnectException) {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "ConnectException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "UnknownHostException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "NumberFormatException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "IOException", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "Exception", Gson().toJson(loginEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }

            }
        })
    }

    override fun verifyOTP(mobileNo: String, flag: String, iResponseSubcriber: IResponseSubcriber) {

        val map = HashMap<String, String>()
        map.put("MobileNo", mobileNo)
        map.put("Flag", flag)

        mAuthNetwork.verifyOTP(map).enqueue(object : Callback<OTPResponse> {

            override fun onResponse(call: Call<OTPResponse>?, response: Response<OTPResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body()?.StatusNo == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                    } else {
                        iResponseSubcriber.OnFailure(response.body()?.Message)
                    }

                } else {

                    var saveError = errorStatus(mContext, SaveError(response.code().toString(), "", mobileNo, response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)

                }
            }

            override fun onFailure(call: Call<OTPResponse>?, t: Throwable?) {

                if (t is ConnectException) {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "ConnectException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "UnknownHostException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "NumberFormatException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "IOException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "Exception", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }

    override fun forgotPassword(mobileNo: String, password: String, iResponseSubcriber: IResponseSubcriber) {

        val map = HashMap<String, String>()
        map.put("MobileNo", mobileNo)
        map.put("Password", password)

        mAuthNetwork.forgotPassword(map).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body()?.StatusNo == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                    } else {
                        iResponseSubcriber.OnFailure(response.body()?.Message)
                    }

                } else {

                    var saveError = errorStatus(mContext, SaveError(response.code().toString(), "", mobileNo, response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)

                }
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {

                if (t is ConnectException) {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "ConnectException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "UnknownHostException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "NumberFormatException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = errorStatus(mContext, SaveError("0",
                            "IOException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = errorStatus(mContext, SaveError("0",
                            "Exception", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }
}