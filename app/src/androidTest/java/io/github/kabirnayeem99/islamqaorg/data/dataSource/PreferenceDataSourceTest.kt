package io.github.kabirnayeem99.islamqaorg.data.dataSource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PreferenceDataSourceTesting {

    lateinit var pref: PreferenceDataSource

    @Before
    fun createPreferenceDataSource() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        pref = PreferenceDataSource(context)
    }

    //_and_then_after_updating_should_be_true
    @Test
    fun checkIfNeedsSyncing_should_be_true_first() {
        runBlocking {
            val needRefresh = pref.checkIfNeedsSyncing()
            assert(needRefresh)
        }
    }

    @Test
    fun updateNeedingToRefresh_checkIfNeedsSyncing__should_be_false_after_first_call() {
        runBlocking {
            pref.updateNeedingToRefresh()
            val needRefresh = pref.checkIfNeedsSyncing()
            assert(needRefresh)
        }
    }

    @Test
    fun savePreferredFiqh_getPreferredFiqh_given_a_predefined_fiqh_should_return_it() {
        val fiqhParam = Fiqh.HANBALI
        runBlocking {
            pref.savePreferredFiqh(fiqhParam)
            val fiqh = pref.getPreferredFiqh()
            assert(fiqh == fiqhParam)
        }
    }
}