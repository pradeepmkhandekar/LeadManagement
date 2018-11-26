package com.pb.leadmanagement.core.requestentity

data class HealthLeadRequestEntity(
        var Name: String,
        var MobileNo: String,
        var EmailID: String,
        var DOB: String,
        var City: String,
        var ProposalType: String,
        var Category: String,
        var PolicyExpiryDate: String,
        var ExistingDisease: String,
        var OtherExistingDisease: String,
        var CurrentYearInsCmpID: String,
        var PartnerChildLogin: String,
        var UserID: Int,
        var Remarks: String
)