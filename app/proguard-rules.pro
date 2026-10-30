# Keep SMS BroadcastReceiver safe from obfuscation stripping
keep class com.example.OTPor2FAKeyReceiver.KeyReceiver { *; }
