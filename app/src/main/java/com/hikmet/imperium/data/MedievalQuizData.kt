package com.hikmet.imperium.data

import com.hikmet.imperium.data.Question

/**
 * Database of Medieval period quiz questions
 */
object MedievalQuizData {

    // Level 1: Early Middle Ages
    private val earlyMiddleAgesQuestions = listOf(
        Question(
            text = "What event marks the beginning of the Middle Ages?",
            options = listOf(
                "The founding of Constantinople",
                "The fall of the Western Roman Empire",
                "The coronation of Charlemagne",
                "The Viking invasions"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the dominant economic system of the Early Middle Ages?",
            options = listOf(
                "Mercantilism",
                "Capitalism",
                "Feudalism",
                "Communism"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Who was crowned Emperor of the Romans by Pope Leo III in 800 CE?",
            options = listOf(
                "Charlemagne",
                "Clovis I",
                "Constantine",
                "Theodoric"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which people were known for their ship burials and raids on coastal areas during the Early Middle Ages?",
            options = listOf(
                "Franks",
                "Saxons",
                "Normans",
                "Vikings"
            ),
            correctAnswerIndex = 3
        )
    )

    // Level 2: The Crusades
    private val crusadesQuestions = listOf(
        Question(
            text = "What was the primary goal of the First Crusade?",
            options = listOf(
                "To conquer Egypt",
                "To recapture Jerusalem from Muslim control",
                "To defeat the Byzantine Empire",
                "To establish trade routes"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Pope called for the First Crusade in 1095?",
            options = listOf(
                "Pope Urban II",
                "Pope Gregory VII",
                "Pope Innocent III",
                "Pope Clement V"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which Muslim leader reconquered Jerusalem from the Crusaders in 1187?",
            options = listOf(
                "Nur ad-Din",
                "Baibars",
                "Saladin",
                "Kilij Arslan"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "The Children's Crusade of 1212 was:",
            options = listOf(
                "A successful military campaign led by child soldiers",
                "A peaceful pilgrimage of children to Jerusalem",
                "A failed movement that led to the enslavement of many participants",
                "A fictional event with no historical basis"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 3: Medieval Society
    private val medievalSocietyQuestions = listOf(
        Question(
            text = "In the feudal system, what was a 'fief'?",
            options = listOf(
                "A type of tax paid to the church",
                "Land granted to a vassal by a lord",
                "A medieval court celebration",
                "A system of crop rotation"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What were serfs in medieval society?",
            options = listOf(
                "Skilled craftsmen in towns",
                "Free farmers who owned their land",
                "Bound peasants who worked a lord's land",
                "Traveling merchants"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What was a 'guild' in medieval society?",
            options = listOf(
                "A social club for nobles",
                "An association of merchants or craftsmen",
                "A religious order of monks",
                "A military unit"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which of these was NOT one of the three traditional estates in medieval society?",
            options = listOf(
                "Those who pray (clergy)",
                "Those who fight (nobility)",
                "Those who work (peasantry)",
                "Those who trade (merchants)"
            ),
            correctAnswerIndex = 3
        )
    )

    // Level 4: The Black Death
    private val blackDeathQuestions = listOf(
        Question(
            text = "When did the Black Death first arrive in Europe?",
            options = listOf(
                "1215",
                "1347",
                "1415",
                "1492"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the primary cause of the Black Death?",
            options = listOf(
                "A bacterial infection (Yersinia pestis)",
                "A viral infection similar to influenza",
                "Contaminated food supplies",
                "Punishment from God for sins"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Approximately what percentage of Europe's population died during the Black Death?",
            options = listOf(
                "5-10%",
                "15-25%",
                "30-50%",
                "75-90%"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What long-term economic effect did the Black Death have on Europe?",
            options = listOf(
                "It increased wages due to labor shortages",
                "It strengthened the feudal system",
                "It decreased international trade permanently",
                "It had little economic impact"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 5: The Hundred Years' War
    private val hundredYearsWarQuestions = listOf(
        Question(
            text = "The Hundred Years' War was fought primarily between which two kingdoms?",
            options = listOf(
                "England and France",
                "France and Spain",
                "England and Scotland",
                "France and the Holy Roman Empire"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "How long did the Hundred Years' War actually last?",
            options = listOf(
                "Exactly 100 years",
                "About 50 years",
                "About 75 years",
                "About 116 years"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which French military leader was captured and burned at the stake during the war?",
            options = listOf(
                "Joan of Arc",
                "Eleanor of Aquitaine",
                "Catherine de Medici",
                "Margaret of Anjou"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which weapon revolutionized warfare during the Hundred Years' War?",
            options = listOf(
                "Crossbow",
                "Longbow",
                "Pike",
                "Cannon"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 6: Medieval Technology
    private val medievalTechnologyQuestions = listOf(
        Question(
            text = "Which medieval invention revolutionized farming and food production?",
            options = listOf(
                "The steel plow",
                "The seed drill",
                "The heavy plow with wheels",
                "The combine harvester"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What innovation helped medieval Europeans harness water power more effectively?",
            options = listOf(
                "The horizontal waterwheel",
                "The vertical waterwheel",
                "The steam engine",
                "The water turbine"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which of these was NOT a significant technological advancement in medieval warfare?",
            options = listOf(
                "Chain mail armor",
                "Trebuchets",
                "Gunpowder weapons",
                "Machine guns"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "What medieval invention made it possible to tell time more accurately?",
            options = listOf(
                "Sundial",
                "Mechanical clock",
                "Hourglass",
                "Atomic clock"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 7: The Catholic Church
    private val catholicChurchQuestions = listOf(
        Question(
            text = "What was the Great Schism of 1054?",
            options = listOf(
                "The split between Catholics and Protestants",
                "The split between Roman Catholic and Eastern Orthodox churches",
                "The period when there were two competing popes",
                "The separation of church and state in Europe"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What were indulgences in medieval Christian practice?",
            options = listOf(
                "Special prayers said only by priests",
                "Documents sold to reduce time in purgatory",
                "Exemptions from attending church",
                "Fasting periods before religious holidays"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which of these orders of monks followed the Rule of St. Benedict?",
            options = listOf(
                "Franciscans",
                "Dominicans",
                "Jesuits",
                "Benedictines"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "What was the primary language used in medieval Church services?",
            options = listOf(
                "Greek",
                "Latin",
                "Hebrew",
                "Local vernacular languages"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 8: Medieval Art & Architecture
    private val medievalArtQuestions = listOf(
        Question(
            text = "Which architectural style is characterized by pointed arches and flying buttresses?",
            options = listOf(
                "Romanesque",
                "Gothic",
                "Byzantine",
                "Renaissance"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What is an illuminated manuscript?",
            options = listOf(
                "A book written in gold ink",
                "A manuscript decorated with painted pictures and designs",
                "A document signed by the Pope",
                "A text that glows in the dark"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which material was most commonly used for medieval stained glass colors?",
            options = listOf(
                "Metal oxides",
                "Plant extracts",
                "Crushed gemstones",
                "Synthetic pigments"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What was a triptych in medieval art?",
            options = listOf(
                "A three-paneled artwork",
                "A technique for painting on walls",
                "A type of musical instrument",
                "A sculptural style"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 9: Trade & Commerce
    private val tradeCommerceQuestions = listOf(
        Question(
            text = "Which of these was a powerful medieval trading alliance of merchant guilds?",
            options = listOf(
                "The Holy League",
                "The Hanseatic League",
                "The Trade Federation",
                "The Silk Alliance"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Italian city became wealthy through trade with the Byzantine Empire and the East?",
            options = listOf(
                "Rome",
                "Milan",
                "Florence",
                "Venice"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "What was the primary purpose of medieval fairs?",
            options = listOf(
                "Entertainment and tournaments for nobles",
                "Religious ceremonies and rituals",
                "Long-distance trade and commerce",
                "Agricultural competitions"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What medieval financial innovation allowed merchants to transfer money without carrying coins?",
            options = listOf(
                "Checking accounts",
                "Bills of exchange",
                "Credit cards",
                "Digital wallets"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 10: The Late Middle Ages
    private val lateMiddleAgesQuestions = listOf(
        Question(
            text = "Which event is often considered to mark the end of the Middle Ages?",
            options = listOf(
                "The Fall of Constantinople (1453)",
                "The discovery of America (1492)",
                "The Protestant Reformation (1517)",
                "The Battle of Agincourt (1415)"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What was the Avignon Papacy?",
            options = listOf(
                "A religious order founded in Avignon",
                "A period when popes resided in Avignon instead of Rome",
                "A French attempt to create a separate Catholic Church",
                "A religious council held in Avignon"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which of these early Renaissance artists lived during the Late Middle Ages?",
            options = listOf(
                "Leonardo da Vinci",
                "Michelangelo",
                "Giotto",
                "Raphael"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "The Peasants' Revolt of 1381 took place in which country?",
            options = listOf(
                "France",
                "England",
                "Germany",
                "Italy"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 11: Feudal Japan
    private val feudalJapanQuestions = listOf(
        Question(
            text = "What was the Japanese equivalent of a European knight called?",
            options = listOf(
                "Daimyo",
                "Samurai",
                "Shogun",
                "Ronin"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was bushido?",
            options = listOf(
                "A type of Japanese sword",
                "The samurai code of conduct",
                "A Japanese castle design",
                "A Japanese religious ceremony"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who were the military rulers who effectively governed Japan during most of the medieval period?",
            options = listOf(
                "Emperors",
                "Daimyos",
                "Shoguns",
                "Samurai"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which religion heavily influenced Japanese medieval culture and samurai values?",
            options = listOf(
                "Shinto",
                "Confucianism",
                "Buddhism",
                "Zen Buddhism"
            ),
            correctAnswerIndex = 3
        )
    )

    // Level 12: Byzantine Empire
    private val byzantineEmpireQuestions = listOf(
        Question(
            text = "What was the capital city of the Byzantine Empire?",
            options = listOf(
                "Rome",
                "Athens",
                "Constantinople",
                "Alexandria"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which Byzantine Emperor is known for his code of laws and massive building projects?",
            options = listOf(
                "Constantine",
                "Theodosius",
                "Justinian",
                "Heraclius"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What event in 1054 split the Christian church between East and West?",
            options = listOf(
                "The Great Schism",
                "The Council of Nicaea",
                "The Fourth Crusade",
                "The Fall of Constantinople"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What was the Hagia Sophia originally built as?",
            options = listOf(
                "A palace",
                "A Christian cathedral",
                "A fortress",
                "A marketplace"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 13: Islamic Golden Age
    private val islamicGoldenAgeQuestions = listOf(
        Question(
            text = "Which city was the center of learning and culture during the Islamic Golden Age?",
            options = listOf(
                "Mecca",
                "Baghdad",
                "Cairo",
                "Damascus"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What mathematical concept did Islamic scholars introduce to Europe?",
            options = listOf(
                "Roman numerals",
                "Fractions",
                "Arabic numerals (0-9)",
                "Geometry"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which Islamic scholar wrote 'The Canon of Medicine', a medical encyclopedia used for centuries?",
            options = listOf(
                "Al-Khwarizmi",
                "Ibn Sina (Avicenna)",
                "Ibn Rushd (Averroes)",
                "Ibn Khaldun"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What field of study was founded by the Islamic scholar Al-Khwarizmi?",
            options = listOf(
                "Astronomy",
                "Chemistry",
                "Algebra",
                "Medicine"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 14: Viking Age
    private val vikingAgeQuestions = listOf(
        Question(
            text = "Vikings primarily came from which regions?",
            options = listOf(
                "Britain and Ireland",
                "France and Germany",
                "Scandinavia (Denmark, Norway, Sweden)",
                "Russia and Ukraine"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What was a Viking longship primarily designed for?",
            options = listOf(
                "Fishing",
                "Trade and raiding",
                "Luxury travel",
                "Naval warfare"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Viking explorer is believed to have reached North America around 1000 CE?",
            options = listOf(
                "Erik the Red",
                "Leif Erikson",
                "Harald Hardrada",
                "Rollo"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the Viking parliament called?",
            options = listOf(
                "Althing",
                "Folketing",
                "Riksdag",
                "Storting"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 15: Mongol Empire
    private val mongolEmpireQuestions = listOf(
        Question(
            text = "Who founded the Mongol Empire?",
            options = listOf(
                "Kublai Khan",
                "Attila",
                "Genghis Khan",
                "Timur"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "At its height, the Mongol Empire was:",
            options = listOf(
                "Limited to Central Asia",
                "The largest contiguous land empire in history",
                "Smaller than the Roman Empire",
                "Confined to China and Korea"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the Mongol system of fast communication called?",
            options = listOf(
                "The Silk Road",
                "Pony Express",
                "Yam",
                "Khan Mail"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What religion did many Mongols adopt after their conquests?",
            options = listOf(
                "Christianity",
                "Islam",
                "Buddhism",
                "Hinduism"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 16: Medieval Warfare
    private val medievalWarfareQuestions = listOf(
        Question(
            text = "What was a medieval siege tower used for?",
            options = listOf(
                "Launching projectiles over castle walls",
                "Housing archers to defend castles",
                "Allowing attackers to climb over castle walls",
                "Storing supplies during long sieges"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "How were knights typically armored in the late medieval period?",
            options = listOf(
                "Leather armor only",
                "Chain mail only",
                "Full plate armor",
                "No armor, for maximum mobility"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What was the primary battlefield formation for medieval infantry?",
            options = listOf(
                "Skirmish line",
                "Shield wall",
                "Phalanx",
                "Column"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "When did gunpowder weapons begin to change European warfare?",
            options = listOf(
                "Early Middle Ages (500-1000)",
                "High Middle Ages (1000-1250)",
                "Late Middle Ages (1250-1500)",
                "Renaissance period (after 1500)"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 17: Medieval Medicine
    private val medievalMedicineQuestions = listOf(
        Question(
            text = "What theory dominated medieval medicine, based on bodily 'humors'?",
            options = listOf(
                "Germ theory",
                "Humorism",
                "Cellular theory",
                "Contagion theory"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was bloodletting used for in medieval medicine?",
            options = listOf(
                "Blood testing",
                "Removing excess blood to balance humors",
                "Injecting medicines",
                "Religious rituals"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which of these was NOT a common medieval treatment?",
            options = listOf(
                "Herbal remedies",
                "Prayer and religious relics",
                "Trepanation (drilling holes in the skull)",
                "Antibiotics"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Who were the primary medical practitioners in medieval towns?",
            options = listOf(
                "University-trained physicians",
                "Barber-surgeons",
                "Apothecaries",
                "All of the above"
            ),
            correctAnswerIndex = 3
        )
    )

    // Level 18: Medieval Music
    private val medievalMusicQuestions = listOf(
        Question(
            text = "What was Gregorian chant?",
            options = listOf(
                "A type of secular dance music",
                "Religious vocal music of the Catholic Church",
                "Military marching songs",
                "Folk music of peasants"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which medieval instrument was similar to a modern guitar?",
            options = listOf(
                "Hurdy-gurdy",
                "Lute",
                "Shawm",
                "Recorder"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What were troubadours known for?",
            options = listOf(
                "Religious hymns",
                "Military battle songs",
                "Love songs and poetry",
                "Instrumental dance music"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which of these was NOT a common medieval instrument?",
            options = listOf(
                "Harp",
                "Bagpipes",
                "Piano",
                "Drums"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 19: Medieval Literature
    private val medievalLiteratureQuestions = listOf(
        Question(
            text = "Who wrote 'The Canterbury Tales'?",
            options = listOf(
                "Geoffrey Chaucer",
                "Dante Alighieri",
                "Christine de Pizan",
                "Giovanni Boccaccio"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What is the epic poem 'Beowulf' about?",
            options = listOf(
                "A Christian pilgrim's journey",
                "A knight's quest for the Holy Grail",
                "A warrior who fights monsters",
                "A royal court's political intrigue"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Medieval romances often focused on:",
            options = listOf(
                "Realistic depictions of peasant life",
                "Chivalry and courtly love",
                "Religious sermons and teachings",
                "Historical chronicles"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was 'The Divine Comedy' by Dante?",
            options = listOf(
                "A humorous play about medieval life",
                "A collection of bawdy tales",
                "An epic poem about a journey through Hell, Purgatory, and Heaven",
                "A political satire of the pope"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 20: End of the Middle Ages
    private val endOfMiddleAgesQuestions = listOf(
        Question(
            text = "What 1453 event is often cited as the end of the Middle Ages?",
            options = listOf(
                "The end of the Hundred Years' War",
                "The fall of Constantinople to the Ottoman Turks",
                "The beginning of the Renaissance in Italy",
                "The discovery of the Americas"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which invention helped spread ideas at the end of the Middle Ages?",
            options = listOf(
                "The telephone",
                "The printing press",
                "The steam engine",
                "The compass"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What led to the decline of feudalism at the end of the medieval period?",
            options = listOf(
                "The rise of centralized monarchies",
                "The spread of communism",
                "The Industrial Revolution",
                "The American Revolution"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What intellectual movement emerged at the end of the Middle Ages?",
            options = listOf(
                "Romanticism",
                "Enlightenment",
                "Humanism",
                "Modernism"
            ),
            correctAnswerIndex = 2
        )
    )

    // Group all questions by level
    private val questionsByLevel = mapOf(
        "1" to earlyMiddleAgesQuestions,
        "2" to crusadesQuestions,
        "3" to medievalSocietyQuestions,
        "4" to blackDeathQuestions,
        "5" to hundredYearsWarQuestions,
        "6" to medievalTechnologyQuestions,
        "7" to catholicChurchQuestions,
        "8" to medievalArtQuestions,
        "9" to tradeCommerceQuestions,
        "10" to lateMiddleAgesQuestions,
        "11" to feudalJapanQuestions,
        "12" to byzantineEmpireQuestions,
        "13" to islamicGoldenAgeQuestions,
        "14" to vikingAgeQuestions,
        "15" to mongolEmpireQuestions,
        "16" to medievalWarfareQuestions,
        "17" to medievalMedicineQuestions,
        "18" to medievalMusicQuestions,
        "19" to medievalLiteratureQuestions,
        "20" to endOfMiddleAgesQuestions
    )

    // Method to get questions by level
    fun getQuestionsByLevel(levelId: String): List<Question> {
        return when (levelId) {
            "1" -> earlyMiddleAgesQuestions
            "2" -> crusadesQuestions
            "3" -> medievalSocietyQuestions
            "4" -> blackDeathQuestions
            "5" -> hundredYearsWarQuestions
            "6" -> medievalTechnologyQuestions
            "7" -> catholicChurchQuestions
            "8" -> medievalArtQuestions
            "9" -> tradeCommerceQuestions
            "10" -> lateMiddleAgesQuestions
            "11" -> feudalJapanQuestions
            "12" -> byzantineEmpireQuestions
            "13" -> islamicGoldenAgeQuestions
            "14" -> vikingAgeQuestions
            "15" -> mongolEmpireQuestions
            "16" -> medievalWarfareQuestions
            "17" -> medievalMedicineQuestions
            "18" -> medievalMusicQuestions
            "19" -> medievalLiteratureQuestions
            "20" -> endOfMiddleAgesQuestions
            else -> emptyList()
        }
    }
    
    /**
     * Get a specified number of random questions from all medieval questions
     * Used as a fallback when specific level questions aren't available
     */
    fun getRandomQuestions(count: Int): List<Question> {
        val allQuestions = listOf(
            earlyMiddleAgesQuestions,
            crusadesQuestions,
            medievalSocietyQuestions,
            blackDeathQuestions,
            hundredYearsWarQuestions,
            medievalTechnologyQuestions,
            catholicChurchQuestions,
            medievalArtQuestions,
            tradeCommerceQuestions,
            lateMiddleAgesQuestions,
            feudalJapanQuestions,
            byzantineEmpireQuestions,
            islamicGoldenAgeQuestions,
            vikingAgeQuestions,
            mongolEmpireQuestions,
            medievalWarfareQuestions,
            medievalMedicineQuestions,
            medievalMusicQuestions,
            medievalLiteratureQuestions,
            endOfMiddleAgesQuestions
        ).flatten()
        
        return if (allQuestions.isEmpty()) {
            // If there are no questions at all, return empty list
            emptyList()
        } else if (allQuestions.size <= count) {
            // If we don't have enough questions, return all available
            allQuestions
        } else {
            // Return random selection of questions
            allQuestions.shuffled().take(count)
        }
    }
} 