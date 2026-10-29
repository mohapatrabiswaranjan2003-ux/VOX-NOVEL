package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GutendexResponse(
    @Json(name = "count") val count: Int = 0,
    @Json(name = "next") val next: String? = null,
    @Json(name = "previous") val previous: String? = null,
    @Json(name = "results") val results: List<GutendexBook> = emptyList()
)

@JsonClass(generateAdapter = true)
data class GutendexBook(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "authors") val authors: List<GutendexPerson> = emptyList(),
    @Json(name = "subjects") val subjects: List<String> = emptyList(),
    @Json(name = "languages") val languages: List<String> = emptyList(),
    @Json(name = "formats") val formats: Map<String, String> = emptyMap(),
    @Json(name = "download_count") val downloadCount: Int = 0
)

@JsonClass(generateAdapter = true)
data class GutendexPerson(
    @Json(name = "name") val name: String,
    @Json(name = "birth_year") val birthYear: Int? = null,
    @Json(name = "death_year") val deathYear: Int? = null
)

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val subjects: List<String>,
    val languages: List<String>,
    val textUrl: String?,
    val downloadCount: Int,
    val isSaved: Boolean = false
)

data class Chapter(
    val title: String,
    val paragraphs: List<String>
)

data class BookContent(
    val bookId: Int,
    val title: String,
    val author: String,
    val chapters: List<Chapter>
)

enum class VoiceGender {
    ANY,
    MALE,
    FEMALE
}

enum class VoiceTonePreset(val label: String, val pitch: Float) {
    DEEP_BASS("Deep Bass", 0.65f),
    WARM_STORYTELLER("Warm Narrator", 0.85f),
    NATURAL("Natural", 1.0f),
    BRIGHT_CLEAR("Crisp Clear", 1.25f),
    HIGH_TREBLE("High Pitch", 1.6f),
    CUSTOM("Custom", 1.0f)
}

data class VoiceInfo(
    val name: String,
    val localeTag: String,
    val displayName: String,
    val isMale: Boolean,
    val isFemale: Boolean,
    val latency: Int = 0,
    val quality: Int = 0
)

data class VoiceConfig(
    val localeTag: String = "en-US",
    val voiceName: String = "",
    val pitch: Float = 1.0f,
    val speechRate: Float = 1.0f,
    val gender: VoiceGender = VoiceGender.ANY,
    val tonePreset: VoiceTonePreset = VoiceTonePreset.NATURAL
)
