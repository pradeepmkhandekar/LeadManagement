package com.pb.leadmanagement.core.requestbuilders

import com.pb.leadmanagement.core.RetroRequestBuilder
import com.pb.leadmanagement.core.response.HealthReportResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Created by IN-RB on 03-10-2018.
 */
class ReportRequestBuilder : RetroRequestBuilder() {

    fun getService(): ReportRequestBuilder.ReportNetworkService {
        return super.build().create(ReportRequestBuilder.ReportNetworkService::class.java)
    }

    interface ReportNetworkService {

        @POST
        fun healthReport(@Url url: String, @Body body: HashMap<String,String>): Call<HealthReportResponse>



    }

}