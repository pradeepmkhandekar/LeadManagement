package com.pb.leadmanagement.core.controller.motor

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestentity.MotorLeadRequestEntity
import com.pb.leadmanagement.core.requestentity.UploadDocRequestEntity

/**
 * Created by Nilesh Birhade on 20-09-2018.
 */
interface IMotor {

    fun addMotorLead(motorLeadRequestEntity: MotorLeadRequestEntity, iResponseSubcriber: IResponseSubcriber)

    fun uploadDocuments(uploadDocRequestEntity: UploadDocRequestEntity, iResponseSubcriber: IResponseSubcriber)

}