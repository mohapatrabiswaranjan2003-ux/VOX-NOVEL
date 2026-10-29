package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM saved_books ORDER BY lastReadTimestamp DESC")
    fun getAllSavedBooks(): Flow<List<SavedBookEntity>>

    @Query("SELECT * FROM saved_books WHERE id = :id")
    suspend fun getBookById(id: Int): SavedBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: SavedBookEntity)

    @Query("UPDATE saved_books SET lastChapterIndex = :chapter, lastParagraphIndex = :paragraph, lastReadTimestamp = :time WHERE id = :id")
    suspend fun updateProgress(id: Int, chapter: Int, paragraph: Int, time: Long)

    @Query("DELETE FROM saved_books WHERE id = :id")
    suspend fun deleteBook(id: Int)
}
