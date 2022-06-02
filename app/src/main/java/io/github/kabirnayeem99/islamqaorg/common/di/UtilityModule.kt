package io.github.kabirnayeem99.islamqaorg.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.common.utility.NetworkUtil

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {

    @Provides
    fun provideNetworkUtil(@ApplicationContext context: Context): NetworkUtil {
        return NetworkUtil(context)
    }
}