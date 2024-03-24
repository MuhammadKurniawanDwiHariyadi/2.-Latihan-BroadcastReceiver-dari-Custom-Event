package com

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.dicoding.mybroadcastreceiver.ui.SmsReceiverActivity

class SmsReceiver : BroadcastReceiver() {
    // class ini akan dijalankan oleh triger dari manifest

    //  onReceive memproses data dari sms masuk
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {

            // untuk memperoleh data dari sms gunakan fasilitas dari class Telephony.Sms.Intents
            // untuk memperoleh object, gunakan getMessagesFromIntent().
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            /*
            Data akan di kembalikan dari getMessagesFromIntent() dalam bentuk array,
            maka perlu loop untuk mendapatkannya
             */
            for (iterate in messages) {
                val senderNum = iterate.originatingAddress // untuk mendapatkan nomer pengirim gunakan originatingAddress
                val body = iterate.messageBody // untuk mendapatkan isi pesan gunakan messageBody

                Log.d(TAG, "senderNum: $senderNum; message: $iterate")

                // lalu ReceiverActivity akan dijalankan dengan membawa data melalui sebuah Intent showSmsIntent
                val showSmsIntent = Intent(context, SmsReceiverActivity::class.java)
                showSmsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // menjalankan Activity pada task yang berbeda. Bila Activity tersebut sudah ada di dalam stack, maka ia akan ditampilkan ke layar
                showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, senderNum) // mengirim data dengan key
                showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, body) // mengirim data dengan key
                context.startActivity(showSmsIntent) // eksekuis akhir
            }
        }
    }

    companion object {
        private val TAG = SmsReceiver::class.java.simpleName
    }
}