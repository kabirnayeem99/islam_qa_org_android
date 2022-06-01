package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepository {
    suspend fun getHomeScreenData(): Flow<Resource<List<Question>>>
}