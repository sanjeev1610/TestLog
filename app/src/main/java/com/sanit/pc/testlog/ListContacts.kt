package com.sanit.pc.testlog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.row_item_contacts.view.*

class ListContacts(val context: Context, val list:ArrayList<Contacts>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_item_contacts,parent,false)
        view.tv_contact_name.text = "Name: ${list[position].name}"
        view.tv_contact_phone.text = "Phone: ${list[position].phone}"

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