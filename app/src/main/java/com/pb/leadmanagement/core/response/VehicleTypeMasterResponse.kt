package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.VehicleTypeMasterEntity

data class VehicleTypeMasterResponse(
        var VehicleTypeMasterEntity: List<VehicleTypeMasterEntity>
) : APIResponse()