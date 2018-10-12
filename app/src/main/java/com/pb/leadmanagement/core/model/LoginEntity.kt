package com.pb.leadmanagement.core.model

data class LoginEntity(
        var Address: String,
        var PartnerLogin: String,
        var PartnerChildLogin: String,
        var City: String,
        var EmailID: String,
        var LMAccountManager: String,
        var ID: Int,
        var LeadInterest: List<String>,
        var Location: String,
        var MobileNo: String,
        var Name: String,
        var Password: String,
        var Pincode: String,
        var State: String
)