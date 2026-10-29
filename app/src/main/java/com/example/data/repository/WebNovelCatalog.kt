package com.example.data.repository

import com.example.data.model.Book
import com.example.data.model.BookContent
import com.example.data.model.Chapter

object WebNovelCatalog {

    val popularWebNovels: List<Book> = listOf(
        Book(
            id = 10001,
            title = "Douluo Dalu (Soul Land I)",
            author = "Tang Jia San Shao",
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=400&q=80",
            subjects = listOf("Cultivation", "Action", "Fantasy", "Romance", "Harem", "Martial Spirits"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 9850000
        ),
        Book(
            id = 10002,
            title = "Douluo Dalu II: The Unrivaled Tang Sect",
            author = "Tang Jia San Shao",
            coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&q=80",
            subjects = listOf("Action", "Fantasy", "Cultivation", "Romance", "Soul Land"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 8420000
        ),
        Book(
            id = 10003,
            title = "Douluo Dalu III: Legend of the Dragon King",
            author = "Tang Jia San Shao",
            coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&q=80",
            subjects = listOf("Action", "Fantasy", "Dragon King", "Mecha", "Romance"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 7650000
        ),
        Book(
            id = 10004,
            title = "Douluo Dalu IV: Ultimate Douluo",
            author = "Tang Jia San Shao",
            coverUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&q=80",
            subjects = listOf("Sci-Fi", "Cultivation", "Action", "Space", "Soul Land"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 6980000
        ),
        Book(
            id = 10005,
            title = "Swallowed Star (吞噬星空)",
            author = "I Eat Tomatoes (我吃西红柿)",
            coverUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400&q=80",
            subjects = listOf("Action", "Sci-Fi", "Post-Apocalyptic", "Cultivation", "Cosmic"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 9430000
        ),
        Book(
            id = 10006,
            title = "A Will Eternal (一念永恒)",
            author = "Er Gen (耳根)",
            coverUrl = "https://images.unsplash.com/photo-1514539079130-25950c84af65?w=400&q=80",
            subjects = listOf("Cultivation", "Comedy", "Action", "Harem", "Xianxia", "Immortality"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 9120000
        ),
        Book(
            id = 10007,
            title = "Battle Through the Heavens (斗破苍穹)",
            author = "Tian Can Tu Dou (天蚕土豆)",
            coverUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=400&q=80",
            subjects = listOf("Action", "Cultivation", "Flame Alchemist", "Harem", "Xuanhuan"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 11200000
        ),
        Book(
            id = 10008,
            title = "Perfect World (完美世界)",
            author = "Chen Dong (辰东)",
            coverUrl = "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?w=400&q=80",
            subjects = listOf("Xuanhuan", "Action", "Primordial Beasts", "Cultivation", "Epic Fantasy"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 8890000
        ),
        Book(
            id = 10009,
            title = "Under One Person: The Outcast (一人之下)",
            author = "Mi Er (米二)",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=400&q=80",
            subjects = listOf("Action", "Urban Fantasy", "Daoist Arts", "Martial Arts", "Supernatural"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 7890000
        ),
        Book(
            id = 10010,
            title = "Lord of the Mysteries (诡秘之主)",
            author = "Cuttlefish That Loves Diving",
            coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&q=80",
            subjects = listOf("Fantasy", "Steampunk", "Mystery", "Horror", "Tarot Club"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 10500000
        ),
        Book(
            id = 10011,
            title = "Renegade Immortal (仙逆)",
            author = "Er Gen (耳根)",
            coverUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=400&q=80",
            subjects = listOf("Cultivation", "Dark Fantasy", "Action", "Xianxia", "Revenge"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 8150000
        ),
        Book(
            id = 10012,
            title = "Against the Gods (逆天邪神)",
            author = "Mars Gravity (火星引力)",
            coverUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&q=80",
            subjects = listOf("Action", "Harem", "Cultivation", "Romance", "Evil God"),
            languages = listOf("en", "zh"),
            textUrl = null,
            downloadCount = 8960000
        ),
        Book(
            id = 10013,
            title = "Solo Leveling (Only I Level Up)",
            author = "Chugong",
            coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&q=80",
            subjects = listOf("Action", "Dungeon Hunter", "Shadow Monarch", "Fantasy", "System"),
            languages = listOf("en", "ko"),
            textUrl = null,
            downloadCount = 12400000
        )
    )

    fun getWebNovelContent(bookId: Int): BookContent? {
        return when (bookId) {
            10001 -> BookContent(
                bookId = 10001,
                title = "Douluo Dalu (Soul Land I)",
                author = "Tang Jia San Shao",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Rebirth into the Douluo Continent",
                        paragraphs = listOf(
                            "Bashu, the Outer Sect of the Tang Sect. The cliff was named Ghost Sorrow, standing thousands of feet high.",
                            "Tang San stood at the precipice of Ghost Sorrow, looking down into the swirling mist below. He had violated sect rules by reading the forbidden Inner Sect secrets, yet he had created the pinnacle hidden weapon, the Buddha's Fury Tang Lotus.",
                            "With a clear heart and nothing to regret, Tang San leapt into the endless abyss, casting away all mortal burdens.",
                            "When he opened his eyes again, he was no longer in Sichuan. He had awakened in the body of a young child in Holy Spirit Village on the wondrous Douluo Continent.",
                            "In this world, there is no magic, no battle qi, and no martial arts. However, there are mystical Spirits. Every person possesses an innate spirit awakened at age six, used to cultivate Soul Power and become a revered Spirit Master.",
                            "Looking at his small hands, Tang San felt the Purple Demon Eyes and Mysterious Heaven Skill circulating within his meridians. His legend as the Thousand Hand Douluo had just begun."
                        )
                    ),
                    Chapter(
                        title = "Chapter 2 - Twin Martial Spirits and Innate Full Spirit Power",
                        paragraphs = listOf(
                            "In the Spirit Awakening Hall of Holy Spirit Village, the handsome deacon Su Yuntao channeled his lone wolf spirit to awaken the children's spirits.",
                            "When Tang San placed his hand upon the crystal orb, his right hand revealed a tiny stalk of Blue Silver Grass, universally considered a useless spirit.",
                            "Yet when he touched the awakening orb, brilliant radiant light filled the entire hall, dazzling everyone present.",
                            "Innate Full Spirit Power! Su Yuntao gasped in absolute disbelief.",
                            "What no one saw was that hidden tightly inside Tang San's left palm behind his back was a small, pitch-black hammer coiled in lightning: the legendary Clear Sky Hammer."
                        )
                    ),
                    Chapter(
                        title = "Chapter 3 - Meeting Xiao Wu at Nuoding Academy",
                        paragraphs = listOf(
                            "Arriving at Nuoding Elementary Spirit Master Academy as a work-study student, Tang San carried his coarse quilt into Dormitory Seven.",
                            "Standing before him was a lively young girl with long scorpion-braided hair and bright, spirited eyes.",
                            "My name is Xiao Wu, dancing like a dancer! she exclaimed with a confident grin. Who is the dorm boss here?",
                            "Tang San took a gentle step forward, and with their hands crossing in sparring, a bond that would cross realms, gods, and eternities was formed."
                        )
                    )
                )
            )
            10002 -> BookContent(
                bookId = 10002,
                title = "Douluo Dalu II: The Unrivaled Tang Sect",
                author = "Tang Jia San Shao",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Ten Thousand Years Later: The Decline of the Tang Sect",
                        paragraphs = listOf(
                            "Ten thousand years have passed since the legendary Thousand Hand Douluo Tang San ascended to the God Realm.",
                            "On the Douluo Continent, the glorious Tang Sect has gradually declined, while new Soul Tools and the Sun Moon Continent have merged into the land.",
                            "Huo Yuhao, a young youth driven out of the Duke's Mansion with only a pair of Spirit Eyes, set out toward the Great Star Dou Forest to hunt his first soul ring.",
                            "Out of nowhere, an immense golden ice silkworm of one million years broke through the snow, offering to become his soul ring and rewriting the destiny of the entire continent."
                        )
                    )
                )
            )
            10003 -> BookContent(
                bookId = 10003,
                title = "Douluo Dalu III: Legend of the Dragon King",
                author = "Tang Jia San Shao",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Awakening of the Golden Dragon King Bloodline",
                        paragraphs = listOf(
                            "With the advancement of soul technology, humans on the Douluo Continent conquered the oceans and discovered two new continents.",
                            "Spirit Beasts were hunted to near extinction, and the Soul Beast King awakened in the depths of the forest with an oath of vengeance.",
                            "Young Tang Wulin awakened the seemingly common Blue Silver Grass, but deep inside his blood vessels slept the terrifying strength of the Golden Dragon King."
                        )
                    )
                )
            )
            10004 -> BookContent(
                bookId = 10004,
                title = "Douluo Dalu IV: Ultimate Douluo",
                author = "Tang Jia San Shao",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - The Egg from the Frozen Polar Ice",
                        paragraphs = listOf(
                            "Ten thousand years after the Dragon King era, Douluo technology conquered interstellar space travel, establishing colonies across distant star systems.",
                            "During a scientific expedition to the extreme northern polar ice cap, researchers discovered an egg pulsing with shimmering golden and silver patterns.",
                            "From this egg hatched a young boy named Lan Xuanyu, heir to the dual Golden and Silver Dragon King bloodlines, destined to rebuild the God Realm."
                        )
                    )
                )
            )
            10005 -> BookContent(
                bookId = 10005,
                title = "Swallowed Star (吞噬星空)",
                author = "I Eat Tomatoes",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - The Grand Nirvana and High School Martial Student",
                        paragraphs = listOf(
                            "Year 2056, Jiang-Nan city, Southern Region. The summer heat scorched the military district pavement.",
                            "Decades ago, the R-virus mutated into the RR-virus, causing a global pandemic known as the Grand Nirvana. Countless beasts mutated into ferocious monsters, forcing humanity to build fortified city bases guarded by elite Fighters.",
                            "Luo Feng stood atop the school roof, gazing out at the looming city walls. As an eighteen-year-old student from an ordinary apartment, he possessed exceptional determination and reflexes.",
                            "During a martial examination, when stress pushed his physical limits, a mysterious golden ball hidden inside his sea of consciousness suddenly vibrated, awakening his dormant Spirit Reader talent."
                        )
                    ),
                    Chapter(
                        title = "Chapter 2 - Awakening as a Spirit Reader",
                        paragraphs = listOf(
                            "The six flying throwing knives whirled in the air with silent, deadly precision under Luo Feng's telekinetic control.",
                            "A Spirit Reader! In the Fighter circles, Spirit Readers are revered as terrifying reapers, capable of slaughtering beast lords with pure mind force.",
                            "From the wilderness of Earth to the Nine Star Purgatories of the universe, Luo Feng's path from a humble boy to the master of the boundless cosmos had begun."
                        )
                    )
                )
            )
            10006 -> BookContent(
                bookId = 10006,
                title = "A Will Eternal (一念永恒)",
                author = "Er Gen",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Bai Xiaochun and the Fragrant Incense",
                        paragraphs = listOf(
                            "Outside Mao Village, thirteen-year-old Bai Xiaochun stood nervously beneath Mount Hood, holding an ancient fragrant incense stick.",
                            "All his life, Bai Xiaochun feared death more than anything. He did not care about conquering the world; his only supreme goal was to live forever and achieve true immortality.",
                            "Lighting the incense as thunder rumbled across the clouds, an immortal from the renowned Spirit Stream Sect descended upon a sword of light.",
                            "With his clever wit, shameless antics, and stubborn pursuit of longevity, Bai Xiaochun entered the immortal sect, turning the entire cultivation world completely upside down."
                        )
                    )
                )
            )
            10007 -> BookContent(
                bookId = 10007,
                title = "Battle Through the Heavens (斗破苍穹)",
                author = "Tian Can Tu Dou",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - The Fallen Genius of the Xiao Clan",
                        paragraphs = listOf(
                            "Dou Qi, rank three! The test stone monument glowed coldly, displaying five heart-wrenching words before the gathered crowd.",
                            "Xiao Yan, who was once hailed as the greatest genius of Wutan City by cultivating to ninth-level Dou Disciple at age eleven, had suffered three agonizing years of power regression.",
                            "Mocking jeers and contemptuous gazes surrounded him, but Xiao Yan held his head high, his fingers digging into his palms until blood stained his sleeves.",
                            "Thirty years east of the river, thirty years west of the river! Do not bully a young man just because he is poor today!",
                            "Little did anyone know that inside the ancient black ring on his finger, an immortal soul named Yao Lao was about to awaken, teaching Xiao Yan the supreme Heavenly Flame Cultivation Mantra."
                        )
                    ),
                    Chapter(
                        title = "Chapter 2 - Yao Lao and the Heavenly Flames",
                        paragraphs = listOf(
                            "From inside the ring, a translucent old man in white robes hovered with an ethereal smile. Little brat, are you ready to become an Alchemist?",
                            "The path of the Flame God was opened: consuming twenty-three mystical Heavenly Flames across the continent to burn through the heavens and conquer the Dou Di pinnacle."
                        )
                    )
                )
            )
            10008 -> BookContent(
                bookId = 10008,
                title = "Perfect World (完美世界)",
                author = "Chen Dong",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Stone Village and the Demon Willow",
                        paragraphs = listOf(
                            "Deep within the boundless wasteland where prehistoric beasts roamed, a bolt of divine lightning had scorched an ancient willow tree, leaving only a single green branch that glowed with gentle life.",
                            "In Stone Village, a small toddler named Little Shi Hao staggered about, drinking beast milk with round, innocent eyes.",
                            "Though stripped of his innate Supreme Being Bone by treacherous kin in his infancy, this gentle toddler carried boundless blood vitality and an untamable spirit.",
                            "Through trials, divine medicine, and primordial treasure techniques, he would rise to become the peerless Emperor Huang, pacifying the dark turmoil across the river of time."
                        )
                    )
                )
            )
            10009 -> BookContent(
                bookId = 10009,
                title = "Under One Person: The Outcast (一人之下)",
                author = "Mi Er",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - The Disturbed Grave in the Rain",
                        paragraphs = listOf(
                            "Zhang Chulan had spent twelve years pretending to be the most unremarkable college student in the country, obeying his grandfather's strict dying wish to hide his Golden Light Art.",
                            "A phone call from his rural hometown shattered the illusion: his grandfather Zhang Xilin's grave had been ransacked, and his body was missing.",
                            "Arriving at the graveyard under torrential rain, Zhang Chulan met Feng Baobao, an unkempt girl wielding kitchen knives with inhuman calm and immortal vitality.",
                            "The curtain was pulled back on the secret world of Inhumans, the ancient Eight Mysterious Arts, and the truth behind the historic Jia Shen Purge."
                        )
                    )
                )
            )
            10010 -> BookContent(
                bookId = 10010,
                title = "Lord of the Mysteries (诡秘之主)",
                author = "Cuttlefish That Loves Diving",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Crimson Moon and the Tarot Club",
                        paragraphs = listOf(
                            "Pain. Blinding pain tore through Zhou Mingrui's skull as the smell of gunpowder and blood filled his senses.",
                            "Looking in the cracked Victorian mirror, he found himself reincarnated as Klein Moretti in the industrial city of Tingen, under the gaze of a crimson moon.",
                            "Steampunk gears, eldritch deities, potion sequences, and the enigmatic Fog above the spirit realm awaited him as he assumed the mantle of The Fool."
                        )
                    )
                )
            )
            10011 -> BookContent(
                bookId = 10011,
                title = "Renegade Immortal (仙逆)",
                author = "Er Gen",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - Wang Lin and the Sky-Defying Bead",
                        paragraphs = listOf(
                            "Wang Lin was an ordinary village youth whose mediocre talent was rejected by the immortal cultivation sect.",
                            "Unwilling to surrender to fate, he stumbled across a mysterious stone bead patterned with five clouds, containing an independent time acceleration dimension.",
                            "If the heavens demand my submission, I shall slay the heavens! Wang Lin's cold, merciless journey of slaughter, dao comprehension, and defying fate had commenced."
                        )
                    )
                )
            )
            10012 -> BookContent(
                bookId = 10012,
                title = "Against the Gods (逆天邪神)",
                author = "Mars Gravity",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - The Sky Poison Pearl and the Reborn Youth",
                        paragraphs = listOf(
                            "Hunted to the Cloud's End Cliff for possessing the Sky Poison Pearl, Yun Che swallowed the pearl and plummeted into the void.",
                            "Reincarnating into Floating Cloud City inside the frail body of Xiao Che with crippled profound entrances, he discovered the supreme God of Evil profound veins.",
                            "With peerless medical skills, decisive martial might, and an unmatched charisma, he embarked on a path that would shake the realm of gods."
                        )
                    )
                )
            )
            10013 -> BookContent(
                bookId = 10013,
                title = "Solo Leveling",
                author = "Chugong",
                chapters = listOf(
                    Chapter(
                        title = "Chapter 1 - The Weakest E-Rank Hunter",
                        paragraphs = listOf(
                            "Known as humanity's weakest hunter, Sung Jin-woo risked his life in low-rank dungeons just to pay for his mother's medical bills.",
                            "Trapped inside a terrifying Double Dungeon before titanic stone statues, he faced certain death while his comrades fled.",
                            "Accepting the secret Courage of the Weak quest, a glowing virtual system screen appeared before his eyes: [You have qualified as a Player. Will you accept?]"
                        )
                    )
                )
            )
            else -> null
        }
    }
}
