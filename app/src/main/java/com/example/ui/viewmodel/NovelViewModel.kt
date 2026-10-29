package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.TtsAudioController
import com.example.data.local.AppDatabase
import com.example.data.local.SavedBookEntity
import com.example.data.model.Book
import com.example.data.model.BookContent
import com.example.data.model.VoiceConfig
import com.example.data.model.VoiceGender
import com.example.data.model.VoiceInfo
import com.example.data.model.VoiceTonePreset
import com.example.data.remote.GutendexApi
import com.example.data.repository.NovelRepository
import com.example.data.repository.StarterNovels
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Discover : Screen()
    object Reader : Screen()
    object Library : Screen()
}

enum class ReaderThemePreset {
    DARK_OBSIDIAN,
    WARM_PARCHMENT,
    LIGHT_LINEN
}

class NovelViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val api = GutendexApi.create()
    private val repository = NovelRepository(api, database.bookDao())

    val audioController = TtsAudioController(application, viewModelScope)

    // Navigation & UI state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Discover)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _featuredBooks = MutableStateFlow<List<Book>>(StarterNovels.sampleBooks)
    val featuredBooks: StateFlow<List<Book>> = _featuredBooks.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults.asStateFlow()

    val savedBooks: StateFlow<List<SavedBookEntity>> = repository.savedBooks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current Reader State
    private val _currentBook = MutableStateFlow<Book?>(null)
    val currentBook: StateFlow<Book?> = _currentBook.asStateFlow()

    private val _currentContent = MutableStateFlow<BookContent?>(null)
    val currentContent: StateFlow<BookContent?> = _currentContent.asStateFlow()

    private val _currentChapterIndex = MutableStateFlow(0)
    val currentChapterIndex: StateFlow<Int> = _currentChapterIndex.asStateFlow()

    private val _isLoadingContent = MutableStateFlow(false)
    val isLoadingContent: StateFlow<Boolean> = _isLoadingContent.asStateFlow()

    private val _readerFontSize = MutableStateFlow(18f)
    val readerFontSize: StateFlow<Float> = _readerFontSize.asStateFlow()

    private val _readerTheme = MutableStateFlow(ReaderThemePreset.DARK_OBSIDIAN)
    val readerTheme: StateFlow<ReaderThemePreset> = _readerTheme.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadFeaturedBooks()
    }

    private fun loadFeaturedBooks() {
        viewModelScope.launch {
            _isSearching.value = true
            val books = repository.getFeaturedNovels()
            _featuredBooks.value = books
            _isSearching.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce user typing
            _isSearching.value = true
            val results = repository.searchNovels(query)
            _searchResults.value = results
            _isSearching.value = false
        }
    }

    fun openBook(book: Book) {
        _currentBook.value = book
        _currentChapterIndex.value = 0
        _currentScreen.value = Screen.Reader
        audioController.currentBookTitle = book.title
        audioController.currentChapterTitle = "Chapter 1"
        audioController.stop()

        viewModelScope.launch {
            _isLoadingContent.value = true
            val content = repository.loadBookContent(book)
            _currentContent.value = content
            _isLoadingContent.value = false
            content.chapters.firstOrNull()?.let {
                audioController.currentChapterTitle = it.title
            }

            // Auto-configure TTS language if book has language tag
            book.languages.firstOrNull()?.let { langCode ->
                val matchingLocale = audioController.availableLocales.value.firstOrNull { it.startsWith(langCode, ignoreCase = true) }
                if (matchingLocale != null) {
                    audioController.setLocale(matchingLocale)
                }
            }
        }
    }

    fun openSavedBook(saved: SavedBookEntity) {
        val book = Book(
            id = saved.id,
            title = saved.title,
            author = saved.author,
            coverUrl = saved.coverUrl,
            subjects = saved.subjects.split(",").filter { it.isNotBlank() },
            languages = saved.languages.split(",").filter { it.isNotBlank() },
            textUrl = null,
            downloadCount = 0,
            isSaved = true
        )
        _currentBook.value = book
        _currentChapterIndex.value = saved.lastChapterIndex
        _currentScreen.value = Screen.Reader
        audioController.stop()

        viewModelScope.launch {
            _isLoadingContent.value = true
            val content = repository.loadBookContent(book)
            _currentContent.value = content
            _isLoadingContent.value = false
        }
    }

    fun selectChapter(index: Int) {
        val content = _currentContent.value ?: return
        if (index in content.chapters.indices) {
            audioController.stop()
            _currentChapterIndex.value = index
            audioController.currentChapterTitle = content.chapters[index].title
            val book = _currentBook.value
            if (book != null) {
                viewModelScope.launch {
                    repository.updateReadingProgress(book.id, index, 0)
                }
            }
        }
    }

    fun playFromParagraph(paragraphIndex: Int, sentenceIndex: Int = 0) {
        val content = _currentContent.value ?: return
        val chapter = content.chapters.getOrNull(_currentChapterIndex.value) ?: return
        audioController.speakParagraph(paragraphIndex, chapter.paragraphs, sentenceIndex)

        val book = _currentBook.value
        if (book != null) {
            viewModelScope.launch {
                repository.updateReadingProgress(book.id, _currentChapterIndex.value, paragraphIndex)
            }
        }
    }

    fun togglePlayPause() {
        if (audioController.isPlaying.value) {
            audioController.pause()
        } else if (audioController.isPaused.value) {
            audioController.resume()
        } else {
            val content = _currentContent.value ?: return
            val chapter = content.chapters.getOrNull(_currentChapterIndex.value) ?: return
            audioController.startReading(chapter.paragraphs, audioController.activeParagraphIndex.value)
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setFontSize(size: Float) {
        _readerFontSize.value = size.coerceIn(13f, 28f)
    }

    fun setReaderTheme(theme: ReaderThemePreset) {
        _readerTheme.value = theme
    }

    fun removeSavedBook(bookId: Int) {
        viewModelScope.launch {
            repository.deleteSavedBook(bookId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioController.shutdown()
    }
}
