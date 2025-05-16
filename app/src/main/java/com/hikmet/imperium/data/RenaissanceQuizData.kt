package com.hikmet.imperium.data

import com.hikmet.imperium.data.Question

/**
 * Database of Renaissance quiz questions
 */
object RenaissanceQuizData {

    // Level 1: Early Renaissance
    private val earlyRenaissanceQuestions = listOf(
        Question(
            text = "In which Italian city did the Renaissance begin?",
            options = listOf(
                "Rome",
                "Venice",
                "Florence",
                "Milan"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which of these families was a major patron of Renaissance art in Florence?",
            options = listOf(
                "Borgia",
                "Medici",
                "Sforza",
                "Visconti"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which event helped spark the Renaissance in Italy?",
            options = listOf(
                "The fall of Constantinople",
                "The discovery of America",
                "The Black Death",
                "The Hundred Years' War"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What does 'Renaissance' literally mean?",
            options = listOf(
                "New Age",
                "Golden Era",
                "Rebirth",
                "Enlightenment"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 2: Renaissance Art
    private val renaissanceArtQuestions = listOf(
        Question(
            text = "Who painted the ceiling of the Sistine Chapel?",
            options = listOf(
                "Leonardo da Vinci",
                "Raphael",
                "Michelangelo",
                "Donatello"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which technique, perfected during the Renaissance, creates the illusion of depth?",
            options = listOf(
                "Chiaroscuro",
                "Linear perspective",
                "Sfumato",
                "Tenebrism"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who painted 'The Birth of Venus'?",
            options = listOf(
                "Titian",
                "Botticelli",
                "Raphael",
                "Giotto"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which of these is NOT a Teenage Mutant Ninja Turtle namesake?",
            options = listOf(
                "Leonardo",
                "Michelangelo",
                "Raphael",
                "Brunelleschi"
            ),
            correctAnswerIndex = 3
        )
    )

    // Level 3: Humanism
    private val humanismQuestions = listOf(
        Question(
            text = "What was the central focus of Renaissance Humanism?",
            options = listOf(
                "Religious devotion",
                "Human potential and achievements",
                "Military conquest",
                "Agricultural development"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which ancient languages did Renaissance humanists revive?",
            options = listOf(
                "Hebrew and Sanskrit",
                "Greek and Latin",
                "Aramaic and Phoenician",
                "Egyptian and Persian"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who wrote 'The Prince', a political treatise embodying Renaissance thinking?",
            options = listOf(
                "Petrarch",
                "Erasmus",
                "Niccolò Machiavelli",
                "Thomas More"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What did humanists generally believe about education?",
            options = listOf(
                "It should focus only on religious texts",
                "It was unnecessary for most people",
                "It should include classical literature and rhetoric",
                "It should be limited to practical skills"
            ),
            correctAnswerIndex = 2
        )
    )

    // Level 4: The Medici Family
    private val mediciQuestions = listOf(
        Question(
            text = "What was the main source of the Medici family's initial wealth?",
            options = listOf(
                "Banking",
                "Textile trade",
                "Agriculture",
                "Military conquests"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which Medici became Pope Leo X?",
            options = listOf(
                "Cosimo de' Medici",
                "Lorenzo de' Medici",
                "Giovanni de' Medici",
                "Piero de' Medici"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which Medici was known as 'Lorenzo the Magnificent'?",
            options = listOf(
                "Lorenzo de' Medici",
                "Cosimo de' Medici",
                "Giovanni de' Medici",
                "Piero de' Medici"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which famous artist was supported by the Medici family?",
            options = listOf(
                "Caravaggio",
                "Botticelli",
                "El Greco",
                "Vermeer"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 5: Renaissance Architecture
    private val renaissanceArchitectureQuestions = listOf(
        Question(
            text = "Who designed the dome of Florence Cathedral?",
            options = listOf(
                "Michelangelo",
                "Filippo Brunelleschi",
                "Leon Battista Alberti",
                "Andrea Palladio"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What ancient architectural style heavily influenced Renaissance architecture?",
            options = listOf(
                "Egyptian",
                "Greek and Roman",
                "Persian",
                "Byzantine"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which architectural element was widely used in Renaissance buildings?",
            options = listOf(
                "Flying buttresses",
                "Minarets",
                "Classical columns",
                "Pagoda roofs"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "St. Peter's Basilica in Vatican City was primarily designed by:",
            options = listOf(
                "Michelangelo and Bramante",
                "Da Vinci and Raphael",
                "Brunelleschi and Alberti",
                "Palladio and Bernini"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 6: Science & Invention
    private val renaissanceScienceQuestions = listOf(
        Question(
            text = "Who is considered the father of modern observational astronomy?",
            options = listOf(
                "Copernicus",
                "Galileo Galilei",
                "Johannes Kepler",
                "Tycho Brahe"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Renaissance artist created detailed anatomical drawings?",
            options = listOf(
                "Raphael",
                "Titian",
                "Leonardo da Vinci",
                "Botticelli"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What controversial theory did Copernicus propose?",
            options = listOf(
                "Evolution",
                "Heliocentrism (sun-centered solar system)",
                "Gravity",
                "Continental drift"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Renaissance invention most helped spread knowledge?",
            options = listOf(
                "Telescope",
                "Printing press",
                "Microscope",
                "Mechanical clock"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 7: The Printing Press
    private val printingPressQuestions = listOf(
        Question(
            text = "Who is credited with inventing the movable type printing press in Europe?",
            options = listOf(
                "Johannes Gutenberg",
                "William Caxton",
                "Aldus Manutius",
                "Johann Fust"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "What was the first major book printed with Gutenberg's press?",
            options = listOf(
                "The Divine Comedy",
                "The Gutenberg Bible",
                "Canterbury Tales",
                "Book of Hours"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "How did the printing press affect literacy in Europe?",
            options = listOf(
                "It had no significant effect",
                "It decreased literacy rates",
                "It increased literacy rates",
                "It only affected the nobility"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What impact did the printing press have on the Protestant Reformation?",
            options = listOf(
                "It helped spread reformist ideas widely",
                "It had no significant impact",
                "It hindered the spread of Protestant ideas",
                "It was banned by Protestant leaders"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 8: Renaissance Literature
    private val renaissanceLiteratureQuestions = listOf(
        Question(
            text = "Who wrote 'The Canterbury Tales'?",
            options = listOf(
                "William Shakespeare",
                "Geoffrey Chaucer",
                "Miguel de Cervantes",
                "Dante Alighieri"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who wrote 'Don Quixote'?",
            options = listOf(
                "Miguel de Cervantes",
                "Niccolò Machiavelli",
                "Giovanni Boccaccio",
                "François Rabelais"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which play is NOT by William Shakespeare?",
            options = listOf(
                "Hamlet",
                "Romeo and Juliet",
                "Doctor Faustus",
                "Macbeth"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which literary form did Petrarch help popularize?",
            options = listOf(
                "Novel",
                "Sonnet",
                "Epic poem",
                "Drama"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 9: Renaissance Music
    private val renaissanceMusicQuestions = listOf(
        Question(
            text = "Which instrument was NOT commonly used in Renaissance music?",
            options = listOf(
                "Lute",
                "Harpsichord",
                "Piano",
                "Recorder"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Renaissance music was characterized by:",
            options = listOf(
                "Heavy use of electric instruments",
                "Polyphonic harmonies with multiple melodic lines",
                "Simple melodies with drum-heavy accompaniment",
                "Exclusively instrumental compositions"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who was a famous Renaissance composer?",
            options = listOf(
                "Johann Sebastian Bach",
                "Wolfgang Amadeus Mozart",
                "Giovanni Pierluigi da Palestrina",
                "Ludwig van Beethoven"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What type of singing was prominent in Renaissance church music?",
            options = listOf(
                "Opera",
                "A cappella",
                "Yodeling",
                "Rap"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 10: The Catholic Church
    private val catholicChurchQuestions = listOf(
        Question(
            text = "Who began the Protestant Reformation by posting the 95 Theses?",
            options = listOf(
                "John Calvin",
                "Martin Luther",
                "Henry VIII",
                "John Wycliffe"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the Catholic Church's response to the Protestant Reformation?",
            options = listOf(
                "They immediately accepted the reforms",
                "They ignored it completely",
                "The Counter-Reformation",
                "They abandoned Christianity"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What was the Council of Trent?",
            options = listOf(
                "A meeting of Protestant leaders",
                "A Catholic ecumenical council addressing reform",
                "A peace treaty between Protestants and Catholics",
                "An art exhibition of religious works"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What were indulgences, which became controversial during this period?",
            options = listOf(
                "Religious holidays",
                "Payments to reduce punishment for sins",
                "Diplomatic agreements between nations",
                "Special prayers for the dead"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 11: Northern Renaissance
    private val northernRenaissanceQuestions = listOf(
        Question(
            text = "Which painter created 'The Garden of Earthly Delights'?",
            options = listOf(
                "Jan van Eyck",
                "Albrecht Dürer",
                "Hieronymus Bosch",
                "Pieter Bruegel the Elder"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which humanist wrote 'In Praise of Folly'?",
            options = listOf(
                "Thomas More",
                "Erasmus",
                "Johannes Gutenberg",
                "Martin Luther"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "How did the Northern Renaissance differ from the Italian Renaissance?",
            options = listOf(
                "It focused more on religious subjects",
                "It rejected classical influences entirely",
                "It was exclusively about architecture",
                "It was limited to music only"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which innovation did Jan van Eyck help perfect?",
            options = listOf(
                "Oil painting",
                "Stone sculpting",
                "Metalworking",
                "Woodblock printing"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 12: Women in the Renaissance
    private val womenRenaissanceQuestions = listOf(
        Question(
            text = "Which female Renaissance artist painted 'Self-Portrait at the Easel'?",
            options = listOf(
                "Artemisia Gentileschi",
                "Sofonisba Anguissola",
                "Lavinia Fontana",
                "Properzia de' Rossi"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who was Christine de Pizan?",
            options = listOf(
                "A queen of France",
                "A Renaissance writer and proto-feminist",
                "An Italian painter",
                "A Spanish composer"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was unusual about Renaissance noblewoman Isabella d'Este?",
            options = listOf(
                "She was a ruler in her own right",
                "She was a prominent art patron and political figure",
                "She was the first female university professor",
                "She was a leading military commander"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What limited most women's participation in Renaissance art and culture?",
            options = listOf(
                "Legal prohibitions against female artists",
                "Lack of societal acceptance and limited education",
                "Physical inability to use artistic tools",
                "Religious bans on female creativity"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 13: The Age of Exploration
    private val explorationQuestions = listOf(
        Question(
            text = "Who was the first European explorer to reach India by sea?",
            options = listOf(
                "Christopher Columbus",
                "Ferdinand Magellan",
                "Vasco da Gama",
                "John Cabot"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What Renaissance innovation made long-distance navigation easier?",
            options = listOf(
                "Telescope",
                "Improved magnetic compass",
                "Steam engine",
                "Telegraph"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Who sponsored Christopher Columbus's voyages?",
            options = listOf(
                "King Henry VIII of England",
                "Queen Isabella and King Ferdinand of Spain",
                "The Medici family of Florence",
                "The Pope"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the main goal of early Renaissance explorers?",
            options = listOf(
                "Scientific discovery",
                "Finding new trade routes to Asia",
                "Spreading Christianity",
                "Establishing colonies"
            ),
            correctAnswerIndex = 1
        )
    )

    // Level 14: High Renaissance
    private val highRenaissanceQuestions = listOf(
        Question(
            text = "Which three artists are considered the masters of the High Renaissance?",
            options = listOf(
                "Botticelli, Giotto, and Titian",
                "Leonardo, Michelangelo, and Raphael",
                "Donatello, Brunelleschi, and Ghiberti",
                "Bellini, Caravaggio, and El Greco"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "In which city was Leonardo da Vinci's 'The Last Supper' painted?",
            options = listOf(
                "Rome",
                "Florence",
                "Milan",
                "Venice"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What technique did Leonardo da Vinci use to create soft, hazy effects in his paintings?",
            options = listOf(
                "Impasto",
                "Pointillism",
                "Sfumato",
                "Fresco"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which of Raphael's works depicts famous philosophers and scientists gathered together?",
            options = listOf(
                "The School of Athens",
                "Disputation of the Holy Sacrament",
                "The Transfiguration",
                "Sistine Madonna"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 15: Politics & Diplomacy
    private val politicsQuestions = listOf(
        Question(
            text = "What political concept did Machiavelli explore in 'The Prince'?",
            options = listOf(
                "Democratic republics",
                "Maintaining power through pragmatic means",
                "Communist ideals",
                "Religious governance"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Renaissance statesman wrote 'Utopia'?",
            options = listOf(
                "Francis Bacon",
                "Niccolò Machiavelli",
                "Thomas More",
                "Thomas Hobbes"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which diplomatic innovation became widespread during the Renaissance?",
            options = listOf(
                "Summit meetings",
                "United Nations-style organizations",
                "Resident ambassadors",
                "International courts"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "How did the concept of the 'balance of power' develop during the Renaissance?",
            options = listOf(
                "It was imposed by the Catholic Church",
                "It emerged from Italian city-states preventing any one state from dominating",
                "It was created by the Ottoman Empire",
                "It was a French innovation"
            ),
            correctAnswerIndex = 1
        )
    )

    // Group all questions by level
    private val questionsByLevel = mapOf(
        "1" to earlyRenaissanceQuestions,
        "2" to renaissanceArtQuestions,
        "3" to humanismQuestions,
        "4" to mediciQuestions,
        "5" to renaissanceArchitectureQuestions,
        "6" to renaissanceScienceQuestions,
        "7" to printingPressQuestions,
        "8" to renaissanceLiteratureQuestions,
        "9" to renaissanceMusicQuestions,
        "10" to catholicChurchQuestions,
        "11" to northernRenaissanceQuestions,
        "12" to womenRenaissanceQuestions,
        "13" to explorationQuestions,
        "14" to highRenaissanceQuestions,
        "15" to politicsQuestions
    )

    // Method to get questions by level
    fun getQuestionsByLevel(levelId: String): List<Question> {
        return questionsByLevel[levelId] ?: emptyList()
    }
    
    /**
     * Get a specified number of random questions from all Renaissance questions
     * Used as a fallback when specific level questions aren't available
     */
    fun getRandomQuestions(count: Int = 4): List<Question> {
        val allQuestions = questionsByLevel.values.flatten()
        
        return if (allQuestions.isEmpty()) {
            emptyList()
        } else if (allQuestions.size <= count) {
            allQuestions
        } else {
            allQuestions.shuffled().take(count)
        }
    }
} 