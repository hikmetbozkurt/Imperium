package com.hikmet.imperium.data

import com.hikmet.imperium.data.Question // Assuming Question data class is in this package

object ModernHistoryQuizData {
    // Level 1: Industrial Revolution (1780-1840)
    private val industrialRevolutionQuestions = listOf(
        Question(text = "Which invention revolutionized textile manufacturing?", options = listOf("Spinning Jenny", "Cotton Gin", "Steam Engine", "Power Loom"), correctAnswerIndex = 0),
        Question(text = "In which country did the Industrial Revolution begin?", options = listOf("France", "Germany", "US", "Great Britain"), correctAnswerIndex = 3),
        Question(text = "What was the primary energy source for early factories?", options = listOf("Electricity", "Steam", "Water", "Coal"), correctAnswerIndex = 3),
        Question(text = "Which transportation innovation was crucial?", options = listOf("Automobile", "Steam Locomotive", "Airplane", "Canal System"), correctAnswerIndex = 1)
    )

    // Level 2: Steam Power Era
    private val steamPowerEraQuestions = listOf(
        Question(text = "Who is credited with improving the steam engine significantly?", options = listOf("Thomas Newcomen", "James Watt", "Richard Arkwright", "George Stephenson"), correctAnswerIndex = 1),
        Question(text = "The first public steam railway, the Stockton and Darlington Railway, opened in:", options = listOf("1805", "1825", "1830", "1845"), correctAnswerIndex = 1),
        Question(text = "Steam power was NOT initially used for:", options = listOf("Pumping water from mines", "Powering locomotives", "Driving factory machinery", "Generating electricity for homes"), correctAnswerIndex = 3),
        Question(text = "Which of these was a major social impact of steam power?", options = listOf("Decreased urbanization", "Increased cottage industries", "Faster travel and transport of goods", "Reduced need for coal"), correctAnswerIndex = 2)
    )

    // Level 3: World Wars (1914-1945)
    private val worldWarsQuestions = listOf(
        Question(text = "What year did WWI begin?", options = listOf("1912", "1914", "1916", "1918"), correctAnswerIndex = 1),
        Question(text = "Which event triggered WWI?", options = listOf("Sinking of Lusitania", "Zimmerman Telegram", "Assassination of Archduke Ferdinand", "Invasion of Poland"), correctAnswerIndex = 2),
        Question(text = "The primary cause of WWII in Europe was:", options = listOf("The Great Depression", "Japanese expansionism", "German aggression under Hitler", "The failure of the League of Nations"), correctAnswerIndex = 2),
        Question(text = "Which country suffered most casualties in WWII?", options = listOf("UK", "US", "Germany", "Soviet Union"), correctAnswerIndex = 3)
    )

    // Level 4: Cold War Begins (1947-1960)
    private val coldWarBeginsQuestions = listOf(
        Question(text = "What was the 'Iron Curtain'?", options = listOf("Berlin Wall", "Political division of Europe", "Nuclear Test Site", "Soviet spy network"), correctAnswerIndex = 1),
        Question(text = "The Truman Doctrine was aimed at containing communism primarily in:", options = listOf("Asia and Africa", "Greece and Turkey", "Latin America", "Western Europe"), correctAnswerIndex = 1),
        Question(text = "NATO was formed in 1949 as a:", options = listOf("Economic alliance", "Cultural exchange program", "Military alliance against Soviet aggression", "United Nations peacekeeping force"), correctAnswerIndex = 2),
        Question(text = "The Korean War (1950-1953) was a conflict between:", options = listOf("North and South Korea only", "China and Japan", "North Korea (backed by USSR/China) and South Korea (backed by UN/USA)", "USA and USSR directly"), correctAnswerIndex = 2)
    )

    // Level 5: Space Race (1955-1975)
    private val spaceRaceQuestions = listOf(
        Question(text = "Which country launched Sputnik 1, the first artificial satellite?", options = listOf("USA", "Soviet Union", "China", "France"), correctAnswerIndex = 1),
        Question(text = "Who was the first human in space?", options = listOf("Neil Armstrong", "Yuri Gagarin", "John Glenn", "Alan Shepard"), correctAnswerIndex = 1),
        Question(text = "Which Apollo mission first landed humans on the moon?", options = listOf("Apollo 1", "Apollo 11", "Apollo 13", "Apollo 17"), correctAnswerIndex = 1),
        Question(text = "The Space Race was primarily a competition between:", options = listOf("USA and China", "USA and Soviet Union", "Soviet Union and Europe", "USA and Japan"), correctAnswerIndex = 1)
    )

