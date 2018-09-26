package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.RegisterEntity

data class RegisterResponse(
        var Result: RegisterEntity

) : APIResponse()