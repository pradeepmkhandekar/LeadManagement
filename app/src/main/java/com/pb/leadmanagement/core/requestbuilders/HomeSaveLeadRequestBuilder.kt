package com.pb.leadmanagement.core.requestbuilders

import com.pb.leadmanagement.core.RetroRequestBuilder
import com.pb.leadmanagement.core.model.SaveError
import com.pb.leadmanagement.core.requestentity.*
import com.pb.leadmanagement.core.response.ErrorResponse
import com.pb.leadmanagement.core.response.MotorLeadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
open class HomeSaveLeadRequestBuilder : RetroRequestBuilder() {

    fun getService(): HomeSaveLeadRequestBuilder.SaveLeadNetworkService {
        return super.build().create(HomeSaveLeadRequestBuilder.SaveLeadNetworkService::class.java)
    }

    interface SaveLeadNetworkService {

        @POST
        fun saveMotorLead(@Url url: String, @Body body: MotorLeadRequestEntity): Call<MotorLeadResponse>

        @POST
        fun saveHealthLead(@Url url: String, @Body body: HealthLeadRequestEntity): Call<MotorLeadResponse>

        @POST
        fun saveLifeLead(@Url url: String, @Body body: LifeLeadRequestEntity): Call<MotorLeadResponse>


        @POST
        fun saveOtherLead(@Url url: String, @Body body: OtherRequestEntity): Call<MotorLeadResponse>

        @POST
        fun saveLoanLead(@Url url: String, @Body body: LoanRequestEntity): Call<MotorLeadResponse>

        @POST
        fun sendError(@Url url: String, @Body body: SaveError): Call<ErrorResponse>

    }
}