package com.example.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import com.example.data.model.VoiceConfig
import com.example.data.model.VoiceGender
import com.example.data.model.VoiceInfo
import com.example.data.model.VoiceTonePreset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class TtsAudioController(
    private val context: Context,
    private val scope: CoroutineScope
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _activeParagraphIndex = MutableStateFlow(0)
    val activeParagraphIndex: StateFlow<Int> = _activeParagraphIndex.asStateFlow()

    private val _activeSentenceIndex = MutableStateFlow(0)
    val activeSentenceIndex: StateFlow<Int> = _activeSentenceIndex.asStateFlow()

    private val _voiceConfig = MutableStateFlow(VoiceConfig())
    val voiceConfig: StateFlow<VoiceConfig> = _voiceConfig.asStateFlow()

    private val _availableVoices = MutableStateFlow<List<VoiceInfo>>(emptyList())
    val availableVoices: StateFlow<List<VoiceInfo>> = _availableVoices.asStateFlow()

    private val _availableLocales = MutableStateFlow<List<String>>(emptyList())
    val availableLocales: StateFlow<List<String>> = _availableLocales.asStateFlow()

    private val _sleepTimerSec = MutableStateFlow<Int?>(null)
    val sleepTimerSec: StateFlow<Int?> = _sleepTimerSec.asStateFlow()

    // Current reading session state
    var currentBookTitle: String = "VoxNovel Audio"
    var currentChapterTitle: String = "Chapter 1"

    private var currentParagraphs: List<String> = emptyList()
    private var currentSentenceList: List<String> = emptyList()
    private var isManualStop = false
    private var sleepTimerJob: Job? = null

    init {
        AudioPlaybackService.controllerInstance = this
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val ttsEngine = tts ?: return
            ttsEngine.language = Locale.getDefault()
            refreshAvailableVoices(ttsEngine)
            setupUtteranceListener(ttsEngine)
            applyVoiceConfig(_voiceConfig.value)
            _isInitialized.value = true
        } else {
            _isInitialized.value = false
        }
    }

    private fun setupUtteranceListener(ttsEngine: TextToSpeech) {
        ttsEngine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isPlaying.value = true
                _isPaused.value = false
                val parts = utteranceId?.split("_")
                if (parts != null && parts.size >= 3 && parts[0] == "utt") {
                    val pIdx = parts[1].toIntOrNull() ?: 0
                    val sIdx = parts[2].toIntOrNull() ?: 0
                    _activeParagraphIndex.value = pIdx
                    _activeSentenceIndex.value = sIdx
                }
            }

            override fun onDone(utteranceId: String?) {
                val parts = utteranceId?.split("_")
                if (parts != null && parts.size >= 3 && parts[0] == "utt") {
                    val pIdx = parts[1].toIntOrNull() ?: 0
                    val sIdx = parts[2].toIntOrNull() ?: 0

                    scope.launch(Dispatchers.Main) {
                        advanceToNext(pIdx, sIdx)
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isPlaying.value = false
            }
        })
    }

    private fun advanceToNext(currentPIdx: Int, currentSIdx: Int) {
        if (isManualStop || _isPaused.value) return

        // Advance to next sentence within the same paragraph
        if (currentSIdx + 1 < currentSentenceList.size) {
            speakSentence(currentPIdx, currentSIdx + 1)
        } else {
            // Advance to next paragraph
            val nextPIdx = currentPIdx + 1
            if (nextPIdx < currentParagraphs.size) {
                speakParagraph(nextPIdx, currentParagraphs)
            } else {
                _isPlaying.value = false
                _activeSentenceIndex.value = 0
            }
        }
    }

    fun startReading(paragraphs: List<String>, startParagraph: Int = 0) {
        currentParagraphs = paragraphs
        speakParagraph(startParagraph, paragraphs)
    }

    fun speakParagraph(paragraphIndex: Int, paragraphs: List<String>, sentenceIndex: Int = 0) {
        if (paragraphIndex !in paragraphs.indices) return
        currentParagraphs = paragraphs
        isManualStop = false
        _activeParagraphIndex.value = paragraphIndex

        val paragraphText = paragraphs[paragraphIndex]
        currentSentenceList = splitIntoSentences(paragraphText)

        val targetSentence = sentenceIndex.coerceIn(0, (currentSentenceList.size - 1).coerceAtLeast(0))
        speakSentence(paragraphIndex, targetSentence)
    }

    fun speakSentence(paragraphIndex: Int, sentenceIndex: Int) {
        val ttsEngine = tts ?: return
        if (sentenceIndex !in currentSentenceList.indices) return

        _activeParagraphIndex.value = paragraphIndex
        _activeSentenceIndex.value = sentenceIndex

        val textToSpeak = currentSentenceList[sentenceIndex]
        val utteranceId = "utt_${paragraphIndex}_${sentenceIndex}"

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        applyVoiceConfig(_voiceConfig.value)
        ttsEngine.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        _isPlaying.value = true
        _isPaused.value = false

        try {
            AudioPlaybackService.start(
                context,
                currentBookTitle,
                "$currentChapterTitle • Sentence ${sentenceIndex + 1}",
                true
            )
        } catch (_: Exception) {}
    }

    fun pause() {
        tts?.stop()
        _isPlaying.value = false
        _isPaused.value = true
        try {
            AudioPlaybackService.update(
                context,
                currentBookTitle,
                "$currentChapterTitle (Paused)",
                false
            )
        } catch (_: Exception) {}
    }

    fun resume() {
        if (_isPaused.value && currentParagraphs.isNotEmpty()) {
            isManualStop = false
            speakParagraph(_activeParagraphIndex.value, currentParagraphs, _activeSentenceIndex.value)
        }
    }

    fun stop() {
        isManualStop = true
        tts?.stop()
        _isPlaying.value = false
        _isPaused.value = false
        try {
            AudioPlaybackService.stop(context)
        } catch (_: Exception) {}
    }

    fun nextSentence() {
        if (currentSentenceList.isNotEmpty() && _activeSentenceIndex.value + 1 < currentSentenceList.size) {
            speakSentence(_activeParagraphIndex.value, _activeSentenceIndex.value + 1)
        } else if (_activeParagraphIndex.value + 1 < currentParagraphs.size) {
            speakParagraph(_activeParagraphIndex.value + 1, currentParagraphs)
        }
    }

    fun previousSentence() {
        if (_activeSentenceIndex.value > 0) {
            speakSentence(_activeParagraphIndex.value, _activeSentenceIndex.value - 1)
        } else if (_activeParagraphIndex.value > 0) {
            val prevPIdx = _activeParagraphIndex.value - 1
            speakParagraph(prevPIdx, currentParagraphs)
        }
    }

    fun setPitch(pitch: Float) {
        _voiceConfig.value = _voiceConfig.value.copy(
            pitch = pitch,
            tonePreset = VoiceTonePreset.CUSTOM
        )
        applyVoiceConfig(_voiceConfig.value)
    }

    fun setTonePreset(preset: VoiceTonePreset) {
        _voiceConfig.value = _voiceConfig.value.copy(
            pitch = preset.pitch,
            tonePreset = preset
        )
        applyVoiceConfig(_voiceConfig.value)
    }

    fun setSpeechRate(rate: Float) {
        _voiceConfig.value = _voiceConfig.value.copy(speechRate = rate)
        applyVoiceConfig(_voiceConfig.value)
    }

    fun setLocale(localeTag: String) {
        _voiceConfig.value = _voiceConfig.value.copy(localeTag = localeTag, voiceName = "")
        try {
            val loc = Locale.forLanguageTag(localeTag)
            tts?.language = loc
            tts?.let { refreshAvailableVoices(it) }
        } catch (_: Exception) {}
        applyVoiceConfig(_voiceConfig.value)
    }

    fun setVoice(voiceName: String) {
        _voiceConfig.value = _voiceConfig.value.copy(voiceName = voiceName)
        applyVoiceConfig(_voiceConfig.value)
    }

    fun setVoiceGender(gender: VoiceGender) {
        _voiceConfig.value = _voiceConfig.value.copy(gender = gender)
        val filtered = _availableVoices.value.filter { v ->
            when (gender) {
                VoiceGender.MALE -> v.isMale
                VoiceGender.FEMALE -> v.isFemale
                VoiceGender.ANY -> true
            }
        }
        if (filtered.isNotEmpty()) {
            setVoice(filtered.first().name)
        }
    }

    private fun applyVoiceConfig(config: VoiceConfig) {
        val ttsEngine = tts ?: return
        try {
            ttsEngine.setPitch(config.pitch)
            ttsEngine.setSpeechRate(config.speechRate)

            if (config.voiceName.isNotBlank()) {
                val match = ttsEngine.voices?.firstOrNull { it.name == config.voiceName }
                if (match != null) {
                    ttsEngine.voice = match
                }
            }
        } catch (_: Exception) {}
    }

    private fun refreshAvailableVoices(ttsEngine: TextToSpeech) {
        try {
            val voices = ttsEngine.voices ?: emptySet()
            val list = voices.map { v ->
                val locale = v.locale
                val name = v.name
                val isFemale = name.contains("female", ignoreCase = true) ||
                        name.contains("woman", ignoreCase = true) ||
                        v.features?.contains("female") == true ||
                        name.endsWith("-f")
                val isMale = name.contains("male", ignoreCase = true) ||
                        name.contains("man", ignoreCase = true) ||
                        v.features?.contains("male") == true ||
                        name.endsWith("-m")

                val tag = locale.toLanguageTag()
                val display = "${locale.displayName} ${if (isFemale) "♀ (Female)" else if (isMale) "♂ (Male)" else ""}"
                VoiceInfo(
                    name = name,
                    localeTag = tag,
                    displayName = display.trim(),
                    isFemale = isFemale,
                    isMale = isMale,
                    latency = v.latency,
                    quality = v.quality
                )
            }
            _availableVoices.value = list
            _availableLocales.value = list.map { it.localeTag }.distinct().sorted()
        } catch (_: Exception) {}
    }

    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) {
            _sleepTimerSec.value = null
            return
        }

        var remaining = minutes * 60
        _sleepTimerSec.value = remaining

        sleepTimerJob = scope.launch(Dispatchers.Default) {
            while (remaining > 0) {
                delay(1000)
                remaining--
                _sleepTimerSec.value = remaining
            }
            // Timer expired: stop playback gracefully
            scope.launch(Dispatchers.Main) {
                stop()
                _sleepTimerSec.value = null
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimerSec.value = null
    }

    private fun splitIntoSentences(paragraph: String): List<String> {
        val list = paragraph.split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        return if (list.isEmpty()) listOf(paragraph) else list
    }

    fun shutdown() {
        sleepTimerJob?.cancel()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
