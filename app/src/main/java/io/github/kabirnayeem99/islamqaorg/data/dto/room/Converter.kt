package io.github.kabirnayeem99.islamqaorg.data.dto.room

import androidx.room.TypeConverter
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import timber.log.Timber

class Converter {

    @TypeConverter
    fun fiqhToString(fiqh: Fiqh): String {
        return fiqh.paramName
    }

    @TypeConverter
    fun stringToFiqh(paramName: String): Fiqh {
        return try {
            Fiqh.values().first { it.paramName == paramName }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get fiqh based on param name")
            Fiqh.UNKNOWN
        }
    }
}