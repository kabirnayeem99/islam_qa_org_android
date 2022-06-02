package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.codelab.android.datastore.UserPreferences
import io.github.kabirnayeem99.islamqaorg.common.preferences.UserPreferencesSerializer
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private const val USER_PREFERENCES_NAME = "islam_qa_user_preferences"
private const val DATA_STORE_FILE_NAME = "islam_qa_user_prefs.pb"
private const val SHOULD_REFRESH_KEY = "should_refresh"

class PreferenceDataSource @Inject constructor(private val context: Context) {

    private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserPreferencesSerializer
    )

    suspend fun checkIfNeedsRefreshing(): Boolean {
        return (context.userPreferencesStore.data.firstOrNull()?.count ?: 1) % 10 == 0
    }

    suspend fun updateNeedingToRefresh() {
        val count = context.userPreferencesStore.data.firstOrNull()?.count ?: 1
        context.userPreferencesStore.updateData {
            val builder = it.toBuilder()
            builder.count = count + 1
            builder.build()
        }
    }

}