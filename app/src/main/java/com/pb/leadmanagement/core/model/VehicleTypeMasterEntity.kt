package com.pb.leadmanagement.core.model

import com.google.gson.annotations.SerializedName

data class VehicleTypeMasterEntity(
        @SerializedName("VehicleType") var vehicleType: String,
        @SerializedName("VehicleTypeID") var vehicleTypeID: Int
)