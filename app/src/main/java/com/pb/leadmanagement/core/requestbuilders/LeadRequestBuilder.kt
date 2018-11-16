package com.pb.leadmanagement.core.requestbuilders

import com.pb.leadmanagement.core.RetroRequestBuilder
import com.pb.leadmanagement.core.requestentity.*
import com.pb.leadmanagement.core.response.MotorLeadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
open class LeadRequestBuilder : RetroRequestBuilder() {

    fun getService(): LeadRequestBuilder.LeadNetworkService {
        return super.build().create(LeadRequestBuilder.LeadNetworkService::class.java)
    }

    interface LeadNetworkService {

        @POST("LeadGenration.svc/MotorLead")
        fun addMotorLead(@Body body: MotorLeadRequestEntity): Call<MotorLeadResponse>

        @POST("LeadGenration.svc/HealthLead")
        fun addHealthLead(@Body body: HealthLeadRequestEntity): Call<MotorLeadResponse>

        @POST("LeadGenration.svc/LifeLead")
        fun addLifeLead(@Body body: LifeLeadRequestEntity): Call<MotorLeadResponse>


        @POST("LeadGenration.svc/OtherLead")
        fun addOtherLead(@Body body: OtherRequestEntity): Call<MotorLeadResponse>

        @POST("LeadGenration.svc/LoanLead")
        fun addLoanLead(@Body body: LoanRequestModifiedEntity): Call<MotorLeadResponse>

        @Headers("Content-Type: text/plain")
        @POST("LeadGenration.svc/UploadDocument")
        fun uploadDocuments(@Body body: UploadDocRequestEntity): Call<MotorLeadResponse>

    }
}