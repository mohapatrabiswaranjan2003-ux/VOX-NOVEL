package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_books")
data class SavedBookEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val subjects: String,
    val languages: String,
    val fullText: String?,
    val lastChapterIndex: Int = 0,
    val lastParagraphIndex: Int = 0,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = true
)
