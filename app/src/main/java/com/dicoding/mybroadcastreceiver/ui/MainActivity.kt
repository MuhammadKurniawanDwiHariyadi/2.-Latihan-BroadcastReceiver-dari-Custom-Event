package com.dicoding.mybroadcastreceiver.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mybroadcastreceiver.R
import com.dicoding.mybroadcastreceiver.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityMainBinding? = null

    private lateinit var downloadReceiver: BroadcastReceiver

    var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Sms receiver permission diterima", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Sms receiver permission ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(binding?.root)

        binding?.BTPermission?.setOnClickListener(this)

        // ini merupakan BroadcastReceiver yang ada di class/activity
        /*
        Berbeda dengan sebelumnya yang mana broadcastnya berada pada file lain dan di daftarkan di manifest,
        disini didaftarkan pada activity dengan fungsi registerReceiver yang berisi BroadcastReceiver
        yang kita buat dan IntentFilter untuk mendefinisikan Action yang ingin dipantau

        jadi ketika baris 79 di jalankan, maka seketika onReceive() dibawah ini akan merespon
         */
        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Toast.makeText(context, "Download Selesai", Toast.LENGTH_SHORT).show() // hanya menampilkans ebuah toast
            }
        }
        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS)
        registerReceiver(downloadReceiver, downloadIntentFilter)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.BTPermission -> {
                requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
            }
            R.id.BTDownload -> {
                //simulate download process in 3 seconds
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        val notifyFinishIntent = Intent().setAction(ACTION_DOWNLOAD_STATUS)
                        sendBroadcast(notifyFinishIntent)
                    },
                    3000
                )
                /*
                    Di sini melakukan proses mengunduh file secara Asynchronous di background.
                    Pada kenyataanya, hanya menyimulasikannya menggunakan Handler.postDelayed selama 5 detik
                    dan kemudian mem-broadcast sebuah Intent dengan Action yang telah di buat sendiri,
                    yakni ACTION_DOWNLOAD_STATUS
                 */
            }
        }
    }
    companion object {
        const val ACTION_DOWNLOAD_STATUS = "download_status"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        unregisterReceiver(downloadReceiver) // jangan lupa untuk mencopot object receiver yang di regsitrasikan saat activity di matikan
    }
}