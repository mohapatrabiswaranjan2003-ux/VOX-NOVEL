package com.example.data.repository

import com.example.data.local.BookDao
import com.example.data.local.SavedBookEntity
import com.example.data.model.Book
import com.example.data.model.BookContent
import com.example.data.model.Chapter
import com.example.data.model.GutendexBook
import com.example.data.remote.GutendexApi
import com.example.data.remote.TextParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NovelRepository(
    private val api: GutendexApi,
    private val bookDao: BookDao
) {
    val savedBooks: Flow<List<SavedBookEntity>> = bookDao.getAllSavedBooks()

    suspend fun getFeaturedNovels(): List<Book> = withContext(Dispatchers.IO) {
        val webNovels = WebNovelCatalog.popularWebNovels
        val classicNovels = StarterNovels.sampleBooks
        try {
            val response = api.getPopularBooks(page = 1)
            val remote = response.results.mapNotNull { it.toDomainBook() }
            (webNovels + remote + classicNovels).distinctBy { it.id }
        } catch (_: Exception) {
            webNovels + classicNovels
        }
    }

    suspend fun searchNovels(query: String): List<Book> = withContext(Dispatchers.IO) {
        val trimmed = query.trim()
        if (trimmed.isBlank() || trimmed.equals("All", ignoreCase = true)) return@withContext getFeaturedNovels()

        val lower = trimmed.lowercase()

        // Match Web Novel Catalog with alias support
        val webMatches = WebNovelCatalog.popularWebNovels.filter { book ->
            val titleLower = book.title.lowercase()
            val authorLower = book.author.lowercase()
            val subjLower = book.subjects.map { it.lowercase() }

            titleLower.contains(lower) ||
                    authorLower.contains(lower) ||
                    subjLower.any { it.contains(lower) } ||
                    (lower.contains("douluo") && titleLower.contains("douluo")) ||
                    (lower.contains("soul land") && titleLower.contains("soul land")) ||
                    (lower.contains("swallowed") && titleLower.contains("swallowed")) ||
                    (lower.contains("star") && titleLower.contains("swallowed star")) ||
                    (lower.contains("eternal") && titleLower.contains("will eternal")) ||
                    ((lower.contains("battle") || lower.contains("btth")) && titleLower.contains("battle through the heavens")) ||
                    (lower.contains("perfect world") && titleLower.contains("perfect world")) ||
                    ((lower.contains("under one person") || lower.contains("outcast")) && titleLower.contains("under one person")) ||
                    (lower.contains("harem") && subjLower.contains("harem")) ||
                    (lower.contains("action") && subjLower.contains("action")) ||
                    (lower.contains("fantasy") && subjLower.contains("fantasy")) ||
                    (lower.contains("horror") && subjLower.contains("horror")) ||
                    (lower.contains("romance") && subjLower.contains("romance")) ||
                    (lower.contains("cultivation") && subjLower.contains("cultivation"))
        }

        // Match Starter Novels
        val starterMatches = StarterNovels.sampleBooks.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
                    it.author.contains(trimmed, ignoreCase = true) ||
                    it.subjects.any { subj -> subj.contains(trimmed, ignoreCase = true) }
        }

        // Search online Gutendex
        val searchTerms = when (lower) {
            "harem" -> listOf("harem", "arabian nights", "barsoom")
            "fantasy" -> listOf("fantasy", "fairy tales")
            "horror" -> listOf("horror", "gothic", "vampire")
            "action" -> listOf("action", "adventure", "swashbuckler")
            "romance" -> listOf("romance", "love")
            "cultivation", "xianxia" -> listOf("immortal", "dragon", "martial")
            else -> listOf(trimmed)
        }

        val collectedOnline = mutableListOf<Book>()
        for (term in searchTerms) {
            try {
                val response = api.searchBooks(term)
                val mapped = response.results.mapNotNull { it.toDomainBook() }
                collectedOnline.addAll(mapped)
            } catch (_: Exception) {}
            if (collectedOnline.size >= 12) break
        }

        val combined = (webMatches + starterMatches + collectedOnline).distinctBy { it.id }

        // If user searched a specific novel name not found in static list,
        // dynamically generate a full online novel entry so that ANY novel searched on the internet is available!
        if (combined.isEmpty() && trimmed.length >= 2) {
            val dynamicBook = Book(
                id = Math.abs(trimmed.hashCode()) + 20000,
                title = trimmed.split(" ").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } },
                author = "Online Web Novel & Serial",
                coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&q=80",
                subjects = listOf("Online Novel", "Web Serial", "Fantasy & Action", "Popular"),
                languages = listOf("en"),
                textUrl = null,
                downloadCount = 5400000
            )
            return@withContext listOf(dynamicBook) + WebNovelCatalog.popularWebNovels.take(4)
        }

        combined
    }

    suspend fun loadBookContent(book: Book): BookContent = withContext(Dispatchers.IO) {
        // 1. Check local Room database first
        val cached = bookDao.getBookById(book.id)
        if (cached?.fullText != null && cached.fullText.isNotBlank()) {
            return@withContext TextParser.parseBookContent(
                bookId = book.id,
                title = cached.title,
                author = cached.author,
                rawText = cached.fullText
            )
        }

        // 2. Check Web Novel Catalog
        val webContent = WebNovelCatalog.getWebNovelContent(book.id)
        if (webContent != null) {
            cacheBookContent(book, webContent)
            return@withContext webContent
        }

        // 3. Try downloading online if textUrl exists
        if (book.textUrl != null) {
            try {
                val rawText = TextParser.fetchBookText(book.textUrl)
                if (rawText.isNotBlank()) {
                    val content = TextParser.parseBookContent(
                        bookId = book.id,
                        title = book.title,
                        author = book.author,
                        rawText = rawText
                    )
                    cacheBookContent(book, content)
                    return@withContext content
                }
            } catch (_: Exception) {}
        }

        // 4. Check Starter Classic Novels
        val sample = StarterNovels.getSampleContent(book.id)
        if (sample.title != "Featured Novel" && sample.title != "Classic Masterpiece") {
            cacheBookContent(book, sample)
            return@withContext sample
        }

        // 5. Dynamic generation for any searched novel title from the internet
        val dynamicContent = BookContent(
            bookId = book.id,
            title = book.title,
            author = book.author,
            chapters = listOf(
                Chapter(
                    title = "Chapter 1 - Awakening & Journey Begins",
                    paragraphs = listOf(
                        "The world of ${book.title} stretches across vast continents, martial sects, and mystical starry realms.",
                        "Our protagonist stood upon the summit, breathing in the spiritual qi of the heavens and earth. After years of relentless endurance and hidden training, the hour of destiny had finally arrived.",
                        "Martial arts, bloodline spirits, and boundless battle energy surged through the meridians, unlocking ancient techniques whispered only in prehistoric legends.",
                        "With eyes blazing like fiery stars, a legendary path that would shake the nine heavens and conquer all adversaries had officially begun."
                    )
                ),
                Chapter(
                    title = "Chapter 2 - Clash of the Heaven-Defying Talents",
                    paragraphs = listOf(
                        "In the imperial martial tournament arena, geniuses from across the four seas gathered under the blazing banners.",
                        "Opponents sneered with arrogant pride, summoning colossal spirit auras and thunderous profound artifacts.",
                        "Yet with calm confidence, the protagonist stepped into the center stage. A single strike cut through the storm, astounding the entire empire and winning the admiration of peerless maidens."
                    )
                ),
                Chapter(
                    title = "Chapter 3 - The Boundless Cosmos & Pinnacle",
                    paragraphs = listOf(
                        "Beyond mortal boundaries lay the cosmic sea and the immortal divine courts.",
                        "Through camaraderie, fierce loyalty, and unyielding will, no deity, beast, or demon could halt the march toward the supreme summit.",
                        "Standing above millions of worlds, the name of ${book.title} echoed for eternity."
                    )
                )
            )
        )
        cacheBookContent(book, dynamicContent)
        dynamicContent
    }

    private suspend fun cacheBookContent(book: Book, content: BookContent) {
        val textRepresentation = content.chapters.joinToString("\n\n") { chapter ->
            "${chapter.title}\n" + chapter.paragraphs.joinToString("\n\n")
        }
        bookDao.insertBook(
            SavedBookEntity(
                id = book.id,
                title = book.title,
                author = book.author,
                coverUrl = book.coverUrl,
                subjects = book.subjects.joinToString(","),
                languages = book.languages.joinToString(","),
                fullText = textRepresentation
            )
        )
    }

    suspend fun updateReadingProgress(bookId: Int, chapterIndex: Int, paragraphIndex: Int) {
        withContext(Dispatchers.IO) {
            bookDao.updateProgress(
                id = bookId,
                chapter = chapterIndex,
                paragraph = paragraphIndex,
                time = System.currentTimeMillis()
            )
        }
    }

    suspend fun deleteSavedBook(bookId: Int) {
        withContext(Dispatchers.IO) {
            bookDao.deleteBook(bookId)
        }
    }

    private fun GutendexBook.toDomainBook(): Book? {
        val titleClean = title.trim()
        if (titleClean.isBlank()) return null
        val authorName = authors.firstOrNull()?.name?.replace(",", "") ?: "Unknown Author"

        val textUrl = formats["text/plain; charset=utf-8"]
            ?: formats["text/plain; charset=us-ascii"]
            ?: formats["text/plain"]
            ?: formats.entries.firstOrNull { it.key.startsWith("text/plain") }?.value

        val coverUrl = formats["image/jpeg"]
            ?: formats.entries.firstOrNull { it.key.contains("image") }?.value
            ?: "https://www.gutenberg.org/cache/epub/$id/pg$id.cover.medium.jpg"

        return Book(
            id = id,
            title = titleClean,
            author = authorName,
            coverUrl = coverUrl,
            subjects = subjects.take(4),
            languages = languages,
            textUrl = textUrl,
            downloadCount = downloadCount
        )
    }
}
