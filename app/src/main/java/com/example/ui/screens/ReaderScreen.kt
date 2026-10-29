package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.MiniAudioBar
import com.example.ui.components.SleepTimerDialog
import com.example.ui.components.VoiceStudioSheet
import com.example.ui.theme.CharcoalTextLight
import com.example.ui.theme.LinenBackgroundLight
import com.example.ui.theme.ObsidianBackgroundDark
import com.example.ui.theme.ParchmentSurfaceLight
import com.example.ui.theme.SentenceHighlightDark
import com.example.ui.theme.SentenceHighlightLight
import com.example.ui.theme.TextParchmentDark
import com.example.ui.viewmodel.NovelViewModel
import com.example.ui.viewmodel.ReaderThemePreset
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: NovelViewModel
) {
    // Back navigation support
    BackHandler {
        viewModel.navigateTo(Screen.Discover)
    }

    val currentBook by viewModel.currentBook.collectAsStateWithLifecycle()
    val currentContent by viewModel.currentContent.collectAsStateWithLifecycle()
    val currentChapterIndex by viewModel.currentChapterIndex.collectAsStateWithLifecycle()
    val isLoadingContent by viewModel.isLoadingContent.collectAsStateWithLifecycle()
    val fontSize by viewModel.readerFontSize.collectAsStateWithLifecycle()
    val readerTheme by viewModel.readerTheme.collectAsStateWithLifecycle()

    val isPlaying by viewModel.audioController.isPlaying.collectAsStateWithLifecycle()
    val isPaused by viewModel.audioController.isPaused.collectAsStateWithLifecycle()
    val activeParagraphIndex by viewModel.audioController.activeParagraphIndex.collectAsStateWithLifecycle()
    val activeSentenceIndex by viewModel.audioController.activeSentenceIndex.collectAsStateWithLifecycle()

    var showVoiceStudio by remember { mutableStateOf(false) }
    var showSleepTimer by remember { mutableStateOf(false) }
    var showChapterPicker by remember { mutableStateOf(false) }
    var showReaderSettings by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Auto-scroll when active paragraph advances during playback
    LaunchedEffect(activeParagraphIndex, isPlaying) {
        if (isPlaying && activeParagraphIndex >= 0) {
            val visibleIndices = listState.layoutInfo.visibleItemsInfo.map { it.index }
            if (activeParagraphIndex !in visibleIndices) {
                listState.animateScrollToItem((activeParagraphIndex - 1).coerceAtLeast(0))
            }
        }
    }

    // Color definitions based on ReaderThemePreset
    val (backgroundColor, textColor, activeHighlightColor, surfaceColor) = when (readerTheme) {
        ReaderThemePreset.DARK_OBSIDIAN -> Quad(
            ObsidianBackgroundDark,
            TextParchmentDark,
            SentenceHighlightDark,
            Color(0xFF161A24)
        )
        ReaderThemePreset.WARM_PARCHMENT -> Quad(
            Color(0xFFF4ECD8),
            Color(0xFF2C2416),
            Color(0x40D97706),
            Color(0xFFEBE0C5)
        )
        ReaderThemePreset.LIGHT_LINEN -> Quad(
            LinenBackgroundLight,
            CharcoalTextLight,
            SentenceHighlightLight,
            ParchmentSurfaceLight
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("reader_screen"),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentBook?.title ?: "Novel Reader",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val chapterTitle = currentContent?.chapters?.getOrNull(currentChapterIndex)?.title
                            ?: "Chapter ${currentChapterIndex + 1}"
                        Text(
                            text = chapterTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Discover) },
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Discover",
                            tint = textColor
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showChapterPicker = true },
                        modifier = Modifier.testTag("reader_chapter_menu")
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Select Chapter",
                            tint = textColor
                        )
                    }
                    IconButton(
                        onClick = { showReaderSettings = true },
                        modifier = Modifier.testTag("reader_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Reader Settings",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                MiniAudioBar(
                    audioController = viewModel.audioController,
                    bookTitle = currentBook?.title ?: "",
                    onOpenVoiceStudio = { showVoiceStudio = true },
                    onOpenSleepTimer = { showSleepTimer = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoadingContent) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading novel & synchronizing audio...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor
                        )
                    }
                }
            } else {
                val currentChapter = currentContent?.chapters?.getOrNull(currentChapterIndex)
                val paragraphs = currentChapter?.paragraphs ?: emptyList()

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = currentChapter?.title ?: "Chapter 1",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    itemsIndexed(paragraphs) { pIndex, paragraphText ->
                        val isCurrentParagraph = isPlaying && (pIndex == activeParagraphIndex)

                        val annotated = buildAnnotatedString {
                            val sentences = paragraphText.split(Regex("(?<=[.!?])\\s+"))
                            sentences.forEachIndexed { sIndex, sentence ->
                                val isCurrentSentence = isCurrentParagraph && (sIndex == activeSentenceIndex)
                                if (isCurrentSentence) {
                                    withStyle(
                                        style = SpanStyle(
                                            background = activeHighlightColor,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        append(sentence)
                                    }
                                } else {
                                    append(sentence)
                                }
                                if (sIndex < sentences.size - 1) {
                                    append(" ")
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrentParagraph) surfaceColor else Color.Transparent,
                            border = if (isCurrentParagraph) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    // Tap to read aloud from this paragraph
                                    viewModel.playFromParagraph(pIndex, 0)
                                }
                                .padding(if (isCurrentParagraph) 8.dp else 2.dp)
                                .testTag("reader_paragraph_$pIndex")
                        ) {
                            Text(
                                text = annotated,
                                fontSize = fontSize.sp,
                                lineHeight = (fontSize * 1.6f).sp,
                                color = textColor,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }
            }
        }
    }

    // Chapter Picker Bottom Sheet
    if (showChapterPicker && currentContent != null) {
        ModalBottomSheet(
            onDismissRequest = { showChapterPicker = false },
            containerColor = surfaceColor,
            modifier = Modifier.testTag("chapter_picker_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Select Chapter",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    itemsIndexed(currentContent!!.chapters) { idx, ch ->
                        val isSelected = idx == currentChapterIndex
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectChapter(idx)
                                    showChapterPicker = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ch.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else textColor
                                )
                                Text(
                                    text = "${ch.paragraphs.size} ¶",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = textColor.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Reader Settings Modal Bottom Sheet
    if (showReaderSettings) {
        ModalBottomSheet(
            onDismissRequest = { showReaderSettings = false },
            containerColor = surfaceColor,
            modifier = Modifier.testTag("reader_settings_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Reading Appearance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Font Size Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Font Size", style = MaterialTheme.typography.bodyMedium, color = textColor)
                    Text("${fontSize.toInt()} sp", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Slider(
                    value = fontSize,
                    onValueChange = { viewModel.setFontSize(it) },
                    valueRange = 14f..26f,
                    steps = 11,
                    modifier = Modifier.testTag("font_size_slider")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Theme Presets
                Text("Page Theme", style = MaterialTheme.typography.bodyMedium, color = textColor)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReaderThemePreset.values().forEach { preset ->
                        val isSelected = readerTheme == preset
                        val label = when (preset) {
                            ReaderThemePreset.DARK_OBSIDIAN -> "Obsidian"
                            ReaderThemePreset.WARM_PARCHMENT -> "Parchment"
                            ReaderThemePreset.LIGHT_LINEN -> "Linen"
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setReaderTheme(preset) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("theme_preset_${preset.name.lowercase()}")
                        )
                    }
                }
            }
        }
    }

    // Voice Studio Sheet
    if (showVoiceStudio) {
        VoiceStudioSheet(
            audioController = viewModel.audioController,
            onDismiss = { showVoiceStudio = false }
        )
    }

    // Sleep Timer Dialog
    if (showSleepTimer) {
        SleepTimerDialog(
            audioController = viewModel.audioController,
            onDismiss = { showSleepTimer = false }
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
