package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.theme.VoxNovelTheme
import com.example.ui.viewmodel.NovelViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoxNovelTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: NovelViewModel = viewModel()
                    VoxNovelApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun VoxNovelApp(viewModel: NovelViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            is Screen.Discover -> {
                DiscoverScreen(viewModel = viewModel)
            }
            is Screen.Reader -> {
                ReaderScreen(viewModel = viewModel)
            }
            is Screen.Library -> {
                DiscoverScreen(viewModel = viewModel)
            }
        }
    }
}
