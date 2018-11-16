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
        var CarMake: String,
        var CarModel: String,
        var CarMfgDate: String,

        var CCEmployeeType: String,

        var PartnerChildLogin: String,
        var UserID: Int,
        var Remark: String
)