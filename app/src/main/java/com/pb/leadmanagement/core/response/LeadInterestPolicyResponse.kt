package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse

data class LeadInterestPolicyResponse(
        var Products: List<String>
) : APIResponse()