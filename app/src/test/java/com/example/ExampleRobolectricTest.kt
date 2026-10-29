package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.remote.TextParser
import com.example.data.repository.StarterNovels
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("VoxNovel", appName)
  }

  @Test
  fun `starter novels contain valid sample books and genres`() {
    val books = StarterNovels.sampleBooks
    assertTrue(books.isNotEmpty())
    val allSubjects = books.flatMap { it.subjects }.map { it.lowercase() }
    assertTrue(allSubjects.contains("fantasy"))
    assertTrue(allSubjects.contains("horror"))
    assertTrue(allSubjects.contains("action"))
    assertTrue(allSubjects.contains("romance"))
    assertTrue(allSubjects.contains("harem"))

    val content = StarterNovels.getSampleContent(62)
    assertEquals("A Princess of Mars", content.title)
    assertTrue(content.chapters.isNotEmpty())
    assertTrue(content.chapters.first().paragraphs.isNotEmpty())
  }

  @Test
  fun `text parser cleans headers and structures chapters`() {
    val sampleText = """
      *** START OF THE PROJECT GUTENBERG EBOOK TEST ***
      CHAPTER 1
      This is the first sentence. This is the second sentence.
      
      Another paragraph in chapter one.
      
      CHAPTER 2
      Here is chapter two with more content.
      *** END OF THE PROJECT GUTENBERG EBOOK TEST ***
    """.trimIndent()

    val parsed = TextParser.parseBookContent(1, "Test Book", "Author", sampleText)
    assertEquals(2, parsed.chapters.size)
    assertEquals("CHAPTER 1", parsed.chapters[0].title)
    assertEquals(2, parsed.chapters[0].paragraphs.size)
  }

  @Test
  fun `web novel catalog contains popular cultivation series`() {
    val webNovels = com.example.data.repository.WebNovelCatalog.popularWebNovels
    assertTrue(webNovels.any { it.title.contains("Douluo Dalu", ignoreCase = true) })
    assertTrue(webNovels.any { it.title.contains("Swallowed Star", ignoreCase = true) })
    assertTrue(webNovels.any { it.title.contains("A Will Eternal", ignoreCase = true) })
    assertTrue(webNovels.any { it.title.contains("Battle Through the Heavens", ignoreCase = true) })
    assertTrue(webNovels.any { it.title.contains("Perfect World", ignoreCase = true) })
    assertTrue(webNovels.any { it.title.contains("Under One Person", ignoreCase = true) })

    val douluoContent = com.example.data.repository.WebNovelCatalog.getWebNovelContent(10001)
    org.junit.Assert.assertNotNull(douluoContent)
    assertEquals("Douluo Dalu (Soul Land I)", douluoContent?.title)
    assertTrue(douluoContent!!.chapters.isNotEmpty())
  }

  @Test
  fun `audio playback service channel configured`() {
    assertEquals("voxnovel_playback_channel", com.example.audio.AudioPlaybackService.CHANNEL_ID)
  }
}
