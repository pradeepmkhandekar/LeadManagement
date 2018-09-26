package com.pb.leadmanagement.core.response

data class PincodeEntity(
        var map_id: Int,
        var pincode: String,
        var postname: String,
        var districtname: String,
        var stateid: String,
        var state_name: String,
        var cityid: String,
        var cityname: String
)