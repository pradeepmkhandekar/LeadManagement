package com.pb.leadmanagement.core.requestbuilders

import com.pb.leadmanagement.core.RetroRequestBuilder
import com.pb.leadmanagement.core.response.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
open class MastersRequestBuilder : RetroRequestBuilder() {

    fun getService(): MastersRequestBuilder.MastersNetworkService {
        return super.build().create(MastersRequestBuilder.MastersNetworkService::class.java)
    }

    interface MastersNetworkService {

        @GET("LeadGenration.svc/CityMaster")
        fun getAllCity(): Call<CityMasterResponse>

        @GET("LeadGenration.svc/StateMaster")
        fun getAllState(): Call<StateMasterResponse>

        @GET("LeadGenration.svc/VehicleTypeMaster")
        fun getVehicleTypeMaster(): Call<VehicleTypeMasterResponse>

        @GET("LeadGenration.svc/InsuranceCompanyMaster")
        fun getInsuranceCompanyMaster(): Call<InsuranceCompanyMasterResponse>

        @GET("LeadGenration.svc/MakeMaster")
        fun getMotorMakeMaster(): Call<MotorMakeMasterResponse>

        @GET("LeadGenration.svc/ModelMaster")
        fun getMotorModelMaster(): Call<MotorModelMasterResponse>

        @GET("LeadGenration.svc/VariantMaster")
        fun getMotorVariantMaster(): Call<MotorVariantMasterResponse>

        @GET("LeadGenration.svc/ProductMaster")
        fun getProductMaster(): Call<ProductMasterResponse>

        //http://202.131.96.100:7541/LeadGenration.svc/VehicleMaster?VehicleTypeID=2
        @GET("LeadGenration.svc/VehicleMaster")
        fun getMakeModelVariant(@Query("VehicleTypeID") type: String): Call<MakeModelMasterResponse>


        @Headers("token:1234567890")
        @POST
        fun fetchCityState(@Url url: String, @Body hashMap: HashMap<String, String>): Call<PincodeResponse>


    }
}