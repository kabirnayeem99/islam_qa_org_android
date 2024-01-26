package io.github.kabirnayeem99.islamqaorg.data.dataSource

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

private const val FETCH_COUNT = "fetch_count"
private const val PREFERRED_FIQH = "preferred_fiqh"
private const val FIQH_LAST_PAGE = "preferred_fiqh"
private const val LAST_SYNC_TIME = "last_sync_time"
private const val SYNC_INTERVAL_TIME_IN_MILLIS = 604_800_000 // 7 days

class PreferenceDataSource @Inject constructor(private val context: Context) {


    private val defaultPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
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
                Timber.e(e, "Failed to update needing to refresh -> ${e.message}")
            }
        }
    }


    /**
     * Checks if the last sync time is greater than the sync interval time
     *
     * @return A Boolean value, whether needs syncing or not
     */
    suspend fun checkIfNeedsSyncing(): Boolean {
        val doesNeedSyncing = withContext(Dispatchers.IO) {
            try {
                val lastSyncTimeInMillis = defaultPrefs.getLong(LAST_SYNC_TIME, 0)
                Timber.d("Last sync time in millis -> $lastSyncTimeInMillis")
                if (lastSyncTimeInMillis == 0L) true
                else {
                    val currentTimeInMillis = Date().time
                    val interval = currentTimeInMillis - lastSyncTimeInMillis
                    interval > SYNC_INTERVAL_TIME_IN_MILLIS
                }
            } catch (e: Exception) {
                true
            }
        }
        Timber.d("Needs syncing or not -> $doesNeedSyncing")
        return doesNeedSyncing
    }

    /**
     * Updates the last sync time in the preference data storage
     */
    suspend fun updateSyncingStatus() {
        withContext(Dispatchers.IO) {
            try {
                val date = Date()
                val dateInMillis = date.time
                defaultPrefs.edit().putLong(LAST_SYNC_TIME, dateInMillis).apply()
            } catch (e: Exception) {
                Timber.e(e, "Failed to update syncing status -> ${e.message}")
            }
        }
    }

    private val lastSyncedLock = Mutex()

    suspend fun saveCurrentFiqhLastPageSynced(lastPage: Int) {
        lastSyncedLock.withLock {
            try {
                val fiqh = getPreferredFiqh()
                defaultPrefs.edit { it.putInt(FIQH_LAST_PAGE + fiqh.paramName, lastPage) }
            } catch (e: Exception) {
                Timber.e(e, "saveCurrentFiqhLastPageSynced: " + e.message)
            }
        }
    }

    suspend fun getCurrentFiqhLastPageSynced(): Int {
        lastSyncedLock.withLock {
            try {
                val fiqh = getPreferredFiqh()
                return defaultPrefs.getInt(FIQH_LAST_PAGE + fiqh.paramName, 0)
            } catch (e: Exception) {
                Timber.e("getCurrentFiqhLastPageSynced: ${e.localizedMessage}")
                return 0
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
                Timber.e(e, "Failed to save preferred fiqh -> ${e.message}")
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
                when (defaultPrefs.getString(PREFERRED_FIQH, Fiqh.HANAFI.paramName)) {
                    Fiqh.HANAFI.paramName -> Fiqh.HANAFI
                    Fiqh.SHAFII.paramName -> Fiqh.SHAFII
                    Fiqh.MALIKI.paramName -> Fiqh.MALIKI
                    Fiqh.HANBALI.paramName -> Fiqh.HANBALI
                    else -> Fiqh.UNKNOWN
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to savePreferredFiqh -> ${e.message}")
                Fiqh.HANAFI
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

private const val TAG = "PreferenceDataSource"