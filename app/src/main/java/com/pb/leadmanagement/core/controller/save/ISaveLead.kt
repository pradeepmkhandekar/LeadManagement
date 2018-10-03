package com.pb.leadmanagement.core.controller.save

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestentity.*

/**
 * Created by Nilesh Birhade on 20-09-2018.
 */
interface ISaveLead {

    fun SaveHealthLead(healthLeadRequestEntity: HealthLeadRequestEntity)

    fun SaveMotorLead(motorLeadRequestEntity: MotorLeadRequestEntity)

    fun SaveLifeLead(lifeLeadRequestEntity: LifeLeadRequestEntity)

    fun SaveLoanLead(loanRequestEntity: LoanRequestEntity)

    fun SaveOtherLead(otherRequestEntity: OtherRequestEntity)
}