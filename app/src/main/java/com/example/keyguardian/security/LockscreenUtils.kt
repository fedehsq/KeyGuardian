import android.app.Activity
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.keyguardian.activities.MainActivity

class LockscreenUtils {

    companion object {
        private const val TAG = "LockscreenUtils"
        private lateinit var requestPinSetup: ActivityResultLauncher<IntentSenderRequest>

        /**
         * Get the authentication status of the device for biometrics
         * or device credentials (PIN, pattern, password) and return the code
         */
        fun getDeviceAuthStatus(context: Context): Int {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.v(TAG, "App can authenticate using biometrics.")
                    return BiometricManager.BIOMETRIC_SUCCESS
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.e(TAG, "No biometric features available on this device.")
                    return BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.e(TAG, "Biometric features are currently unavailable.")
                    return BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.e(
                        TAG, "The user hasn't associated " +
                                "any biometric credentials with their account."
                    )
                    return BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                }
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                    Log.e(TAG, "The security update is required.")
                    return BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED
                }
            }
            throw Exception("Unknown error")
        }

        private fun setPinCallback(activity: ComponentActivity) {
            requestPinSetup = activity.registerForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult()
            ) { result ->
                Log.v(TAG, "Pin setup result: $result")
                val lockScreenEnabled =
                    getDeviceAuthStatus(activity) == BiometricManager.BIOMETRIC_SUCCESS
                if (!lockScreenEnabled) {
                    Log.e(TAG, "Lock screen not set up")
                    Toast.makeText(
                        activity,
                        "You must set up a lock screen to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.v(TAG, "Lock screen set up")
                    // Launch MainActivity
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                }

            }
        }

        fun setupLockScreen(context: Context) {
            if (!this::requestPinSetup.isInitialized) {
                setPinCallback(context as ComponentActivity)
            }
            val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
            if (intent.resolveActivity(context.packageManager) != null) {
                val intentSender = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                ).intentSender
                val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
                requestPinSetup.launch(intentSenderRequest)
            } else {
                Toast.makeText(context, "No activity found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
