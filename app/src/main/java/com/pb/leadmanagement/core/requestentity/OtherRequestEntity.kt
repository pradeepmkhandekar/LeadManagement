package com.pb.leadmanagement.core.requestentity

data class OtherRequestEntity(
        var Name: String,
        var MobileNo: String,
        var ProductID: Int,
        var CategoryID: Int,
        var TravelDate: String,
        var TravelCountry: String,
        var IsNew: Boolean,
        var RenewalDate: String,
        var CompanyName: String,
        var InsuredName: String,
        var PartnerChildLogin: String,
        var UserID: Int
)