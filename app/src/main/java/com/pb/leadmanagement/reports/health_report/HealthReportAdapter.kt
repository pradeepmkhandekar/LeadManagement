package com.pb.leadmanagement.reports.health_report

import android.content.Context
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pb.leadmanagement.core.requestentity.HealthLeadRequestEntity

/**
 * Created by IN-RB on 04-10-2018.
 */
class HealthReportAdapter (val healthList : List<HealthLeadRequestEntity>, val context: Context) :
        RecyclerView.Adapter<HealthReportAdapter.HealthItem>() {
    override fun onBindViewHolder(holder: HealthItem, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthItem {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class HealthItem(itemView: View): RecyclerView.ViewHolder(itemView) {

    }




}