package com.pb.leadmanagement.core.controller.authentication

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.requestentity.LoginRequestEntity

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
interface IAuthentication {

    fun login(loginRequestEntity: LoginRequestEntity, iResponseSubcriber: IResponseSubcriber)

    fun register(loginEntity: LoginEntity, iResponseSubcriber: IResponseSubcriber)

    fun updateProfile(loginEntity: LoginEntity, iResponseSubcriber: IResponseSubcriber)

    fun verifyOTP(mobileNo: String, flag: String, iResponseSubcriber: IResponseSubcriber)

    fun forgotPassword(mobileNo: String, password: String, iResponseSubcriber: IResponseSubcriber)

    fun fetchLeadInterest( iResponseSubcriber: IResponseSubcriber)

}