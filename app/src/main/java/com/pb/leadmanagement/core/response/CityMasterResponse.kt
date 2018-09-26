package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.CityMasterEntity

data class CityMasterResponse(
        var CityMasterResult: List<CityMasterEntity>
) : APIResponse()