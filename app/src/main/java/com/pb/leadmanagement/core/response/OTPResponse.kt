package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.OTPEntity

data class OTPResponse(
        var Result: OTPEntity

) : APIResponse()