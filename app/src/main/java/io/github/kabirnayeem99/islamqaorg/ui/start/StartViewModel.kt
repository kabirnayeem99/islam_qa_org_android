package io.github.kabirnayeem99.islamqaorg.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.data.dataSource.workers.SyncWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val workManager: WorkManager,
) : ViewModel() {

    init {
        syncQuestionsAndAnswers()
    }

    /**
     * Requests work-manager to sync all the questions to the local DB from remote
     */
    fun syncQuestionsAndAnswers() {
        viewModelScope.launch(Dispatchers.Default) {
            workManager.enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
        }
    }
}