package com.hikmet.imperium.data

import com.hikmet.imperium.data.Question

/**
 * Database of Ancient Civilizations quiz questions
 */
object AncientQuizData {

    // Level 1: Ancient Egypt
    private val ancientEgyptQuestions = listOf(
        Question(
            text = "Which ancient Egyptian structure is the oldest of the Seven Wonders of the Ancient World?",
            options = listOf(
                "Great Pyramid of Giza",
                "Hanging Gardens of Babylon",
                "Lighthouse of Alexandria",
                "Colossus of Rhodes"
            ),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which pharaoh's tomb was discovered nearly intact by Howard Carter in 1922?",
            options = listOf(
                "Ramses II",
                "Tutankhamun",
                "Cleopatra",
                "Khufu"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What is the name of the ancient Egyptian writing system?",
            options = listOf(
                "Cuneiform",
                "Sanskrit",
                "Hieroglyphics",
                "Aramaic"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which river was central to ancient Egyptian civilization?",
            options = listOf(
                "Tigris",
                "Euphrates",
                "Jordan",
                "Nile"
            ),
            correctAnswerIndex = 3
        )
    )

    // Level 2: Ancient Egyptian Society
    private val egyptianSocietyQuestions = listOf(
        Question(
            text = "Who was the Egyptian queen who aligned herself with Julius Caesar and Mark Antony?",
            options = listOf(
                "Nefertiti",
                "Cleopatra VII",
                "Hatshepsut",
                "Nefertari"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the main purpose of the Egyptian pyramids?",
            options = listOf(
                "Temples for worship",
                "Astronomical observatories",
                "Tombs for pharaohs",
                "Granaries for food storage"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What material did ancient Egyptians use to write on?",
            options = listOf(
                "Parchment",
                "Cotton",
                "Silk",
                "Papyrus"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which pharaoh promoted the exclusive worship of the Aten?",
            options = listOf(
                "Akhenaten",
                "Ramses II",
                "Tutankhamun",
                "Thutmose III"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 3: Ancient Egyptian Culture
    private val egyptianCultureQuestions = listOf(
        Question(
            text = "What is the name of the legendary half-lion, half-human statue near the Pyramids of Giza?",
            options = listOf(
                "The Minotaur",
                "The Sphinx",
                "The Chimera",
                "The Centaur"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which ancient Egyptian god is associated with the afterlife and resurrection?",
            options = listOf(
                "Anubis",
                "Ra",
                "Osiris",
                "Horus"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What was the name of the process used by ancient Egyptians to preserve bodies?",
            options = listOf(
                "Cryogenics",
                "Cremation",
                "Burial at sea",
                "Mummification"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which female pharaoh ruled during Egypt's 18th dynasty and established trading networks?",
            options = listOf(
                "Hatshepsut",
                "Nefertiti",
                "Cleopatra",
                "Meritaten"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 4: Egyptian Legacy
    private val egyptianLegacyQuestions = listOf(
        Question(
            text = "What stone, found in 1799, helped scholars decipher Egyptian hieroglyphics?",
            options = listOf(
                "Philosopher's Stone",
                "Rosetta Stone",
                "Moabite Stone",
                "Behistun Rock"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the ancient Egyptian name for their country?",
            options = listOf(
                "Aegyptus",
                "Nubia",
                "Kemet",
                "Mesopotamia"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which pharaoh commissioned the construction of the temple at Abu Simbel?",
            options = listOf(
                "Tutankhamun",
                "Thutmose III",
                "Akhenaten",
                "Ramses II"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "In ancient Egyptian religion, what was the feather of Ma'at used for?",
            options = listOf(
                "Weighing against the deceased's heart",
                "Crowning new pharaohs",
                "Warding off evil spirits",
                "Healing the sick"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 5: Ancient Greece
    private val ancientGreeceQuestions = listOf(
        Question(
            text = "Which hero was traditionally credited with unifying Attica under Athens?",
            options = listOf(
                "Theseus",
                "Athena",
                "Perseus",
                "Hercules"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the main language of ancient Greece?",
            options = listOf(
                "Latin",
                "Phoenician",
                "Greek",
                "Aramaic"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which ancient Greek philosopher was the teacher of Alexander the Great?",
            options = listOf(
                "Plato",
                "Socrates",
                "Diogenes",
                "Aristotle"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "What was the name of the famous oracle in ancient Greece?",
            options = listOf(
                "Oracle of Delphi",
                "Oracle of Athens",
                "Oracle of Olympus",
                "Oracle of Sparta"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 6: Classical Greece
    private val classicalGreeceQuestions = listOf(
        Question(
            text = "What was the name of the conflict between Athens and Sparta?",
            options = listOf(
                "Persian War",
                "Peloponnesian War",
                "Trojan War",
                "Macedonian War"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What form of government was practiced in Athens during its Golden Age?",
            options = listOf(
                "Monarchy",
                "Oligarchy",
                "Democracy",
                "Theocracy"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Who was the Greek historian known as the 'Father of History'?",
            options = listOf(
                "Thucydides",
                "Xenophon",
                "Plutarch",
                "Herodotus"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "What was the name of the ancient Greek theater festival held in Athens?",
            options = listOf(
                "Dionysia",
                "Olympia",
                "Pythian Games",
                "Panathenaea"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 7: Greek Culture and Philosophy
    private val greekCultureQuestions = listOf(
        Question(
            text = "Which philosopher taught Plato and left no writings of his own?",
            options = listOf(
                "Plato",
                "Socrates",
                "Aristotle",
                "Pythagoras"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which Greek mathematician formulated the theorem about right triangles?",
            options = listOf(
                "Euclid",
                "Archimedes",
                "Pythagoras",
                "Thales"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What ancient Greek epic poem tells the story of Odysseus?",
            options = listOf(
                "The Iliad",
                "Theogony",
                "Works and Days",
                "The Odyssey"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which Greek god was the ruler of Mount Olympus?",
            options = listOf(
                "Zeus",
                "Poseidon",
                "Apollo",
                "Hades"
            ),
            correctAnswerIndex = 0
        )
    )

    // Level 8: Ancient Rome
    private val ancientRomeQuestions = listOf(
        Question(
            text = "According to legend, who founded Rome?",
            options = listOf(
                "Julius Caesar",
                "Romulus and Remus",
                "Augustus",
                "Aeneas"
            ),
            correctAnswerIndex = 1
        ),
        Question(
            text = "What was the main language of ancient Rome?",
            options = listOf(
                "Greek",
                "Etruscan",
                "Latin",
                "Oscan"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            text = "What was the Latin term for a Roman citizen?",
            options = listOf(
                "Patrician",
                "Plebeian",
                "Senator",
                "Civis"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which structure was built to entertain Romans with gladiatorial games?",
            options = listOf(
                "Colosseum",
                "Pantheon",
                "Forum",
                "Circus Maximus"
            ),
            correctAnswerIndex = 0
        )
    )

    fun getQuestionsByLevel(levelId: String): List<Question> {
        return when (levelId) {
            "1" -> ancientEgyptQuestions
            "2" -> egyptianSocietyQuestions
            "3" -> egyptianCultureQuestions
            "4" -> egyptianLegacyQuestions
            "5" -> ancientGreeceQuestions
            "6" -> classicalGreeceQuestions
            "7" -> greekCultureQuestions
            "8" -> ancientRomeQuestions
            else -> emptyList()
        }
    }
}
