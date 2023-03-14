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
import com.example.keyguardian.activities.SecretsListActivity

class LockscreenUtils {

    companion object {
        private const val TAG = "LockscreenUtils"

        /**
         * Check if the device has a lock screen set up (PIN, pattern, password, or biometrics)
         */
        fun hasLockScreen(context: Context): Boolean {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.v(TAG, "App can authenticate using biometrics.")
                    return true
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.e(TAG, "No biometric features available on this device.")
                    return false
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.e(TAG, "Biometric features are currently unavailable.")
                    return false
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.e(
                        TAG, "The user hasn't associated " +
                                "any biometric credentials with their account."
                    )
                    return false
                }
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                    Log.e(TAG, "The security update is required.")
                    return false
                }
            }
            return false
        }
    }
}
