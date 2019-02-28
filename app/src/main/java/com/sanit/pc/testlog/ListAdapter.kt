package com.sanit.pc.testlog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.row_item.view.*

class ListAdapter(val context:Context, val list:ArrayList<CallDetails>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_item,parent,false)
        view.tv_call_log.text = "Caller Name:  ${list[position].name}\n" +
                "Phone Number: ${list[position].number}\n" +
                "Call duration: ${list[position].duration}\n" +
                "Call type: ${list[position].type}\n" +
                "Csll time: ${list[position].dayTime}"
        return view
    }

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}