package com.hikmet.imperium.data.content

import com.hikmet.imperium.data.Question

internal object MedievalSupplementalQuestions {
    val byLevel: Map<Int, List<Question>> = mapOf(
        1 to listOf(
            supplementalQuestion("Which Frankish king converted to Christianity around the end of the fifth century?", "Clovis I", "Theodoric", "Alfred the Great", "Otto I"),
            supplementalQuestion("What was the economic center of a typical medieval manor?", "The lord's estate and its dependent farms", "A royal mint", "A cathedral school", "A seaport"),
            supplementalQuestion("Which institutions preserved and copied many manuscripts in early medieval Europe?", "Monasteries", "Tournament grounds", "Merchant guilds", "Royal forests"),
            supplementalQuestion("Which English king is remembered for resisting Viking armies and promoting learning?", "Alfred the Great", "Cnut", "Harold Godwinson", "Richard I"),
        ),
        2 to listOf(
            supplementalQuestion("Which city was captured by crusader forces in 1099?", "Jerusalem", "Cairo", "Damascus", "Alexandria"),
            supplementalQuestion("What were the states founded by western crusaders in the eastern Mediterranean called?", "The Crusader states", "The Papal States", "The Hanseatic towns", "The Lombard communes"),
            supplementalQuestion("Which English king participated in the Third Crusade?", "Richard I", "John", "Henry VII", "Edward III"),
            supplementalQuestion("Which Christian city was sacked during the Fourth Crusade in 1204?", "Constantinople", "Rome", "Venice", "Antioch"),
        ),
        3 to listOf(
            supplementalQuestion("What ceremony formally established the bond between a vassal and a lord?", "Homage", "Coronation", "Ordination", "Pilgrimage"),
            supplementalQuestion("What did a medieval apprentice primarily do?", "Learn a craft from a master", "Collect royal taxes", "Command a castle garrison", "Copy laws for a court"),
            supplementalQuestion("What privilege could a town charter grant?", "Rights of self-government and trade", "Control of every nearby manor", "Automatic noble status for all residents", "Freedom from all laws"),
            supplementalQuestion("What was demesne land on a manor?", "Land reserved for the lord", "Common pasture owned by a guild", "Land belonging to a monastery school", "A royal hunting road"),
        ),
        4 to listOf(
            supplementalQuestion("Which bacterium causes plague?", "Yersinia pestis", "Vibrio cholerae", "Bacillus anthracis", "Mycobacterium tuberculosis"),
            supplementalQuestion("Which port city introduced a thirty-day isolation rule in 1377 that helped inspire quarantine?", "Ragusa", "London", "Bruges", "Lisbon"),
            supplementalQuestion("Why did wages often rise after the Black Death?", "Labor became scarce", "Coinage disappeared", "Harvests doubled", "Guilds were abolished"),
            supplementalQuestion("Which trade routes helped plague spread westward from Asia?", "Overland and maritime commercial routes", "Only Atlantic whaling routes", "Only trans-Saharan caravan routes", "Only Baltic fishing routes"),
        ),
        5 to listOf(
            supplementalQuestion("At which 1346 battle did English longbowmen help defeat a French army?", "Crécy", "Bouvines", "Hastings", "Tours"),
            supplementalQuestion("Which 1415 English victory occurred during Henry V's campaign in France?", "Agincourt", "Poitiers", "Bosworth", "Bannockburn"),
            supplementalQuestion("Which city did Joan of Arc help relieve from siege in 1429?", "Orléans", "Calais", "Rouen", "Paris"),
            supplementalQuestion("Which weapon became increasingly important in sieges near the end of the Hundred Years' War?", "Gunpowder cannon", "Bronze-age chariots", "Greek fire siphons", "Composite bows alone"),
        ),
        6 to listOf(
            supplementalQuestion("Which innovation allowed horses to pull heavy loads without choking?", "The horse collar", "The stirrup", "The astrolabe", "The drawbridge"),
            supplementalQuestion("What was the main benefit of the three-field system?", "More land could remain productive each year", "It eliminated the need for animals", "It made irrigation unnecessary", "It replaced grain with cotton"),
            supplementalQuestion("Which optical device appeared in Europe during the late thirteenth century?", "Eyeglasses", "The telescope", "The microscope", "The camera obscura"),
            supplementalQuestion("What natural force powered medieval windmills?", "Moving air", "Steam pressure", "Tidal heat", "Magnetism"),
        ),
        7 to listOf(
            supplementalQuestion("Which reform movement began at a Burgundian monastery founded in 910?", "The Cluniac reform", "The Great Schism", "The Investiture movement", "The Lollard revolt"),
            supplementalQuestion("Which two orders are prominent examples of medieval mendicant friars?", "Franciscans and Dominicans", "Templars and Hospitallers", "Benedictines and Cistercians", "Jesuits and Oratorians"),
            supplementalQuestion("What did excommunication exclude a person from?", "Communion with the Church", "Membership in a craft guild", "Service in a royal army", "Residence inside a town wall"),
            supplementalQuestion("What body of rules governed the medieval Western Church?", "Canon law", "Feudal custom alone", "The Twelve Tables", "Maritime law"),
        ),
        8 to listOf(
            supplementalQuestion("Which type of arch is characteristic of Romanesque architecture?", "A rounded arch", "A pointed arch", "A horseshoe arch only", "An ogee dome"),
            supplementalQuestion("Which structural support allowed Gothic churches to have taller walls and larger windows?", "The flying buttress", "The triumphal arch", "The ziggurat platform", "The mastaba wall"),
            supplementalQuestion("What was a fresco?", "A painting made on wet plaster", "A carving on an ivory diptych", "A woven wall hanging", "A stained-glass technique"),
            supplementalQuestion("What practical purpose did many carved gargoyles serve?", "They carried rainwater away from walls", "They supported church bells", "They stored holy water", "They marked burial places"),
        ),
        9 to listOf(
            supplementalQuestion("Which French commercial gatherings linked merchants from northern and Mediterranean Europe?", "The Champagne fairs", "The Field of the Cloth of Gold", "The Estates-General", "The Truce of God"),
            supplementalQuestion("Which city became a leading center of the Hanseatic League?", "Lübeck", "Toledo", "Naples", "Avignon"),
            supplementalQuestion("Which gold coin first minted by Florence became widely used in European trade?", "The florin", "The denarius", "The solidus", "The groat"),
            supplementalQuestion("Why did merchants use bills of exchange?", "To transfer value without carrying large amounts of coin", "To abolish tolls permanently", "To recruit feudal armies", "To set church doctrine"),
        ),
        10 to listOf(
            supplementalQuestion("What was the Western Schism of 1378–1417?", "A period with rival claimants to the papacy", "The division of the Roman Empire", "The Norman conquest of England", "The separation of guilds from towns"),
            supplementalQuestion("What name is given to the French peasant revolt of 1358?", "The Jacquerie", "The Fronde", "The Commune", "The Hussite War"),
            supplementalQuestion("Which council ended the Western Schism by resolving the competing papal claims?", "The Council of Constance", "The Council of Nicaea", "The Council of Trent", "The Council of Clermont"),
            supplementalQuestion("Which city fell to Mehmed II in 1453?", "Constantinople", "Vienna", "Rome", "Belgrade"),
        ),
        11 to listOf(
            supplementalQuestion("Which military government began in Japan in 1192?", "The Kamakura shogunate", "The Tokugawa shogunate", "The Meiji government", "The Heian regency"),
            supplementalQuestion("What was a daimyo?", "A powerful regional lord", "A Buddhist monk", "A court poet", "A village artisan"),
            supplementalQuestion("Which curved sword became closely associated with the samurai?", "The katana", "The gladius", "The rapier", "The scimitar"),
            supplementalQuestion("Which indigenous religious tradition coexisted with Buddhism in medieval Japan?", "Shinto", "Zoroastrianism", "Jainism", "Manichaeism"),
        ),
        12 to listOf(
            supplementalQuestion("Which language was dominant in the administration and culture of the medieval Byzantine Empire?", "Greek", "Latin", "Arabic", "Old Church Slavonic"),
            supplementalQuestion("What was the Corpus Juris Civilis?", "A compilation of Roman law under Justinian", "A crusader military order", "A Byzantine tax register", "A collection of monastic hymns"),
            supplementalQuestion("What issue was central to Byzantine Iconoclasm?", "The religious use of sacred images", "The use of gunpowder", "The election of consuls", "The language of trade contracts"),
            supplementalQuestion("Which crusade captured and occupied Constantinople?", "The Fourth Crusade", "The First Crusade", "The Second Crusade", "The Third Crusade"),
        ),
        13 to listOf(
            supplementalQuestion("Which Abbasid institution in Baghdad became famous for scholarship and translation?", "The House of Wisdom", "The Academy of Athens", "The Library of Pergamum", "The Imperial College of London"),
            supplementalQuestion("Which scholar's Book of Optics emphasized experiment and the study of vision?", "Ibn al-Haytham", "Al-Farabi", "Al-Ghazali", "Ibn Khaldun"),
            supplementalQuestion("Which instrument helped medieval astronomers determine the positions of celestial objects?", "The astrolabe", "The sextant clock", "The barometer", "The steam turbine"),
            supplementalQuestion("The medieval Arabic translation movement preserved and expanded works from which earlier traditions?", "Greek, Persian, and Indian traditions", "Only Roman military manuals", "Only Norse sagas", "Only Chinese legal codes"),
        ),
        14 to listOf(
            supplementalQuestion("Which writing system appears on many Viking Age memorial stones?", "Runes", "Cuneiform", "Hieroglyphs", "Linear B"),
            supplementalQuestion("What was the Danelaw?", "An area of England under Scandinavian law and influence", "A Viking assembly in Iceland", "A royal tax on ships", "A treaty between Byzantium and Venice"),
            supplementalQuestion("Which Norwegian king was defeated at Stamford Bridge in 1066?", "Harald Hardrada", "Olaf Tryggvason", "Haakon IV", "Magnus the Good"),
            supplementalQuestion("Which explorer established a Norse settlement in Greenland?", "Erik the Red", "Leif Erikson", "Rollo", "Sweyn Forkbeard"),
        ),
        15 to listOf(
            supplementalQuestion("Which dynasty did Kublai Khan establish in China?", "The Yuan dynasty", "The Ming dynasty", "The Song dynasty", "The Qing dynasty"),
            supplementalQuestion("What does Pax Mongolica describe?", "A period of relative security across Mongol-controlled trade routes", "A Mongol ban on foreign commerce", "A peace treaty with the Roman Empire", "The division of Japan into domains"),
            supplementalQuestion("Which policy helped the Mongols govern a religiously diverse empire?", "Broad religious tolerance", "Mandatory conversion to one faith", "A ban on all clergy", "Destruction of every shrine"),
            supplementalQuestion("Which communication network carried official messages across the Mongol Empire?", "The Yam relay system", "The cursus honorum", "The Hanseatic route", "The Silk Guild"),
        ),
        16 to listOf(
            supplementalQuestion("Which counterweight-powered engine hurled projectiles at fortifications?", "The trebuchet", "The ballista", "The battering ram", "The mangonel"),
            supplementalQuestion("What was the purpose of a concentric castle design?", "To create multiple rings of defense", "To house merchant guilds", "To improve cathedral acoustics", "To irrigate farmland"),
            supplementalQuestion("Which ranged weapon could be used effectively with less training than a longbow?", "The crossbow", "The sling", "The javelin", "The composite spear"),
            supplementalQuestion("Which infantry formation used long pole weapons to resist cavalry?", "A pike formation", "A shieldless skirmish line", "A chariot circle", "A naval phalanx"),
        ),
        17 to listOf(
            supplementalQuestion("Which Italian city housed one of medieval Europe's best-known medical schools?", "Salerno", "Pisa", "Milan", "Genoa"),
            supplementalQuestion("What did an apothecary prepare and sell?", "Medicinal substances", "Illuminated manuscripts", "Weapons and armor", "Legal charters"),
            supplementalQuestion("Which institutions often provided care for the sick and travelers?", "Monasteries and religious hospitals", "Tournament companies", "Tax courts", "Merchant fleets"),
            supplementalQuestion("What did a medieval regimen sanitatis provide?", "Advice about preserving health", "Rules for siege warfare", "Instructions for glassmaking", "A list of feudal taxes"),
        ),
        18 to listOf(
            supplementalQuestion("What is organum in medieval music?", "An early form of polyphony", "A type of drum", "A dance for solo flute", "A spoken legal formula"),
            supplementalQuestion("Which abbess composed sacred music and wrote visionary works in the twelfth century?", "Hildegard of Bingen", "Christine de Pizan", "Héloïse", "Anna Komnene"),
            supplementalQuestion("Which instrument was a bowed ancestor of later violins?", "The vielle", "The sackbut", "The psaltery", "The shawm"),
            supplementalQuestion("Which theorist is traditionally linked with teaching solmization syllables and staff notation?", "Guido of Arezzo", "Thomas Aquinas", "Geoffrey Chaucer", "Peter Abelard"),
        ),
        19 to listOf(
            supplementalQuestion("Which epic poem celebrates the rearguard of Charlemagne's army?", "The Song of Roland", "The Nibelungenlied", "The Canterbury Tales", "The Romance of the Rose"),
            supplementalQuestion("Which author wrote romances about knights of King Arthur's court?", "Chrétien de Troyes", "Boccaccio", "Dante Alighieri", "William Langland"),
            supplementalQuestion("What does vernacular literature use?", "The everyday language of a region", "Only classical Greek", "Only church music notation", "A secret guild cipher"),
            supplementalQuestion("Where were many medieval books copied by hand?", "In monastic scriptoria", "In royal mints", "In castle armories", "In market tollhouses"),
        ),
        20 to listOf(
            supplementalQuestion("Which cultural movement emerged first in Italian cities near the end of the Middle Ages?", "The Renaissance", "The Enlightenment", "Romanticism", "Modernism"),
            supplementalQuestion("How did gunpowder weapons weaken many castles?", "Cannon could break traditional stone walls", "Gunpowder stopped metal from rusting", "Firearms made roads unnecessary", "Cannon replaced all naval vessels"),
            supplementalQuestion("Which development strengthened several late-medieval monarchies?", "More centralized taxation and administration", "The disappearance of written law", "The abolition of coinage", "The end of all diplomacy"),
            supplementalQuestion("Why were expanding towns important to the transition out of the medieval economy?", "They increased commerce and specialized labor", "They eliminated long-distance trade", "They restored subsistence farming only", "They ended the use of money"),
        ),
    )
}