    // Level 6: Digital Dawn (1960-1980)
    private val digitalDawnQuestions = listOf(
        Question(text = "The invention of the transistor replaced what larger, less efficient component?", options = listOf("Resistors", "Capacitors", "Vacuum tubes", "Inductors"), correctAnswerIndex = 2),
        Question(text = "ARPANET, the precursor to the modern internet, was developed by:", options = listOf("Microsoft", "IBM", "The U.S. Department of Defense", "Bell Labs"), correctAnswerIndex = 2),
        Question(text = "The first commercially successful microprocessor was the Intel 4004, released in:", options = listOf("1961", "1971", "1981", "1991"), correctAnswerIndex = 1),
        Question(text = "Which company introduced the first popular home computer, the Apple II?", options = listOf("IBM", "Microsoft", "Commodore", "Apple"), correctAnswerIndex = 3)
    )

    // Level 7: Globalization (1980-2000)
    private val globalizationQuestions = listOf(
        Question(text = "The fall of the Berlin Wall in 1989 symbolized:", options = listOf("The start of the Space Race", "The rise of China", "The end of the Cold War and beginning of a more interconnected world", "The creation of the European Union"), correctAnswerIndex = 2),
        Question(text = "The World Trade Organization (WTO) was established in:", options = listOf("1945", "1975", "1995", "2005"), correctAnswerIndex = 2),
        Question(text = "Outsourcing is a key feature of globalization, meaning:", options = listOf("Selling goods only domestically", "Hiring workers from other countries for lower costs", "Restricting international trade", "Investing in local businesses"), correctAnswerIndex = 1),
        Question(text = "The North American Free Trade Agreement (NAFTA) primarily involved which countries?", options = listOf("USA, UK, Canada", "USA, Mexico, Canada", "USA, Brazil, Argentina", "USA, China, Japan"), correctAnswerIndex = 1)
    )

    // Level 8: Internet Revolution (1990-2005)
    private val internetRevolutionQuestions = listOf(
        Question(text = "Tim Berners-Lee is credited with inventing:", options = listOf("The first search engine", "The World Wide Web", "Email", "The first social media platform"), correctAnswerIndex = 1),
        Question(text = "The dot-com bubble burst around what year?", options = listOf("1995", "2000", "2005", "2010"), correctAnswerIndex = 1),
        Question(text = "Which company, founded in 1998, revolutionized online search?", options = listOf("Yahoo", "AltaVista", "Google", "Ask Jeeves"), correctAnswerIndex = 2),
        Question(text = "Napster, launched in 1999, was famous for:", options = listOf("Online shopping", "Peer-to-peer music file sharing", "The first blog platform", "Early social networking"), correctAnswerIndex = 1)
    )

    // Level 9: Climate Awareness (1980s-Present)
    private val climateAwarenessQuestions = listOf(
        Question(text = "The Intergovernmental Panel on Climate Change (IPCC) was established in:", options = listOf("1978", "1988", "1998", "2008"), correctAnswerIndex = 1),
        Question(text = "The Kyoto Protocol (1997) aimed to:", options = listOf("Ban ozone-depleting substances", "Reduce greenhouse gas emissions", "Protect endangered species", "Fund renewable energy research"), correctAnswerIndex = 1),
        Question(text = "What is considered the primary cause of current global warming?", options = listOf("Solar flares", "Volcanic eruptions", "Increased human-caused greenhouse gas emissions", "Deforestation only"), correctAnswerIndex = 2),
        Question(text = "The Paris Agreement, adopted in 2015, aims to limit global warming to well below:", options = listOf("1 degree Celsius", "2 degrees Celsius", "3 degrees Celsius", "0.5 degrees Celsius"), correctAnswerIndex = 1)
    )

