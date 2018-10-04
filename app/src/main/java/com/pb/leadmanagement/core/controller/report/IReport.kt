package com.pb.leadmanagement.core.controller.report

import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.model.LoginEntity

/**
 * Created by IN-RB on 03-10-2018.
 */
interface IReport {

    fun getHealthReport(fromDate: String,toDate: String,UserId: String, iResponseSubcriber: IResponseSubcriber)
}