package com.pb.leadmanagement.core.model

data class LoginEntity(
        var Address: String,
        var ChainCode: String,
        var ChainID: String,
        var City: String,
        var EmailID: String,
        var FieldManager: String,
        var ID: Int,
        var LeadInterest: List<String>,
        var Location: String,
        var MobileNo: String,
        var Name: String,
        var Password: String,
        var Pincode: String,
        var State: String
)