    // Level 10: War on Terror (2001-2021)
    private val warOnTerrorQuestions = listOf(
        Question(text = "The 9/11 terrorist attacks targeted which country?", options = listOf("United Kingdom", "France", "United States", "Germany"), correctAnswerIndex = 2),
        Question(text = "Following 9/11, the US and its allies invaded which country in 2001?", options = listOf("Iraq", "Iran", "Syria", "Afghanistan"), correctAnswerIndex = 3),
        Question(text = "Osama bin Laden, the leader of Al-Qaeda, was killed in which country?", options = listOf("Afghanistan", "Pakistan", "Iraq", "Yemen"), correctAnswerIndex = 1),
        Question(text = "The 'War on Terror' led to increased:", options = listOf("Global peace and stability", "Government surveillance and security measures", "International cooperation on all fronts", "Funding for education"), correctAnswerIndex = 1)
    )

    // Level 11: Smartphone Era (2007-Present)
    private val smartphoneEraQuestions = listOf(
        Question(text = "Which company launched the first widely successful smartphone, the iPhone, in 2007?", options = listOf("Nokia", "BlackBerry", "Apple", "Samsung"), correctAnswerIndex = 2),
        Question(text = "Android, the mobile operating system, was primarily developed by:", options = listOf("Microsoft", "Apple", "Google", "Nokia"), correctAnswerIndex = 2),
        Question(text = "App stores revolutionized software distribution by:", options = listOf("Making all software free", "Allowing direct downloads to devices", "Requiring physical media for installation", "Limiting software to a few developers"), correctAnswerIndex = 1),
        Question(text = "Smartphones have NOT significantly impacted which of the following?", options = listOf("Communication", "Photography", "Navigation", "Book printing methods"), correctAnswerIndex = 3)
    )

    // Level 12: Social Media Age (2004-Present)
    private val socialMediaAgeQuestions = listOf(
        Question(text = "Facebook was founded by Mark Zuckerberg in:", options = listOf("2000", "2004", "2008", "2010"), correctAnswerIndex = 1),
        Question(text = "Twitter, known for its short character limit messages (originally), was launched in:", options = listOf("2003", "2006", "2009", "2012"), correctAnswerIndex = 1),
        Question(text = "Which platform is primarily known for professional networking?", options = listOf("Instagram", "TikTok", "LinkedIn", "Snapchat"), correctAnswerIndex = 2),
        Question(text = "The rise of social media has been linked to:", options = listOf("Decreased political polarization", "Increased spread of misinformation ('fake news')", "Less time spent online", "Improved attention spans"), correctAnswerIndex = 1)
    )

    // Level 13: Pandemic World (2020-Present)
    private val pandemicWorldQuestions = listOf(
        Question(text = "The COVID-19 pandemic was first identified in which city?", options = listOf("Wuhan, China", "Milan, Italy", "New York, USA", "London, UK"), correctAnswerIndex = 0),
        Question(text = "mRNA vaccines were a significant new technology used against COVID-19. What does mRNA stand for?", options = listOf("Micro RNA", "Messenger RNA", "Modified RNA", "Major RNA"), correctAnswerIndex = 1),
        Question(text = "Which of these was a major global impact of the COVID-19 pandemic?", options = listOf("Increased international travel", "Strengthened global supply chains", "Widespread adoption of remote work", "Reduced use of digital technology"), correctAnswerIndex = 2),
        Question(text = "The term 'long COVID' refers to:", options = listOf("A very severe initial infection", "Symptoms persisting for weeks or months after initial infection", "The economic impact of the pandemic", "The period of lockdown"), correctAnswerIndex = 1)
    )

    // Level 14: Green Energy Shift (2010s-Present)
    private val greenEnergyShiftQuestions = listOf(
        Question(text = "Which renewable energy source has seen the most significant cost reduction and adoption globally in the last decade?", options = listOf("Geothermal", "Hydropower", "Solar photovoltaic", "Nuclear fusion"), correctAnswerIndex = 2),
        Question(text = "Electric vehicles (EVs) are seen as a key part of the green energy shift. Which company popularized long-range EVs?", options = listOf("Toyota", "General Motors", "Tesla", "Volkswagen"), correctAnswerIndex = 2),
        Question(text = "The concept of a 'smart grid' primarily involves:", options = listOf("Building more power plants", "Using digital technology to optimize energy distribution", "Reducing overall energy consumption", "Exporting energy to other countries"), correctAnswerIndex = 1),
        Question(text = "What is a major challenge for the widespread adoption of renewable energy sources like wind and solar?", options = listOf("They produce too much energy", "Their intermittent nature (not always available)", "They are more polluting than fossil fuels", "They are too expensive to maintain"), correctAnswerIndex = 1)
    )

