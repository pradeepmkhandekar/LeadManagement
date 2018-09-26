package com.pb.leadmanagement.core.controller.master

import com.pb.leadmanagement.core.IResponseSubcriber

/**
 * Created by Nilesh Birhade on 19-09-2018.
 */
interface IMaster {

    fun getCityMaster()

    fun getStateMaster(iResponseSubcriber: IResponseSubcriber)

    fun getVehicleMaster(iResponseSubcriber: IResponseSubcriber)

    fun getInsuranceMaster()

    fun fetchCityState(pincode: String, iResponseSubcriber: IResponseSubcriber)

    fun getMotorAllMaster(motorType: String)

    /* fun getMotorMakeMaster(iResponseSubcriber: IResponseSubcriber)

     fun getMotorModelMaster(iResponseSubcriber: IResponseSubcriber)

     fun getMotorVariantMaster(iResponseSubcriber: IResponseSubcriber)*/


}