package com.pb.leadmanagement.core.controller.loan

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestentity.LoanRequestEntity
import com.pb.leadmanagement.core.requestentity.OtherRequestEntity

/**
 * Created by Nilesh Birhade on 24-09-2018.
 */
interface ILoan {

    fun addLoanLead(loanRequestEntity: LoanRequestEntity, iResponseSubcriber: IResponseSubcriber)
}