    // Level 15: AI Revolution (2010s-Present)
    private val aiRevolutionQuestions = listOf(
        Question(text = "Deep learning, a subset of machine learning, is inspired by:", options = listOf("Animal behavior", "The structure and function of the human brain", "Quantum physics", "Mathematical logic"), correctAnswerIndex = 1),
        Question(text = "Generative AI, like ChatGPT, is primarily designed to:", options = listOf("Control robots", "Analyze large datasets", "Create new content (text, images, etc.)", "Perform complex calculations"), correctAnswerIndex = 2),
        Question(text = "Ethical concerns regarding AI include:", options = listOf("Job displacement due to automation", "Bias in AI algorithms", "Potential for misuse in surveillance", "All of the above"), correctAnswerIndex = 3),
        Question(text = "The 'Turing Test' is a test of a machine's ability to:", options = listOf("Perform complex calculations", "Exhibit intelligent behavior equivalent to, or indistinguishable from, that of a human", "Win a game of chess", "Translate languages accurately"), correctAnswerIndex = 1)
    )

    // Level 16: Quantum Computing (2020s-Future)
    private val quantumQuestions = listOf(
        Question(text = "Which company claimed to achieve 'quantum supremacy' in 2019?", options = listOf("IBM", "Google", "Intel", "Microsoft"), correctAnswerIndex = 1),
        Question(text = "A qubit, the basic unit of quantum information, can represent:", options = listOf("Only 0 or 1", "0, 1, or both simultaneously (superposition)", "Only shades of gray", "A continuous range of values"), correctAnswerIndex = 1),
        Question(text = "Quantum computers are expected to be particularly good at solving problems related to:", options = listOf("Playing video games", "Drug discovery and material science", "Writing news articles", "Social media content moderation"), correctAnswerIndex = 1),
        Question(text = "One of the biggest challenges in building practical quantum computers is:", options = listOf("The cost of materials", "Maintaining qubit coherence (preventing errors)", "Lack of programming languages", "Slow processing speed"), correctAnswerIndex = 1)
    )

    // Level 17: Mars Exploration (2030s-Future)
    private val marsQuestions = listOf(
        Question(text = "Which private company has publicly stated ambitions to colonize Mars?", options = listOf("Blue Origin", "Virgin Galactic", "SpaceX", "Boeing"), correctAnswerIndex = 2),
        Question(text = "A significant challenge for long-term human presence on Mars is:", options = listOf("Lack of interesting scenery", "High levels of solar and cosmic radiation", "Too much oxygen in the atmosphere", "Extremely hot temperatures"), correctAnswerIndex = 1),
        Question(text = "Terraforming Mars refers to the hypothetical process of:", options = listOf("Building underground habitats", "Modifying its atmosphere, temperature, and ecology to be similar to Earth's", "Transporting resources from Mars to Earth", "Establishing a communication network with Mars"), correctAnswerIndex = 1),
        Question(text = "NASA's Artemis program, while focused on the Moon, is also seen as a stepping stone for future missions to:", options = listOf("Venus", "Jupiter", "Mars", "Saturn"), correctAnswerIndex = 2)
    )

