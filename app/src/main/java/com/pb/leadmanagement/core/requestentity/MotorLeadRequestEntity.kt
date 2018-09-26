package com.pb.leadmanagement.core.requestentity

data class MotorLeadRequestEntity(
        var Name: String,
        var MobileNo: String,
        var EmailID: String,
        var RelationshipWithOwner: String,
        var RelativeName: String,
        var RelativeMobileNo: String,
        var VehicleTypeID: Int,
        var RegistrationNo: String,
        var MakeID: Int,
        var ModelID: Int,
        var SubModelID: Int,
        var ManufacturingDate: String,
        var PolicyExpiryDate: String,
        var ChainID: String,
        var UserID: Int
)