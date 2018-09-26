package com.pb.leadmanagement.motor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.response.Variant

class VariantAdapter(val context: Context, var listVariant: List<Variant>) : BaseAdapter() {


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

     /*   val params = view.layoutParams
        params.height = 60
        view.layoutParams = params*/

        vh.label.text = listVariant.get(position).Variant
        return view
    }

    override fun getItem(position: Int): Variant? {

        return listVariant.get(position)

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {
        return listVariant.size
    }

    private class ItemRowHolder(row: View?) {

        val label: TextView

        init {
            this.label = row?.findViewById(R.id.lbl_name) as TextView
            this.label.setTextColor(row?.context.resources.getColor(R.color.textTitle))
        }
    }
}