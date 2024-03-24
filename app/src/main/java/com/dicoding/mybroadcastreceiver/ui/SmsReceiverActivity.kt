package com.dicoding.mybroadcastreceiver.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mybroadcastreceiver.R
import com.dicoding.mybroadcastreceiver.databinding.ActivitySmsReceiverBinding

class SmsReceiverActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsReceiverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.incoming_message)

        binding.BTClose.setOnClickListener {
            finish()
        }

        val senderNo = intent.getStringExtra(EXTRA_SMS_NO) // mendapatkan data dari intent
        val senderMessage = intent.getStringExtra(EXTRA_SMS_MESSAGE) // mendapatkan data dari intent

        binding.TVFrom.text = getString(R.string.from, senderNo)
        binding.TVMessage.text = senderMessage


    }

    companion object {
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MESSAGE = "extra_sms_message"
    }
}