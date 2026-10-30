# OTP or 2FA Key Receiver

A lightweight, open-source Android utility application designed to automatically capture and parse incoming SMS One-Time Passwords (OTP) and 2FA authentication keys.

## Features
* **SMS Reception:** Listens for incoming SMS messages securely on your device.
* **OTP Parsing:** Extracts verification codes and sender names for quick reference.
* **Open Source:** Fully inspectable source code—build it yourself or review every line.

## Privacy & Security
This application runs entirely locally on your device. It does not collect, store, or transmit any personal data, SMS contents, or authentication tokens to any external servers. 

## Automated Builds
This repository utilizes **GitHub Actions** to compile and package the APK directly from the source code, ensuring total transparency and reproducibility.

*(Note: Because this app requests SMS receiver permissions for local parsing, heuristic antivirus scanners may occasionally flag side-loaded APK downloads as false positives. You can review the complete source code in this repository to verify its safety.)*
