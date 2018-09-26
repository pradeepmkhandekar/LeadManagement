package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse

data class MakeModelMasterResponse(
        var VehicleMasterResult: VehicleMasterResult
) : APIResponse()