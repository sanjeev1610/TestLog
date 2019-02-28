package com.sanit.pc.testlog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val requstCode: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requstCode)
        }else{
            loadData(this)

        }

    }//onCreate()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == requstCode && grantResults[0] == Activity.RESULT_OK){
            loadData(this)
        }
    }

    private fun loadData(context:Context) {
        
        var list = getCallDetails(context)
        var adaptr = ListAdapter(context,list)
        list_view.adapter = adaptr
        var contactList = getContacts(context)
        list_view1.adapter = ListContacts(this,contactList)
        readFiles()

    }

    private fun getContacts(context: Context): ArrayList<Contacts> {
        val contactList = ArrayList<Contacts>()

            val cusrsor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

        if(cusrsor.moveToFirst()){
            do {
                val name = cusrsor.getString(cusrsor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone = cusrsor.getString(cusrsor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactList.add(
                    Contacts(
                    name,
                    phone
                )
                )
            }while (cusrsor.moveToNext())
        }
        return contactList
    }

    private fun getCallDetails(context: Context): ArrayList<CallDetails> {
        var callDDetails = ArrayList<CallDetails>()
        val contentURI = CallLog.Calls.CONTENT_URI

        try {

            val cr = context.contentResolver
            val cursor = cr.query(
                contentURI,
                null,
                null,
                null,
                null
            )
            val nameUri = cursor.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)


            if (cursor.moveToFirst()){
                do {
                    val callType =  when(cursor.getInt(type)){
                        CallLog.Calls.INCOMING_TYPE -> "Incoming Call"
                        CallLog.Calls.OUTGOING_TYPE -> "OutGOing Call"
                        CallLog.Calls.MISSED_TYPE -> "Missed Call"
                        CallLog.Calls.REJECTED_TYPE -> "Rejected Call"
                        else -> "Not defined"
                    }
                    val phoneNumber = cursor.getString(number)
                    val callDuration = cursor.getString(duration)
                    val calldate = cursor.getString(date)
                    val dayTime = Date(calldate.toLong()).toString()
                    val callerNameUri = cursor.getString(nameUri)
                    callDDetails.add(CallDetails(
                        getCallerName(callerNameUri),
                        phoneNumber,
                        callDuration,
                        callType,
                        dayTime
                    ))

                }while (cursor.moveToNext())

            }


        }catch (e: SecurityException){
            Toast.makeText(this,""+e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

        return callDDetails

    }

    private fun getCallerName(callerNameUri: String?): String{

        return if(callerNameUri!=null){
            var name = ""
            val cursor = contentResolver.query(Uri.parse(callerNameUri),
                null,null,null,null)
            if((cursor?.count ?: 0)>0){
                while (cursor!=null&& cursor.moveToNext()){
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }
            }
            return name
        }else{
            "not a contact"
        }
    }

    fun readFiles() {
//        var path = "/storage/sdcard0/WhatsApp/Media/WhatsApp Images/"
        var path = "/storage/sdcard0/DCIM/Camera/"
        var file = File(path)
        if(!file.exists())
        {
//            path = "/storage/emulated/0/WhatsApp/Media/WhatsApp Images/"
            path = "/storage/emulated/0/DCIM/Camera/"
            file = File(path)
        }
        val files = file.listFiles()
//         val files = file.list() ->for Array adapter Array<File>
        list_view2.adapter = GalleryAdapter(this@MainActivity,files)
    }

}
