package com.novyapp.test.broadcastreceivercodelab

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        var toastText = ""
        when(intent.action){
            Intent.ACTION_POWER_CONNECTED -> {
                toastText = "CHARGING"
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                toastText = "DISCONNECT"

            }
            ACTION_CUSTOM_BROADCAST -> {
                toastText = "My Broadcast"
            }
        }
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()

    }
}