    // Level 18: Post-Capitalism Concepts (2030s-Future)
    private val postCapitalismQuestions = listOf(
        Question(text = "Universal Basic Income (UBI) is a concept where:", options = listOf("Only the unemployed receive government payments", "All citizens receive a regular, unconditional sum of money from the government", "Corporations pay a basic wage to all citizens", "Healthcare and education are fully nationalized"), correctAnswerIndex = 1),
        Question(text = "The 'gig economy' refers to a labor market characterized by:", options = listOf("Lifelong employment with a single company", "Prevalence of short-term contracts or freelance work", "Government-assigned jobs", "Exclusively high-tech jobs"), correctAnswerIndex = 1),
        Question(text = "Decentralized Autonomous Organizations (DAOs) are often associated with which technology?", options = listOf("Artificial Intelligence", "Quantum Computing", "Blockchain", "Virtual Reality"), correctAnswerIndex = 2),
        Question(text = "A potential driver for post-capitalist economic models is:", options = listOf("Increased automation leading to widespread job displacement", "A return to agrarian societies", "The discovery of unlimited free energy", "A global ban on international trade"), correctAnswerIndex = 0)
    )

    // Level 19: Transhumanism & Biohacking (2040s-Future)
    private val transhumanismQuestions = listOf(
        Question(text = "Transhumanism is a movement that advocates for:", options = listOf("Returning to a simpler, pre-technological lifestyle", "The use of technology to enhance human intellectual, physical, and psychological capacities", "Strict limits on technological development", "Focusing solely on environmental preservation"), correctAnswerIndex = 1),
        Question(text = "CRISPR-Cas9 is a technology primarily associated with:", options = listOf("Brain-computer interfaces", "Gene editing", "Artificial general intelligence", "Nanotechnology"), correctAnswerIndex = 1),
        Question(text = "Brain-Computer Interfaces (BCIs) aim to:", options = listOf("Create realistic virtual worlds", "Allow direct communication pathways between a brain and an external device", "Predict future events", "Enhance agricultural output"), correctAnswerIndex = 1),
        Question(text = "Ethical concerns about transhumanist technologies often revolve around:", options = listOf("Accessibility and social inequality (the 'bio-divide')", "The potential for new forms of art", "Increased efficiency in manufacturing", "Simpler medical diagnoses"), correctAnswerIndex = 0)
    )

    // Level 20: Future Scenarios & Existential Risks (2050+-Future)
    private val futureQuestions = listOf(
        Question(text = "The 'Technological Singularity' is a hypothetical point in time when:", options = listOf("All diseases are cured", "Technological growth becomes uncontrollable and irreversible, resulting in unforeseeable changes to human civilization", "Humans establish a permanent colony on Mars", "Global peace is achieved"), correctAnswerIndex = 1),
        Question(text = "An 'existential risk' is defined as a risk that threatens:", options = listOf("The economy of a single country", "A specific technological system", "The long-term survival or potential of humanity", "The outcome of a political election"), correctAnswerIndex = 2),
        Question(text = "The Kardashev scale is used to measure:", options = listOf("The speed of light in different mediums", "A civilization's level of technological advancement based on its energy consumption", "The likelihood of alien contact", "The stability of planetary orbits"), correctAnswerIndex = 1),
        Question(text = "Which of these is often discussed as a potential existential risk associated with advanced AI?", options = listOf("AI taking over customer service jobs", "The AI alignment problem (ensuring AI goals align with human values)", "AI creating too much art", "AI making scientific discoveries too quickly"), correctAnswerIndex = 1)
    )

    val questionsByLevel = mapOf(
        "1" to industrialRevolutionQuestions,
        "2" to steamPowerEraQuestions,
        "3" to worldWarsQuestions,
        "4" to coldWarBeginsQuestions,
        "5" to spaceRaceQuestions,
        "6" to digitalDawnQuestions,
        "7" to globalizationQuestions,
        "8" to internetRevolutionQuestions,
        "9" to climateAwarenessQuestions,
        "10" to warOnTerrorQuestions,
        "11" to smartphoneEraQuestions,
        "12" to socialMediaAgeQuestions,
        "13" to pandemicWorldQuestions,
        "14" to greenEnergyShiftQuestions,
        "15" to aiRevolutionQuestions,
        "16" to quantumQuestions,
        "17" to marsQuestions,
        "18" to postCapitalismQuestions,
        "19" to transhumanismQuestions,
        "20" to futureQuestions
    )

    fun getQuestionsByLevel(levelId: String): List<Question> {
        return questionsByLevel[levelId] ?: emptyList()
    }

    fun getRandomQuestions(count: Int = 4): List<Question> { // Fallback
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