package com.pb.leadmanagement.core.facade

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pb.leadmanagement.core.model.InsuranceCompanyMasterEntity
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.response.MakeX
import com.pb.leadmanagement.core.response.VehicleMasterResult
import com.google.gson.reflect.TypeToken
import com.pb.leadmanagement.core.model.CityMasterEntity


/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
open class UserFacade : IUserfacade, IMotorFacade, IInsurance {


    lateinit var mContext: Context
    internal lateinit var sharedPreferences: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor
    val SHAREDPREFERENCE_TITLE: String = "LEAD_MANAGEMENT"
    val LOGIN_DATA: String = "LOGIN_DATA"

    val TWO_WHEELER: String = "TWO_WHEELER"
    val FOUR_WHEELER: String = "FOUR_WHEELER"
    val INSURER_LIST: String = "INSURER"
    val CITY_LIST: String = "CITY"


    val TWO_WHEELER_SUCCESS = "TWO_WHEELER_SUCCESS"
    val FOUR_WHEELER_SUCCESS = "FOUR_WHEELER_SUCCESS"
    val INSURER_SUCCESS = "INSURER_SUCCESS"
    val CITY_SUCCESS = "CITY_SUCCESS"


    constructor(context: Context) {
        mContext = context
        sharedPreferences = mContext.getSharedPreferences(SHAREDPREFERENCE_TITLE, 0)
        editor = sharedPreferences.edit()
    }

    //region user facade

    override fun clearUser(): Boolean {
        editor.remove(LOGIN_DATA)
        return editor.commit()
    }

    override fun storeUser(loginActivity: LoginEntity): Boolean {
        try {
            val gson = Gson()
            editor.remove(LOGIN_DATA)
            editor.putString(LOGIN_DATA, gson.toJson(loginActivity))
            return editor.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun getUser(): LoginEntity? {
        val user = sharedPreferences.getString(LOGIN_DATA, "")
        val gson = Gson()
        if (user.length > 0) {
            val loginEntity = gson.fromJson<LoginEntity>(user, LoginEntity::class.java)
            return loginEntity
        } else {
            return null
        }
    }

    override fun getUserID(): Int {
        var loginEntity = getUser()
        if (loginEntity != null)
            return loginEntity.ID
        else
            return 0

    }

    override fun getChainID(): String {
        var loginEntity = getUser()
        if (loginEntity != null)
            return loginEntity.ChainID
        else
            return ""
    }

    //endregion

    //region motor facade

    override fun MotorWheelerSuccess(type: Int, isSuccess: Boolean): Boolean {
        if (type == 4) {
            try {
                editor.remove(TWO_WHEELER_SUCCESS)
                editor.putBoolean(TWO_WHEELER_SUCCESS, isSuccess)
                return editor.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (type == 2) {
            try {
                editor.remove(FOUR_WHEELER_SUCCESS)
                editor.putBoolean(FOUR_WHEELER_SUCCESS, isSuccess)
                return editor.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }

    override fun isMotorMasterSuccess(type: Int): Boolean {
        if (type == 4) {
            try {
                return sharedPreferences.getBoolean(TWO_WHEELER_SUCCESS, false)
            } catch (e: Exception) {
                return false
            }
        } else if (type == 2) {
            try {
                return sharedPreferences.getBoolean(FOUR_WHEELER_SUCCESS, false)
            } catch (e: Exception) {
                return false
            }
        }

        return false
    }

    override fun storeMotorMaster(type: Int, vehicleMaster: VehicleMasterResult): Boolean {
        if (type == 4) {
            try {
                val gson = Gson()
                editor.remove(TWO_WHEELER)
                editor.putString(TWO_WHEELER, gson.toJson(vehicleMaster))
                return editor.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (type == 2) {
            try {
                val gson = Gson()
                editor.remove(FOUR_WHEELER)
                editor.putString(FOUR_WHEELER, gson.toJson(vehicleMaster))
                return editor.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }

    override fun getTwoWheelerMaster(type: Int): List<MakeX>? {

        if (type == 4) {
            val twoWheeler = sharedPreferences.getString(TWO_WHEELER, "")
            val gson = Gson()
            if (twoWheeler.length > 0) {
                val vehicleMaster = gson.fromJson<VehicleMasterResult>(twoWheeler, VehicleMasterResult::class.java)
                return vehicleMaster.Make
            } else {
                return null
            }
        }
        return null
    }

    override fun getFourWheelerMaster(type: Int): List<MakeX>? {
        if (type == 2) {
            val fourWheeler = sharedPreferences.getString(FOUR_WHEELER, "")
            val gson = Gson()
            if (fourWheeler.length > 0) {
                val vehicleMaster = gson.fromJson<VehicleMasterResult>(fourWheeler, VehicleMasterResult::class.java)
                return vehicleMaster.Make
            } else {
                return null
            }
        }
        return null
    }

    //endregion

    //region Insurance List

    override fun isInsurerMasterSuccess(): Boolean {
        return sharedPreferences.getBoolean(INSURER_SUCCESS, false)
    }

    override fun storeInsurance(insuranceCompanyMasterResponse: List<InsuranceCompanyMasterEntity>): Boolean {

        try {
            val gson = Gson()
            editor.remove(INSURER_LIST)
            editor.putString(INSURER_LIST, gson.toJson(insuranceCompanyMasterResponse))
            editor.putBoolean(INSURER_SUCCESS, editor.commit())
            return editor.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun getInsuranceList(): List<InsuranceCompanyMasterEntity>? {

        val insurer = sharedPreferences.getString(INSURER_LIST, "")

        if (insurer.length > 0) {
            val convertType = object : TypeToken<List<InsuranceCompanyMasterEntity>>() {}.type
            val insurerList = Gson().fromJson<List<InsuranceCompanyMasterEntity>>(insurer, convertType)
            return insurerList
        } else {
            return null
        }
    }

    //endregion

    override fun storeCityMaster(cityMasterEntity: List<CityMasterEntity>): Boolean {
        try {
            val gson = Gson()
            editor.remove(CITY_LIST)
            editor.putString(CITY_LIST, gson.toJson(cityMasterEntity))
            editor.putBoolean(CITY_SUCCESS, editor.commit())
            return editor.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun getCity(): List<CityMasterEntity>? {
        val insurer = sharedPreferences.getString(CITY_LIST, "")

        if (insurer.length > 0) {
            val convertType = object : TypeToken<List<CityMasterEntity>>() {}.type
            val insurerList = Gson().fromJson<List<CityMasterEntity>>(insurer, convertType)
            return insurerList
        } else {
            return null
        }
    }

    override fun isCityMasterSuccess(): Boolean {
        return sharedPreferences.getBoolean(CITY_SUCCESS, false)
    }

/*    override fun getCityName(cityID: Int): String {

        var selectedCity: CityMasterEntity = getCity()?.filter { s -> s.CityID == cityID }!!.single()
        return selectedCity.CityName
    }

    override fun getStateName(stateID: Int): String {
        var selectedCity: CityMasterEntity = getCity()?.filter { s -> s. }!!.single()
        return selectedCity.CityName
    }*/


}