package com.pb.leadmanagement.core.model

data class SaveError(
        var ErrorCode: String,
        var Message: String,
        var Request: String,
        var URL: String
)