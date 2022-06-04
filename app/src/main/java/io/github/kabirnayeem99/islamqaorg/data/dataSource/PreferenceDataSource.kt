package io.github.kabirnayeem99.islamqaorg.data.dataSource

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val FETCH_COUNT = "fetch_count"
private const val PREFERRED_FIQH = "preferred_fiqh"

class PreferenceDataSource @Inject constructor(private val context: Context) {


    private val defaultPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * Checks if the user needs to refresh their data
     *
     * @return A Boolean
     */
    suspend fun checkIfNeedsRefreshing(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val currentFetchCount = defaultPrefs.getInt(FETCH_COUNT, 1)
                currentFetchCount % 10 == 0
            } catch (e: Exception) {
                Timber.e(e, "Failed to find checkIfNeedsRefreshing -> ${e.message}")
                true
            }
        }
    }

    /**
     * Notifies whether the data needs to update or not
     */
    suspend fun updateNeedingToRefresh() {
        withContext(Dispatchers.IO) {
            try {
                val currentFetchCount = defaultPrefs.getInt(FETCH_COUNT, 1)
                defaultPrefs.edit { it.putInt(FETCH_COUNT, currentFetchCount + 1) }
            } catch (e: Exception) {
                Timber.e(e, "Failed to updateNeedingToRefresh -> ${e.message}")
            }
        }
    }

    /**
     * Saves the preferred fiqh of the user to local storage
     *
     * @param fiqh Fiqh - This is the object that contains the fiqh information.
     */
    suspend fun savePreferredFiqh(fiqh: Fiqh) {
        withContext(Dispatchers.IO) {
            try {
                val fiqhParamName = fiqh.paramName
                defaultPrefs.edit { it.putString(PREFERRED_FIQH, fiqhParamName) }
            } catch (e: Exception) {
                Timber.e(e, "Failed to savePreferredFiqh -> ${e.message}")
            }
        }
    }

    /**
     * Gets the selected [Fiqh] by the user, which can be either Hanafi, Shafii, Maliki, Hanbali
     * or unknown
     *
     * @return Fiqh - the Fiqh the user has selected before.
     */
    suspend fun getPreferredFiqh(): Fiqh {
        return withContext(Dispatchers.IO) {
            try {
                when (defaultPrefs.getString(PREFERRED_FIQH, Fiqh.UNKNOWN.paramName)) {
                    Fiqh.HANAFI.paramName -> Fiqh.HANAFI
                    Fiqh.SHAFII.paramName -> Fiqh.SHAFII
                    Fiqh.MALIKI.paramName -> Fiqh.MALIKI
                    Fiqh.HANBALI.paramName -> Fiqh.HANBALI
                    else -> Fiqh.UNKNOWN
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to savePreferredFiqh -> ${e.message}")
                Fiqh.UNKNOWN
            }
        }
    }


    /**
     * Takes a lambda as a parameter, and calls it with a SharedPreferences.Editor as a parameter
     *
     * @param operation This is a lambda function that takes a SharedPreferences.Editor as a parameter
     * and returns nothing.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

}