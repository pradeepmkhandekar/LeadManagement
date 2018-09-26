package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.LoginEntity

data class LoginResponse(
        var Result: LoginEntity
) : APIResponse()