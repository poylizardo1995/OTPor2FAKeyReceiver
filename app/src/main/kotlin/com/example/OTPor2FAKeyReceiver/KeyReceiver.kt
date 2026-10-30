package com.example.OTPor2FAKeyReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class KeyReceiver : BroadcastReceiver() {

    interface OtpListener {
        fun onOtpReceived(otp: String, sender: String)
    }

    companion object {
        var listener: OtpListener? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = smsMessage.originatingAddress ?: "Unknown"
                    val messageBody = smsMessage.messageBody ?: ""

                    // Regex pattern to capture a 4 to 6 digit OTP code
                    val otpRegex = Regex("\\b\\d{4,6}\\b")
                    val matchResult = otpRegex.find(messageBody)

                    if (matchResult != null) {
                        val otpCode = matchResult.value
                        listener?.onOtpReceived(otpCode, sender)
                    }
                }
            }
        }
    }
}
