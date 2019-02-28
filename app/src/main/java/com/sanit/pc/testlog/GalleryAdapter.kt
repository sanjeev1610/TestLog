package com.sanit.pc.testlog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.row_gallery_item.view.*
import java.io.File

class GalleryAdapter(var context:MainActivity, var list:Array<File>) : BaseAdapter(){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_gallery_item,parent,false)
        var file = list[position]
        var urii = Uri.fromFile(file)
        val bm = Bitmap.createScaledBitmap(urii as Bitmap,200,200,true)
        view.imageView_gallery.setImageBitmap(bm)
        view.tv_gallary_name.text = file.name
        //view.textView_filesize.text = file.length().toString()
//        view.button_delete_item.setOnClickListener {
//            file.delete()
//            context.readFiles()
//        }
        return view
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

}