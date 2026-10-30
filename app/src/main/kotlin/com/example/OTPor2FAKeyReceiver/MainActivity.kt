package com.example.OTPor2FAKeyReceiver

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), KeyReceiver.OtpListener {

    private lateinit var tvOtp: TextView
    private lateinit var tvSender: TextView
    private val smsReceiver = KeyReceiver()
    private val PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple programmatic layout to avoid needing an external layout XML file
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        tvSender = TextView(this).apply {
            text = "Source Name: Waiting..."
            textSize = 18f
            setPadding(0, 0, 0, 30)
        }

        tvOtp = TextView(this).apply {
            text = "OTP Code: Waiting..."
            textSize = 22f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        layout.addView(tvSender)
        layout.addView(tvOtp)
        setContentView(layout)

        KeyReceiver.listener = this

        // Request runtime SMS permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(smsReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(smsReceiver)
    }

    override fun onOtpReceived(otp: String, sender: String) {
        runOnUiThread {
            tvOtp.text = "OTP Code: $otp"
            tvSender.text = "Source Name: $sender"
        }
    }
}
