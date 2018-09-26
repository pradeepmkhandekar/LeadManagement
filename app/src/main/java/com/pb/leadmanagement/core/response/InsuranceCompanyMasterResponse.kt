package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.model.InsuranceCompanyMasterEntity

data class InsuranceCompanyMasterResponse(
        var InsuranceCompanyMasterResult: List<InsuranceCompanyMasterEntity>
) : APIResponse()