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
        fun setupSecurity(context: Context): Boolean {
            val authStatus = LockscreenUtils.getDeviceAuthStatus(context)
            if (authStatus == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
                LockscreenUtils.setupLockScreen(context)
            } else if (authStatus != BiometricManager.BIOMETRIC_SUCCESS) {
                Log.e(TAG, "Failed to setup lock screen: $authStatus")
                throw Exception("Failed to setup lock screen $authStatus")
            }
            MasterKeyUtils.setupMasterKey(context)
            Log.v(TAG, "set up")
            return authStatus == BiometricManager.BIOMETRIC_SUCCESS
        }
    }
}