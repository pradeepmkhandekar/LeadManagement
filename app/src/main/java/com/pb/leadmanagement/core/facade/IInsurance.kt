package com.pb.leadmanagement.core.facade

import com.pb.leadmanagement.core.model.CityMasterEntity
import com.pb.leadmanagement.core.model.InsuranceCompanyMasterEntity
import com.pb.leadmanagement.core.response.InsuranceCompanyMasterResponse

/**
 * Created by Nilesh Birhade on 21-09-2018.
 */
interface IInsurance {

    fun storeInsurance(insuranceCompanyMasterResponse: List<InsuranceCompanyMasterEntity>): Boolean

    fun getInsuranceList(): List<InsuranceCompanyMasterEntity>?

    fun isInsurerMasterSuccess(): Boolean


    fun storeCityMaster(cityMasterEntity: List<CityMasterEntity>): Boolean

    fun getCity(): List<CityMasterEntity>?

    fun isCityMasterSuccess(): Boolean

   /* fun getCityName(cityID: Int): String

    fun getStateName(stateID: Int): String*/

}