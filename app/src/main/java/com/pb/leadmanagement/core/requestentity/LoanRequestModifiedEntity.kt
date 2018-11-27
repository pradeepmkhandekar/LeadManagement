package com.pb.leadmanagement.core.requestentity

data class LoanRequestModifiedEntity(
        var Name: String,
        var MobileNo: String,
        var Products: String,
        var BusinessLoanAmount: Long,
        var isBusinessExistingLoan: Boolean,
        var ExistingBusinessLoanAmount: Long,

        var HomeLoanAmount: Long,
        var HomeLoanCityName: String,
        var HomeLoantype: String,

        var PLAmount: Long,
        var PLEmployeeType: String,
        var PLDispatchDate: String,

        var CarType: String,
        var CarMakeID: Int,
        var CarModelID: Int,
        var CarMfgDate: String,

        var CCEmployeeType: String,

        var PartnerChildLogin: String,
        var UserID: Int,
        var Remarks: String
)