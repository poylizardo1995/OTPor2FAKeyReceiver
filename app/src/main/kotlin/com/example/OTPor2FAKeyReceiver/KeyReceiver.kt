package com.example.OTPor2FAKeyReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import java.util.regex.Pattern

class KeyReceiver : BroadcastReceiver() {

    private val allowedSenders = listOf("GCash", "PayMaya", "Maya", "Google", "AUTHMSG")

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>?
                val format = bundle.getString("format")

                pdus?.forEach { pdu ->
                    val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                    val sender = sms.displayOriginatingAddress ?: "Unknown"
                    val messageBody = sms.displayMessageBody ?: ""

                    val isAllowedSender = allowedSenders.any { allowed ->
                        sender.contains(allowed, ignoreCase = true)
                    }

                    if (isAllowedSender) {
                        val otpCode = extractOtpCode(messageBody)

                        val updateIntent = Intent("com.example.OTP_UPDATED").apply {
                            putExtra("sender", sender)
                            putExtra("otp", otpCode ?: "No OTP found in text")
                            putExtra("full_text", messageBody)
                        }
                        context?.sendBroadcast(updateIntent)
                    }
                }
            }
        }
    }

    private fun extractOtpCode(message: String): String? {
        val pattern = Pattern.compile("\\b\\d{4,8}\\b")
        val matcher = pattern.matcher(message)
        return if (matcher.find()) {
            matcher.group(0)
        } else {
            null
        }
    }
}
