package com.pb.leadmanagement.core.model

import com.google.gson.annotations.SerializedName

data class StateMasterEntity(
        @SerializedName("StateID") var stateID: Int,
        @SerializedName("StateName") var stateName: String
)