package com.pb.leadmanagement.core.facade

import com.pb.leadmanagement.core.response.MakeX
import com.pb.leadmanagement.core.response.VehicleMasterResult
import com.pb.leadmanagement.core.response.VehicleTypeMasterResponse

/**
 * Created by Nilesh Birhade on 19-09-2018.
 */
interface IMotorFacade {

    fun storeMotorMaster(type: Int, vehicleMaster: VehicleMasterResult): Boolean

    fun MotorWheelerSuccess(type: Int, isSuccess: Boolean): Boolean

    fun isMotorMasterSuccess(type: Int): Boolean

    fun getTwoWheelerMaster(type: Int): List<MakeX>?

    fun getFourWheelerMaster(type: Int): List<MakeX>?


    /* fun storeModel(modelMasterResult: List<ModelMasterResult>)

     fun storeVariant(variantMasterResult: List<VariantMasterResult>)

     fun getMakeList(vehicleType: Int): List<MakeMasterResult>

     fun getModelList(makeID: Int): List<ModelMasterResult>

     fun getVariantList(modelID: Int): List<VariantMasterResult>*/


}