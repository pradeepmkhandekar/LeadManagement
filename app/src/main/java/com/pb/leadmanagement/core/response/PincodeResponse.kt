package com.pb.leadmanagement.core.response

import com.android.chemistlead.core.APIResponse

data class PincodeResponse(
        var MasterData: PincodeEntity
) : APIResponse()