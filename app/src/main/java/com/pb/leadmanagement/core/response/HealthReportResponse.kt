package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.requestentity.HealthLeadRequestEntity

data class HealthReportResponse(
        val Leads: List<HealthLeadRequestEntity>
):APIResponse()