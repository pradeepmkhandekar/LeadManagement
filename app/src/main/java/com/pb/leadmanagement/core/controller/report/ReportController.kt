package com.pb.leadmanagement.core.controller.report

import android.content.Context
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestbuilders.ReportRequestBuilder
import com.pb.leadmanagement.core.response.HealthReportResponse
import com.pb.leadmanagement.core.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by IN-RB on 03-10-2018.
 */
class ReportController : IReport {

    var mContext: Context
    lateinit var mRepNetwork: ReportRequestBuilder.ReportNetworkService

    constructor(mContext: Context) {
        this.mContext = mContext
        mRepNetwork = ReportRequestBuilder().getService()
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

    override fun getHealthReport(fromDate: String, toDate: String, UserId: String,iResponseSubcriber: IResponseSubcriber) {

        val map = HashMap<String, String>()
        map.put("FromDate", fromDate)
        map.put("ToDate", toDate)
        map.put("UserId", UserId)

        var url ="http://49.50.95.141:2001/LeadCollection.svc/HealthReport"

        mRepNetwork.healthReport(url,map).enqueue(object : Callback<HealthReportResponse> {

            override fun onResponse(call: Call<HealthReportResponse>?, response: Response<HealthReportResponse>?) {
                if (response!!.isSuccessful) {
                    if (response.body()?.StatusNo == 0)
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                    else
                        iResponseSubcriber.OnFailure(response.body()?.Message)
                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<HealthReportResponse>?, t: Throwable?) {
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