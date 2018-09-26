package com.pb.leadmanagement.core.controller.other

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.requestentity.OtherRequestEntity

/**
 * Created by Nilesh Birhade on 24-09-2018.
 */
interface IOther {

    fun addOtherLead(otherRequestEntity: OtherRequestEntity, iResponseSubcriber: IResponseSubcriber)
}