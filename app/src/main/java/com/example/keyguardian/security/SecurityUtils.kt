package com.example.keyguardian.security

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.util.Log

class SecurityUtils {

    companion object {
        private const val TAG = "SecurityUtils"

        /**
         * 1.Get the authentication status of the device for biometrics
         * or device credentials (PIN, pattern, password)
         * 2. If the device is not secured, then asks to set a lock screen
         * 3. If the device is secured, then setups a master key
         */
        fun checkAndSet(context: Context): Boolean {
            val authStatus = LockscreenUtils.getDeviceAuthStatus(context)
            if (authStatus == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
                if (!LockscreenUtils.setupLockScreen(context)) {
                    Log.e(TAG, "Failed to setup lock screen: $authStatus")
                    return false
                }
            } else if (authStatus != BiometricManager.BIOMETRIC_SUCCESS) {
                Log.e(TAG, "Failed to setup lock screen: $authStatus")
                return false
            }
            MasterKeyUtils.setupMasterKey(context)
            return true
        }
    }
}