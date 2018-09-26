package com.pb.leadmanagement.core.controller.health

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestentity.HealthLeadRequestEntity
import com.pb.leadmanagement.core.requestentity.MotorLeadRequestEntity

/**
 * Created by Nilesh Birhade on 20-09-2018.
 */
interface IHealth {

    fun addHealthLead(healthLeadRequestEntity: HealthLeadRequestEntity, iResponseSubcriber: IResponseSubcriber)
}