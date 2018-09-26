package com.pb.leadmanagement.core.response

data class MakeX(
        var Make: String,
        var MakeID: Int,
        var Model: List<ModelX>
)