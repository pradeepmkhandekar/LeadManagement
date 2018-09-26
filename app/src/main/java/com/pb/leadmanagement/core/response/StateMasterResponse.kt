package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.StateMasterEntity

data class StateMasterResponse(
        var StateMasterEntity: List<StateMasterEntity>
) : APIResponse()