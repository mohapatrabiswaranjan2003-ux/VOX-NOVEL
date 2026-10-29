package com.example.data.repository

import com.example.data.model.Book
import com.example.data.model.BookContent
import com.example.data.model.Chapter

object StarterNovels {
    val sampleBooks = listOf(
        Book(
            id = 62,
            title = "A Princess of Mars",
            author = "Edgar Rice Burroughs",
            coverUrl = "https://www.gutenberg.org/cache/epub/62/pg62.cover.medium.jpg",
            subjects = listOf("Fantasy", "Action", "Romance", "Harem", "Sword & Planet"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/62.txt.utf-8",
            downloadCount = 42100
        ),
        Book(
            id = 345,
            title = "Dracula",
            author = "Bram Stoker",
            coverUrl = "https://www.gutenberg.org/cache/epub/345/pg345.cover.medium.jpg",
            subjects = listOf("Horror", "Gothic", "Vampires", "Supernatural"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/345.txt.utf-8",
            downloadCount = 98400
        ),
        Book(
            id = 1184,
            title = "The Count of Monte Cristo",
            author = "Alexandre Dumas",
            coverUrl = "https://www.gutenberg.org/cache/epub/1184/pg1184.cover.medium.jpg",
            subjects = listOf("Action", "Adventure", "Revenge", "Drama"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/1184.txt.utf-8",
            downloadCount = 67300
        ),
        Book(
            id = 19860,
            title = "The Arabian Nights (1001 Nights)",
            author = "Richard F. Burton",
            coverUrl = "https://www.gutenberg.org/cache/epub/19860/pg19860.cover.medium.jpg",
            subjects = listOf("Harem", "Fantasy", "Romance", "Adventure", "Folklore"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/19860.txt.utf-8",
            downloadCount = 51200
        ),
        Book(
            id = 1342,
            title = "Pride and Prejudice",
            author = "Jane Austen",
            coverUrl = "https://www.gutenberg.org/cache/epub/1342/pg1342.cover.medium.jpg",
            subjects = listOf("Romance", "Classics", "Drama"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/1342.txt.utf-8",
            downloadCount = 76100
        ),
        Book(
            id = 84,
            title = "Frankenstein",
            author = "Mary Shelley",
            coverUrl = "https://www.gutenberg.org/cache/epub/84/pg84.cover.medium.jpg",
            subjects = listOf("Horror", "Gothic", "Science Fiction"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/84.txt.utf-8",
            downloadCount = 58900
        ),
        Book(
            id = 35,
            title = "The Time Machine",
            author = "H. G. Wells",
            coverUrl = "https://www.gutenberg.org/cache/epub/35/pg35.cover.medium.jpg",
            subjects = listOf("Science Fiction", "Action", "Adventure"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/35.txt.utf-8",
            downloadCount = 28400
        ),
        Book(
            id = 11,
            title = "Alice's Adventures in Wonderland",
            author = "Lewis Carroll",
            coverUrl = "https://www.gutenberg.org/cache/epub/11/pg11.cover.medium.jpg",
            subjects = listOf("Fantasy", "Adventure", "Classics"),
            languages = listOf("en"),
            textUrl = "https://www.gutenberg.org/ebooks/11.txt.utf-8",
            downloadCount = 34500
        )
    )

    fun getSampleContent(bookId: Int): BookContent {
        return when (bookId) {
            62 -> BookContent(
                bookId = 62,
                title = "A Princess of Mars",
                author = "Edgar Rice Burroughs",
                chapters = listOf(
                    Chapter(
                        title = "Chapter I - On the Arizona Hills",
                        paragraphs = listOf(
                            "I am a very old man; how old I do not know. It is possible I am a hundred, perhaps more, but I cannot tell because I have never aged as other men, nor do I remember any childhood.",
                            "So far as I could recount, I had always been a man, a full thirty years of age, and as vigorous in body and agile in martial action as any soldier alive.",
                            "My story begins in the winter of 1865, after the close of the Civil War, when Captain James K. Powell and I rode out to the goldfields of Arizona.",
                            "Suddenly, as evening deepened, strange sounds echoed across the valley, and a mysterious astral pull seemed to lift me straight toward the brilliant red planet glowing above."
                        )
                    ),
                    Chapter(
                        title = "Chapter II - The Warrior Princess",
                        paragraphs = listOf(
                            "I opened my eyes upon a strange and weird landscape. I knew that I was on Mars; not one ounce of doubt entered my mind.",
                            "Surrounded by fierce green warriors ten feet high, armed with gigantic spears and radiant metals, I prepared for the duel of a lifetime.",
                            "There, guarded amidst the royal retinue, stood Dejah Thoris, Princess of Helium, incomparable in beauty and poise, whose gaze captivated all who beheld her court."
                        )
                    )
                )
            )
            345 -> BookContent(
                bookId = 345,
                title = "Dracula",
                author = "Bram Stoker",
                chapters = listOf(
                    Chapter(
                        title = "Chapter I - Jonathan Harker's Journal",
                        paragraphs = listOf(
                            "3 May. Bistritz.—Left Munich at 8:35 P.M., on 1st May, arriving at Vienna early next morning. Should have arrived at 6:46, but train was an hour late.",
                            "The impression I had was that we were leaving the West and entering the East; the most western of splendid bridges over the Danube took us among the traditions of Turkish rule.",
                            "The mountains rose dark and jagged against the blood-red twilight. As midnight struck, a pack of wolves began to howl, and a sinister carriage with four black horses emerged from the shadows of Castle Dracula."
                        )
                    )
                )
            )
            1184 -> BookContent(
                bookId = 1184,
                title = "The Count of Monte Cristo",
                author = "Alexandre Dumas",
                chapters = listOf(
                    Chapter(
                        title = "Chapter I - Marseilles: The Arrival",
                        paragraphs = listOf(
                            "On the 24th of February, 1815, the lookout at Notre-Dame de la Garde signalled the three-master, the Pharaon, from Smyrna, Trieste, and Naples.",
                            "As usual, a pilot put off immediately, and rounding the Château d'If, got on board the vessel between Cape Morgiou and Rion island.",
                            "Edmond Dantès, a young man eighteen or twenty years of age, stood by the pilot, his eyes flashing with courage and determination as adventures and grand vengeance awaited him."
                        )
                    )
                )
            )
            19860 -> BookContent(
                bookId = 19860,
                title = "The Arabian Nights (1001 Nights)",
                author = "Richard F. Burton",
                chapters = listOf(
                    Chapter(
                        title = "Prologue - The Tale of King Shahryar and His Brother",
                        paragraphs = listOf(
                            "In the chronicles of ancient kings of Persia and Ind, there ruled a mighty monarch named King Shahryar, whose vast palace held a magnificent court and noble retinue.",
                            "Betrayed by treachery, the King vowed a solemn and severe decree, marrying a maiden each eve and commanding her demise at sunrise.",
                            "Until the vizier's daughter, Scheherazade, famed for her wisdom, beauty, and thousand tales of fantasy and romance, volunteered to enter the royal chambers and weave enchantment night after night."
                        )
                    )
                )
            )
            1342 -> BookContent(
                bookId = 1342,
                title = "Pride and Prejudice",
                author = "Jane Austen",
                chapters = listOf(
                    Chapter(
                        title = "Chapter I - An Offer of Fortune",
                        paragraphs = listOf(
                            "It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.",
                            "However little known the feelings or views of such a man may be on his first entering a neighbourhood, this truth is so well fixed in the minds of the surrounding families, that he is considered the rightful property of someone or other of their daughters.",
                            "My dear Mr. Bennet, said his lady to him one day, have you heard that Netherfield Park is let at last?"
                        )
                    )
                )
            )
            35 -> BookContent(
                bookId = 35,
                title = "The Time Machine",
                author = "H. G. Wells",
                chapters = listOf(
                    Chapter(
                        title = "Chapter I - The Time Traveller",
                        paragraphs = listOf(
                            "The Time Traveller was expounding a recondite matter to us. His grey eyes shone and twinkled, and his usually pale face was flushed and animated.",
                            "The fire burnt brightly, and the soft radiance of the incandescent lamps in the lilies of silver caught the bubbles that flashed and passed in our glasses.",
                            "You must follow me carefully. I shall have to controvert one or two ideas that are almost universally accepted. The geometry, for instance, they taught you at school is founded on a misconception."
                        )
                    )
                )
            )
            else -> BookContent(
                bookId = bookId,
                title = "Featured Novel",
                author = "Classic Online Author",
                chapters = listOf(
                    Chapter(
                        title = "Opening Chapter",
                        paragraphs = listOf(
                            "Welcome to VoxNovel. This online novel is prepared for eye reading and high-fidelity text-to-speech narration.",
                            "Browse genres including Fantasy, Horror, Action, Romance, and Harem/Web Fiction to find full stories.",
                            "Tap any paragraph to start listening with customizable voice pitch, tone, and tempo."
                        )
                    )
                )
            )
        }
    }
}
