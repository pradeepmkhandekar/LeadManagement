package com.pb.leadmanagement.core.controller.motor

import android.content.Context
import com.google.gson.Gson
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import com.pb.leadmanagement.core.controller.save.SaveLeadController
import com.pb.leadmanagement.core.model.SaveError
import com.pb.leadmanagement.core.requestbuilders.LeadRequestBuilder
import com.pb.leadmanagement.core.requestentity.MotorLeadRequestEntity
import com.pb.leadmanagement.core.requestentity.UploadDocRequestEntity
import com.pb.leadmanagement.core.response.LeadEntity
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.core.response.UploadImageResponse
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
open class MotorController : IMotor {

    var mContext: Context
    var mLeadNetwork: LeadRequestBuilder.LeadNetworkService

    constructor(context: Context) {
        mContext = context
        mLeadNetwork = LeadRequestBuilder().getService()
    }


    override fun uploadDocuments(uploadDocRequestEntity: UploadDocRequestEntity, iResponseSubcriber: IResponseSubcriber) {

        mLeadNetwork.uploadDocuments(uploadDocRequestEntity).enqueue(object : Callback<UploadImageResponse> {

            override fun onResponse(call: Call<UploadImageResponse>?, response: Response<UploadImageResponse>?) {

                if (response!!.isSuccessful) {
                    if (response!!.body()?.UploadDocumentResult?.StatusNo == 0) {
                        var motorLeadResponse = MotorLeadResponse(LeadEntity(0))
                        motorLeadResponse!!.Message = response!!.body()!!.UploadDocumentResult.Message
                        motorLeadResponse!!.StatusNo = response!!.body()!!.UploadDocumentResult.StatusNo
                        iResponseSubcriber.OnSuccess(motorLeadResponse, response.message())

                    } else {
                        iResponseSubcriber.OnFailure(response!!.body()!!.UploadDocumentResult.Message)
                    }

                } else {
                    var saveError = AuthenticationController.errorStatus(mContext,
                            SaveError(response.code().toString(), "", Gson().toJson(uploadDocRequestEntity),
                                    response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)
                }

            }

            override fun onFailure(call: Call<UploadImageResponse>?, t: Throwable?) {


                if (t is ConnectException) {
                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "ConnectException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "UnknownHostException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "NumberFormatException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "IOException", "", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "Exception","", call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }


    override fun addMotorLead(motorLeadRequestEntity: MotorLeadRequestEntity, iResponseSubcriber: IResponseSubcriber) {


        mLeadNetwork.addMotorLead(motorLeadRequestEntity).enqueue(object : Callback<MotorLeadResponse> {

            override fun onResponse(call: Call<MotorLeadResponse>?, response: Response<MotorLeadResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body()?.StatusNo == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                        // SaveLeadController(mContext).SaveMotorLead(motorLeadRequestEntity)
                    } else {
                        iResponseSubcriber.OnFailure(response!!.body()?.Message)
                    }

                } else {
                    var saveError = AuthenticationController.errorStatus(mContext,
                            SaveError(response.code().toString(), "", Gson().toJson(motorLeadRequestEntity),
                                    response.raw().request().url().toString()))
                    iResponseSubcriber.OnFailure(saveError)
                }
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

                if (t is ConnectException) {
                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "ConnectException", Gson().toJson(motorLeadRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)
                } else if (t is SocketTimeoutException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "SocketTimeoutException", Gson().toJson(motorLeadRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Socket time-out")
                } else if (t is UnknownHostException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "UnknownHostException", Gson().toJson(motorLeadRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    // iResponseSubcriber.OnFailure("Unknown host exception")
                } else if (t is NumberFormatException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "NumberFormatException", Gson().toJson(motorLeadRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //  iResponseSubcriber.OnFailure("Unknown response from server")
                } else if (t is IOException) {

                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "IOException", Gson().toJson(motorLeadRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)


                    //iResponseSubcriber.OnFailure("Server Time-out")
                } else {
                    var saveError = AuthenticationController.errorStatus(mContext, SaveError("0",
                            "Exception", Gson().toJson(motorLeadRequestEntity), call?.request()?.url().toString()))

                    iResponseSubcriber.OnFailure(saveError)

                    //iResponseSubcriber.OnFailure(t?.message)
                }
            }
        })
    }
}