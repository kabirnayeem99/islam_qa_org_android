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
import javax.inject.Inject

private const val PREFERRED_FIQH = "preferred_fiqh"
private const val FIQH_LAST_PAGE = "preferred_fiqh"
private const val FIRST_TIME_OPEN = "first_time_open"

class PreferenceDataSource @Inject constructor(private val context: Context) {

    private val defaultPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
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

    suspend fun markAsOpened() {
        withContext(Dispatchers.IO) {
            defaultPrefs.edit { dp -> dp.putBoolean(FIRST_TIME_OPEN, false) }
        }
    }

    suspend fun determineIfFirstTime(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val isFirstTime = defaultPrefs.getBoolean(FIRST_TIME_OPEN, true)
                val selectedFiqh = defaultPrefs.getString(PREFERRED_FIQH, "") ?: ""

                isFirstTime || selectedFiqh.isBlank()
            } catch (e: Exception) {
                Timber.e(e)
                true
            }
        }
    }


    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

}
