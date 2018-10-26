package com.pb.leadmanagement.core.controller.save

import android.content.Context
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestbuilders.HomeSaveLeadRequestBuilder
import com.pb.leadmanagement.core.requestbuilders.LeadRequestBuilder
import com.pb.leadmanagement.core.requestentity.*
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
open class SaveLeadController : ISaveLead {


    var mContext: Context
    var mSaveNetwork: HomeSaveLeadRequestBuilder.SaveLeadNetworkService

    constructor(context: Context) {
        mContext = context
        mSaveNetwork = HomeSaveLeadRequestBuilder().getService()
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


    override fun SaveMotorLead(motorLeadRequestEntity: MotorLeadRequestEntity) {
        var url = "http://49.50.95.141:2001/LeadCollection.svc/SaveMotorLeads"

        mSaveNetwork.saveMotorLead(url, motorLeadRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                /* if (response!!.isSuccessful) {
                     if (response!!.body()?.StatusNo == 0)
                         iResponseSubcriber.OnSuccess(response.body(), response.message())
                     else
                         iResponseSubcriber.OnFailure(response!!.body()?.Message)

                 } else {
                     iResponseSubcriber.OnFailure(errorStatus(response.code()))
                 }*/
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {
               /* if (t is ConnectException) {
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
            }
        })
    }

    override fun SaveLoanLead(loanRequestEntity: LoanRequestEntity) {

        var url = "http://49.50.95.141:2001/LeadCollection.svc/SaveLoanLeads"

        mSaveNetwork.saveLoanLead(url, loanRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                /* if (response!!.isSuccessful) {
                     if (response!!.body()?.StatusNo == 0)
                         iResponseSubcriber.OnSuccess(response.body(), response.message())
                     else
                         iResponseSubcriber.OnFailure(response!!.body()?.Message)

                 } else {
                     iResponseSubcriber.OnFailure(errorStatus(response.code()))
                 }*/
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {
                /* if (t is ConnectException) {
                     iResponseSubcriber.OnFailure("Check your internet connection")
                 } else if (t is SocketTimeoutException) {
                     iResponseSubcriber.OnFailure("Socket time-out")
                 } else if (t is UnknownHostException) {
                     iResponseSubcriber.OnFailure("Unknown host exception")
                 } else if (t is NumberFormatException) {
                     iResponseSubcriber.OnFailure("Unknown response from server")
                 } else {
                     iResponseSubcriber.OnFailure(t?.message)
                 }*/
            }
        })
    }

    override fun SaveOtherLead(otherRequestEntity: OtherRequestEntity) {

        var url = "http://49.50.95.141:2001/LeadCollection.svc/SaveOtherLeads"

        mSaveNetwork.saveOtherLead(url, otherRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                /* if (response!!.isSuccessful) {
                     if (response!!.body()?.StatusNo == 0)
                         iResponseSubcriber.OnSuccess(response.body(), response.message())
                     else
                         iResponseSubcriber.OnFailure(response!!.body()?.Message)

                 } else {
                     iResponseSubcriber.OnFailure(errorStatus(response.code()))
                 }*/
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {
                /* if (t is ConnectException) {
                     iResponseSubcriber.OnFailure("Check your internet connection")
                 } else if (t is SocketTimeoutException) {
                     iResponseSubcriber.OnFailure("Socket time-out")
                 } else if (t is UnknownHostException) {
                     iResponseSubcriber.OnFailure("Unknown host exception")
                 } else if (t is NumberFormatException) {
                     iResponseSubcriber.OnFailure("Unknown response from server")
                 } else {
                     iResponseSubcriber.OnFailure(t?.message)
                 }*/
            }
        })

    }

    override fun SaveLifeLead(lifeLeadRequestEntity: LifeLeadRequestEntity) {
        var url = "http://49.50.95.141:2001/LeadCollection.svc/SaveLifeLeads"
        mSaveNetwork.saveLifeLead(url, lifeLeadRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                /* if (response!!.isSuccessful) {
                     if (response!!.body()?.StatusNo == 0)
                         iResponseSubcriber.OnSuccess(response.body(), response.message())
                     else
                         iResponseSubcriber.OnFailure(response!!.body()?.Message)

                 } else {
                     iResponseSubcriber.OnFailure(errorStatus(response.code()))
                 }*/
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {
                /* if (t is ConnectException) {
                     iResponseSubcriber.OnFailure("Check your internet connection")
                 } else if (t is SocketTimeoutException) {
                     iResponseSubcriber.OnFailure("Socket time-out")
                 } else if (t is UnknownHostException) {
                     iResponseSubcriber.OnFailure("Unknown host exception")
                 } else if (t is NumberFormatException) {
                     iResponseSubcriber.OnFailure("Unknown response from server")
                 } else {
                     iResponseSubcriber.OnFailure(t?.message)
                 }*/
            }
        })
    }

    override fun SaveHealthLead(healthLeadRequestEntity: HealthLeadRequestEntity) {

        var url = "http://49.50.95.141:2001/LeadCollection.svc/SaveHealthLeads";

        mSaveNetwork.saveHealthLead(url, healthLeadRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                /* if (response!!.isSuccessful) {
                     if (response!!.body()?.StatusNo == 0)
                         iResponseSubcriber.OnSuccess(response.body(), response.message())
                     else
                         iResponseSubcriber.OnFailure(response!!.body()?.Message)

                 } else {
                     iResponseSubcriber.OnFailure(errorStatus(response.code()))
                 }*/
            }

            override fun onFailure(call: Call<MotorLeadResponse>?, t: Throwable?) {
                /* if (t is ConnectException) {
                     iResponseSubcriber.OnFailure("Check your internet connection")
                 } else if (t is SocketTimeoutException) {
                     iResponseSubcriber.OnFailure("Socket time-out")
                 } else if (t is UnknownHostException) {
                     iResponseSubcriber.OnFailure("Unknown host exception")
                 } else if (t is NumberFormatException) {
                     iResponseSubcriber.OnFailure("Unknown response from server")
                 } else {
                     iResponseSubcriber.OnFailure(t?.message)
                 }*/
            }
        })
    }
}