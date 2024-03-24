package com.dicoding.mybroadcastreceiver.ui

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
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
    private lateinit var binding: ActivityMainBinding


//    var requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            Toast.makeText(this, "Sms receiver permission diterima", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Sms receiver permission ditolak", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(binding.root)

        binding.BTPermission.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.BTPermission -> {
                // normalnya
//                requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
                // dengan dexter
                Dexter.withContext(this).withPermission(Manifest.permission.RECEIVE_SMS)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            Toast.makeText(
                                this@MainActivity,
                                "Sms receiver permission diterima",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {
                            if (response.isPermanentlyDenied) {
                                val snackbar = Snackbar.make(
                                    binding.root,
                                    "Permission ditolak, Anda perlu mengaktifkannya di pengaturan aplikasi",
                                    Snackbar.LENGTH_LONG
                                )
                                snackbar.setAction("PENGATURAN") {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startActivity(intent)
                                }
                                snackbar.show()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest, token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
            }

        }
    }
}