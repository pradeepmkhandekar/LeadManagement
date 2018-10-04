package com.pb.leadmanagement.core.requestentity

data class UploadDocRequestEntity(
        var ProductID: Int,
        var DocType: String,
        var LeadID: Int,
        var PDFBase64String: String
)