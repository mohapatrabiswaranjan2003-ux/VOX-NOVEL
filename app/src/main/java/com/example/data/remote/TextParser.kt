package com.example.data.remote

import com.example.data.model.BookContent
import com.example.data.model.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern

object TextParser {
    private val client = OkHttpClient()

    suspend fun fetchBookText(url: String): String = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "VoxNovel-Android-App/1.0")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Failed to download text: ${response.code}")
            response.body?.string() ?: ""
        }
    }

    fun parseBookContent(bookId: Int, title: String, author: String, rawText: String): BookContent {
        val cleanText = stripGutenbergHeaderFooter(rawText)
        val chapters = splitIntoChapters(cleanText)
        return BookContent(
            bookId = bookId,
            title = title,
            author = author,
            chapters = chapters
        )
    }

    private fun stripGutenbergHeaderFooter(text: String): String {
        var processed = text
        val startPattern = Pattern.compile("\\*\\*\\*\\s*START OF (THE|THIS) PROJECT GUTENBERG.*\\*\\*\\*", Pattern.CASE_INSENSITIVE)
        val startMatcher = startPattern.matcher(processed)
        if (startMatcher.find()) {
            processed = processed.substring(startMatcher.end())
        }

        val endPattern = Pattern.compile("\\*\\*\\*\\s*END OF (THE|THIS) PROJECT GUTENBERG", Pattern.CASE_INSENSITIVE)
        val endMatcher = endPattern.matcher(processed)
        if (endMatcher.find()) {
            processed = processed.substring(0, endMatcher.start())
        }

        return processed.trim()
    }

    private fun splitIntoChapters(text: String): List<Chapter> {
        val chapterRegex = Regex("(?m)^\\s*(CHAPTER|Chapter|Book|BOOK|Part|PART|ACT|Act)\\s+([0-9IVXLCDM]+|[A-Za-z]+).*$")
        val matches = chapterRegex.findAll(text).toList()

        if (matches.size >= 2) {
            val chapters = mutableListOf<Chapter>()
            for (i in matches.indices) {
                val currentMatch = matches[i]
                val nextStart = if (i + 1 < matches.size) matches[i + 1].range.first else text.length
                val chapterTitle = currentMatch.value.trim()
                val chapterBody = text.substring(currentMatch.range.last + 1, nextStart).trim()
                val paragraphs = splitParagraphs(chapterBody)
                if (paragraphs.isNotEmpty()) {
                    chapters.add(Chapter(title = chapterTitle, paragraphs = paragraphs))
                }
            }
            if (chapters.isNotEmpty()) return chapters
        }

        // Fallback: group paragraphs into logical segments
        val allParagraphs = splitParagraphs(text)
        if (allParagraphs.isEmpty()) {
            return listOf(Chapter(title = "Chapter 1", paragraphs = listOf("No text available.")))
        }

        val chunkSize = 20
        return allParagraphs.chunked(chunkSize).mapIndexed { index, chunk ->
            Chapter(title = "Part ${index + 1}", paragraphs = chunk)
        }
    }

    private fun splitParagraphs(text: String): List<String> {
        return text.split(Regex("\\r?\\n\\s*\\r?\\n+"))
            .map { it.replace(Regex("\\s+"), " ").trim() }
            .filter { it.length > 5 }
    }
}
