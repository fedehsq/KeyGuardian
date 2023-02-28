import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.keyguardian.security.MasterKeyUtils

class EncryptedSharedPreferencesUtils private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_settings",
            MasterKeyUtils.getMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getAll(): Map<String, *> {
        // get all as list
        return sharedPreferences.all
    }

    fun getKeys(): Set<String> {
        // get all keys
        return sharedPreferences.all.keys
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    companion object {

        @Volatile
        private var instance: EncryptedSharedPreferencesUtils? = null

        fun getInstance(context: Context): EncryptedSharedPreferencesUtils {
            return instance ?: synchronized(this) {
                instance ?: EncryptedSharedPreferencesUtils(context).also { instance = it }
            }
        }
    }
}
