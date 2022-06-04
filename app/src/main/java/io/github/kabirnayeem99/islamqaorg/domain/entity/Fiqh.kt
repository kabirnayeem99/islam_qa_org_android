package io.github.kabirnayeem99.islamqaorg.domain.entity

/**
 * Fiqh is Islamic jurisprudence. Fiqh is often described as the human understanding and practices
 * of the sharia, that is human understanding of the divine Islamic law as revealed in the Quran
 * and the Sunnah.
 *
 * @property paramName String - the name of the Fiqh to pass to IslamQA
 */
enum class Fiqh(val paramName: String, val displayName: String) {
    HANAFI("hanafi", "Hanafi"),
    MALIKI("maliki", "Maliki"),
    SHAFII("shafii", "Shafii"),
    HANBALI("hanbali", "Hanbali"),
    UNKNOWN("", ""),
}