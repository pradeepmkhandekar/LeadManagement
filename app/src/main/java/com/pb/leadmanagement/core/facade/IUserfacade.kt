package com.pb.leadmanagement.core.facade

import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.login.LoginActivity

/**
 * Created by Nilesh Birhade on 18-09-2018.
 */
interface IUserfacade {

    fun clearUser(): Boolean

    fun storeUser(loginEntity: LoginEntity): Boolean

    fun getUser(): LoginEntity?

    fun getUserID(): Int

    fun getReferenceCode(): String
}