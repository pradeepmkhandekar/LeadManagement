package com.pb.leadmanagement.core.controller.life

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestentity.LifeLeadRequestEntity
import com.pb.leadmanagement.core.requestentity.MotorLeadRequestEntity

/**
 * Created by Nilesh Birhade on 20-09-2018.
 */
interface ILife {

    fun addLifeLead(lifeLeadRequestEntity: LifeLeadRequestEntity, iResponseSubcriber: IResponseSubcriber)
}