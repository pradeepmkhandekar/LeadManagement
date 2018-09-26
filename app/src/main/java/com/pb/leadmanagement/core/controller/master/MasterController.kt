package com.pb.leadmanagement.core.controller.master

import android.content.Context
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.requestbuilders.MastersRequestBuilder
import com.pb.leadmanagement.core.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Nilesh Birhade on 19-09-2018.
 */
open class MasterController : IMaster {

    var mContext: Context
    var mMastersNetwork: MastersRequestBuilder.MastersNetworkService

    constructor(context: Context) {
        mContext = context
        mMastersNetwork = MastersRequestBuilder().getService()
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


    override fun getMotorAllMaster(motorType: String) {
        mMastersNetwork.getMakeModelVariant(motorType).enqueue(object : Callback<MakeModelMasterResponse> {

            override fun onResponse(call: Call<MakeModelMasterResponse>?, response: Response<MakeModelMasterResponse>?) {
                if (response!!.isSuccessful) {

                    if (UserFacade(mContext).storeMotorMaster(motorType.toInt(), response.body()?.VehicleMasterResult!!))
                        UserFacade(mContext).MotorWheelerSuccess(motorType.toInt(), true)
                    else
                        UserFacade(mContext).MotorWheelerSuccess(motorType.toInt(), false)

                }
            }

            override fun onFailure(call: Call<MakeModelMasterResponse>?, t: Throwable?) {

            }
        })
    }

    override fun fetchCityState(pincode: String, iResponseSubcriber: IResponseSubcriber) {

        var url = "http://api.magicfinmart.com/api/get-city-and-state"
        val map = HashMap<String, String>()
        map.put("PinCode", pincode)
        mMastersNetwork.fetchCityState(url, map).enqueue(object : Callback<PincodeResponse> {

            override fun onResponse(call: Call<PincodeResponse>?, response: Response<PincodeResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body()?.StatusNo == 0)
                        iResponseSubcriber.OnSuccess(response.body(), response.message())
                    else
                        iResponseSubcriber.OnFailure(response!!.body()?.Message)

                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<PincodeResponse>?, t: Throwable?) {
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

    override fun getCityMaster() {

        mMastersNetwork.getAllCity().enqueue(object : Callback<CityMasterResponse> {

            override fun onResponse(call: Call<CityMasterResponse>?, response: Response<CityMasterResponse>?) {

                if (response!!.isSuccessful) {
                    UserFacade(mContext).storeCityMaster(response.body()?.CityMasterResult!!)
                } else {

                }
            }

            override fun onFailure(call: Call<CityMasterResponse>?, t: Throwable?) {

            }
        })
    }

    override fun getStateMaster(iResponseSubcriber: IResponseSubcriber) {
        mMastersNetwork.getAllState().enqueue(object : Callback<StateMasterResponse> {

            override fun onResponse(call: Call<StateMasterResponse>?, response: Response<StateMasterResponse>?) {

                if (response!!.isSuccessful) {
                    iResponseSubcriber.OnSuccess(response.body(), response.message())

                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<StateMasterResponse>?, t: Throwable?) {
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

    override fun getVehicleMaster(iResponseSubcriber: IResponseSubcriber) {
        mMastersNetwork.getVehicleTypeMaster().enqueue(object : Callback<VehicleTypeMasterResponse> {

            override fun onResponse(call: Call<VehicleTypeMasterResponse>?, response: Response<VehicleTypeMasterResponse>?) {

                if (response!!.isSuccessful) {
                    iResponseSubcriber.OnSuccess(response.body(), response.message())

                } else {
                    iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<VehicleTypeMasterResponse>?, t: Throwable?) {
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

    override fun getInsuranceMaster() {

        mMastersNetwork.getInsuranceCompanyMaster().enqueue(object : Callback<InsuranceCompanyMasterResponse> {

            override fun onResponse(call: Call<InsuranceCompanyMasterResponse>?, response: Response<InsuranceCompanyMasterResponse>?) {

                if (response!!.isSuccessful) {
                    UserFacade(mContext).storeInsurance(response.body()?.InsuranceCompanyMasterResult!!)
                    // iResponseSubcriber.OnSuccess(response.body(), response.message())

                } else {
                    //  iResponseSubcriber.OnFailure(errorStatus(response.code()))
                }
            }

            override fun onFailure(call: Call<InsuranceCompanyMasterResponse>?, t: Throwable?) {

            }
        })
    }
}

