package com.pb.leadmanagement.core.response

import com.google.gson.annotations.SerializedName

data class UploadDocumentResult(
        var Message: String,
        var Result: Result,
        var StatusNo: Int
)