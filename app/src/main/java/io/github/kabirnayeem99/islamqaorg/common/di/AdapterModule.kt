package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionAdapter
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionSliderAdapter

@Module
@InstallIn(FragmentComponent::class)
object AdapterModule {
    @Provides
    fun provideQuestionAdapter(): QuestionAdapter {
        return QuestionAdapter()
    }

    @Provides
    fun provideQuestionSliderAdapter(): QuestionSliderAdapter {
        return QuestionSliderAdapter()
    }
}