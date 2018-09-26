package com.pb.leadmanagement.core.requestentity

data class LoanRequestEntity(
        var Name: String,
        var MobileNo: String,
        var Products: List<String>,
        var ChaindID: String,
        var UserID: Int
)