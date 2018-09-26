package com.pb.leadmanagement.core.response

import com.google.gson.annotations.SerializedName

data class MakeMasterResult(
        var MakeID: Int,
        var MakeName: String,
        var VehicleTypeID: Int
)