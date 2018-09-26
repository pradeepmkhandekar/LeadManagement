package com.pb.leadmanagement.health

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.model.InsuranceCompanyMasterEntity
import com.pb.leadmanagement.core.response.Variant

class InsurerAdapter(val context: Context, var listInsurer: List<InsuranceCompanyMasterEntity>) : BaseAdapter() {


    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.layout_people, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.

       // val params = view.layoutParams
       // params.height = 60
       // view.layoutParams = params

        vh.label.text = listInsurer.get(position).InsCompanyName
        return view
    }

    override fun getItem(position: Int): InsuranceCompanyMasterEntity? {

        return listInsurer.get(position)

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {
        return listInsurer.size
    }

    private class ItemRowHolder(row: View?) {

        val label: TextView

        init {
            this.label = row?.findViewById(R.id.lbl_name) as TextView
            this.label.setTextColor(row?.context.resources.getColor(R.color.textTitle))
        }
    }
}