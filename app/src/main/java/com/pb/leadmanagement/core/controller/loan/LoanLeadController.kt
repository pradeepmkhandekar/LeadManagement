package com.pb.leadmanagement.core.controller.loan

import android.content.Context
import com.google.gson.Gson
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import com.pb.leadmanagement.core.controller.save.SaveLeadController
import com.pb.leadmanagement.core.model.SaveError
import com.pb.leadmanagement.core.requestbuilders.LeadRequestBuilder
import com.pb.leadmanagement.core.requestentity.LoanRequestEntity
import com.pb.leadmanagement.core.requestentity.LoanRequestModifiedEntity
import com.pb.leadmanagement.core.response.MotorLeadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Nilesh Birhade on 20-09-2018.
 */
open class LoanLeadController : ILoan {

    var mContext: Context
    var mLeadNetwork: LeadRequestBuilder.LeadNetworkService

    constructor(context: Context) {
        mContext = context
        mLeadNetwork = LeadRequestBuilder().getService()
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


    override fun addLoanLead(loanRequestEntity: LoanRequestModifiedEntity, iResponseSubcriber: IResponseSubcriber) {


        mLeadNetwork.addLoanLead(loanRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body()?.StatusNo == 0) {

                        iResponseSubcriber.OnSuccess(response.body(), response.message())

                        //SaveLeadController(mContext).SaveLoanLead(loanRequestEntity)

                    } else {
                        iResponseSubcriber.OnFailure(response!!.body()?.Message)
                    }

                } else {
                    var saveError = AuthenticationController.errorStatus(mContext,
                            SaveError(response.code().toString(), "", Gson().toJson(loanRequestEntity),
                                    response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)
                }
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {
                /*       if (t is ConnectException) {
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
                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "ConnectException", Gson().toJson(loanRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", Gson().toJson(loanRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "UnknownHostException", Gson().toJson(loanRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "NumberFormatException", Gson().toJson(loanRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "IOException", Gson().toJson(loanRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "Exception", Gson().toJson(loanRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }
}