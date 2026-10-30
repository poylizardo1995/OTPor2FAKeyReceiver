package com.example.OTPor2FAKeyReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ClipboardManager
import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.OTPor2FAKeyReceiver.R

class MainActivity : AppCompatActivity() {

    private lateinit var tvSource: TextView
    private lateinit var tvOtp: TextView
    private lateinit var btnCopy: Button
    private var currentOtpCode: String = ""

    private val otpUpdateReceiver = object : BroadcastReceiver() {
        onReceive(context: Context?, intent: Intent?) {
            val sender = intent?.getStringExtra("sender") ?: "Unknown"
            val otp = intent?.getStringExtra("otp") ?: ""

            currentOtpCode = otp
            tvSource.text = "Source Name: $sender"
            tvOtp.text = "OTP Code: $otp"
            btnCopy.isEnabled = otp.isNotEmpty() && otp.all { it.isDigit() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSource = findViewById(R.id.tvSource)
        tvOtp = findViewById(R.id.tvOtp)
        btnCopy = findViewById(R.id.btnCopy)

        btnCopy.setOnClickListener {
            copyOtpToClipboard(currentOtpCode)
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.example.OTP_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(otpUpdateReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(otpUpdateReceiver, filter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(otpUpdateReceiver)
    }

    private fun copyOtpToClipboard(code: String) {
        if (code.isNotEmpty()) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("OTP Code", code)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "OTP Code copied: $code", Toast.LENGTH_SHORT).show()
        }
    }
}
