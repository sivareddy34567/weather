package com.example.login.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.login.MainActivity
import com.example.login.R

class Utils {
 companion object {
     fun showAlert(context: MainActivity){
         val builder = AlertDialog.Builder(context)
         //set title for alert dialog
         builder.setTitle(R.string.dialogTitle)
         //set message for alert dialog
         builder.setMessage(R.string.dialogMessage)
         builder.setIcon(android.R.drawable.ic_dialog_alert)

         //performing positive action
         builder.setPositiveButton("Yes"){dialogInterface, which ->
             Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
         }

         //performing negative action
         builder.setNegativeButton("No"){dialogInterface, which ->
             Toast.makeText(context,"clicked No",Toast.LENGTH_LONG).show()
         }
         // Create the AlertDialog
         val alertDialog: AlertDialog = builder.create()
         // Set other dialog properties
         alertDialog.setCancelable(false)
         alertDialog.show()
     }


 }




}