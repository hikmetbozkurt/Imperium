package com.hikmet.imperium.data.content

import com.hikmet.imperium.data.Question

internal object ModernSupplementalQuestions {
    val byLevel: Map<Int, List<Question>> = mapOf(
        1 to listOf(
            supplementalQuestion("Which inventor is closely associated with improvements to the steam engine in the eighteenth century?", "James Watt", "Michael Faraday", "Samuel Morse", "Guglielmo Marconi"),
            supplementalQuestion("Which machine invented by James Hargreaves accelerated spinning in textile production?", "The spinning jenny", "The cotton gin", "The power loom", "The sewing machine"),
            supplementalQuestion("What demographic change accompanied industrialization?", "Rapid urban growth", "The abandonment of all cities", "A return to nomadic life", "The disappearance of wage labor"),
            supplementalQuestion("What defined the factory system?", "Workers and powered machinery were concentrated in one workplace", "Every product was made by isolated farmers", "Production depended only on guild masters", "Machines were prohibited by law"),
        ),
        2 to listOf(
            supplementalQuestion("Which alliance included Germany, Austria-Hungary, and Italy before World War I?", "The Triple Alliance", "The Triple Entente", "The Warsaw Pact", "The Axis alliance"),
            supplementalQuestion("Which empire dissolved at the end of World War I?", "Austria-Hungary", "The British Empire", "The Japanese Empire", "The Portuguese Empire"),
            supplementalQuestion("Which 1939 invasion triggered Britain and France to declare war on Germany?", "The invasion of Poland", "The invasion of Norway", "The invasion of Greece", "The invasion of the Soviet Union"),
            supplementalQuestion("Which wartime alliance brought together Britain, the Soviet Union, and the United States?", "The Grand Alliance", "The Central Powers", "The Triple Alliance", "The Warsaw Pact"),
        ),
        3 to listOf(
            supplementalQuestion("What was D-Day?", "The Allied landings in Normandy on 6 June 1944", "Germany's invasion of Poland", "Japan's attack on Pearl Harbor", "The Soviet entry into Berlin"),
            supplementalQuestion("Which US program introduced federal relief, recovery, and reform during the Great Depression?", "The New Deal", "The Great Society", "The Square Deal", "The New Frontier"),
            supplementalQuestion("Which 1948–49 operation supplied West Berlin by air?", "The Berlin Airlift", "Operation Torch", "The Marshall Mission", "The Potsdam Airlift"),
            supplementalQuestion("Which alliance was formed in 1955 by the Soviet Union and its Eastern European partners?", "The Warsaw Pact", "NATO", "SEATO", "CENTO"),
        ),
        4 to listOf(
            supplementalQuestion("Which leader read Indonesia's proclamation of independence alongside Mohammad Hatta in August 1945?", "Sukarno", "Suharto", "Sutan Sjahrir", "Agus Salim"),
            supplementalQuestion("Which 1956 uprising was suppressed by Soviet forces in Budapest?", "The Hungarian Revolution", "The Prague Spring", "The Velvet Revolution", "The Warsaw Uprising"),
            supplementalQuestion("What naval measure did the United States announce during the Cuban Missile Crisis?", "A quarantine of Cuba", "A blockade of West Berlin", "A closure of the Suez Canal", "An invasion of East Germany"),
            supplementalQuestion("Which 1968 reform movement in Czechoslovakia was ended by a Warsaw Pact invasion?", "The Prague Spring", "The Hungarian Revolution", "The Velvet Revolution", "The Solidarity movement"),
        ),
        5 to listOf(
            supplementalQuestion("Which British prime minister succeeded Winston Churchill in July 1945?", "Clement Attlee", "Anthony Eden", "Harold Macmillan", "Neville Chamberlain"),
            supplementalQuestion("Which 1944 conference designed the postwar monetary system and its key institutions?", "The Bretton Woods Conference", "The Bandung Conference", "The Geneva Conference", "The Tehran Conference"),
            supplementalQuestion("Which Soviet-led organization coordinated economic cooperation in the Eastern Bloc?", "Comecon", "The OECD", "The EEC", "The IMF"),
            supplementalQuestion("Which 1961 invasion attempt sought to overthrow Cuba's government?", "The Bay of Pigs invasion", "Operation Market Garden", "The Suez invasion", "The Prague intervention"),
        ),
        6 to listOf(
            supplementalQuestion("Which document became Japan's postwar constitution in 1947?", "The Constitution of Japan", "The Meiji Charter Oath", "The San Francisco Declaration", "The Potsdam Constitution"),
            supplementalQuestion("Which 1948 declaration set out a global standard of fundamental rights?", "The Universal Declaration of Human Rights", "The Atlantic Charter", "The Helsinki Final Act", "The European Social Charter"),
            supplementalQuestion("Which two financial institutions emerged from the Bretton Woods system?", "The IMF and World Bank", "NATO and the Warsaw Pact", "The UN and UNESCO", "The EEC and EFTA"),
            supplementalQuestion("Which 1947 plan proposed the partition of Palestine into Arab and Jewish states?", "UN General Assembly Resolution 181", "The Balfour Declaration", "The Sykes–Picot Agreement", "The Camp David Accords"),
        ),
        7 to listOf(
            supplementalQuestion("Which conference of Asian and African states met in Indonesia in 1955?", "The Bandung Conference", "The Yalta Conference", "The Bretton Woods Conference", "The Tehran Conference"),
            supplementalQuestion("Who became Kenya's first prime minister at independence in 1963?", "Jomo Kenyatta", "Kwame Nkrumah", "Julius Nyerere", "Patrice Lumumba"),
            supplementalQuestion("Which agreements brought the Algerian War to an end in 1962?", "The Évian Accords", "The Camp David Accords", "The Oslo Accords", "The Lomé Convention"),
            supplementalQuestion("Which 1956 crisis weakened British and French imperial influence in the Middle East?", "The Suez Crisis", "The Berlin Crisis", "The Congo Crisis", "The Agadir Crisis"),
        ),
        8 to listOf(
            supplementalQuestion("Which 1963 event included Martin Luther King Jr.'s I Have a Dream speech?", "The March on Washington", "The Selma to Montgomery march", "The Montgomery bus boycott", "The March Against Fear"),
            supplementalQuestion("Which US law prohibited racial discrimination in voting practices in 1965?", "The Voting Rights Act", "The Civil Rights Act of 1957", "The Fair Housing Act", "The Social Security Act"),
            supplementalQuestion("What was Checkpoint Charlie?", "A crossing point between East and West Berlin", "A launch site in Kazakhstan", "A US naval base in Cuba", "A border post between the Koreas"),
            supplementalQuestion("Who were the Red Guards during China's Cultural Revolution?", "Militant student groups mobilized in Mao's campaign", "Foreign UN peacekeepers", "Imperial palace guards", "Rural tax collectors"),
        ),
        9 to listOf(
            supplementalQuestion("Which 1951 organization pooled French, West German, Italian, and Benelux coal and steel production?", "The European Coal and Steel Community", "The European Free Trade Association", "The Council for Mutual Economic Assistance", "The Organization for European Economic Cooperation"),
            supplementalQuestion("Which 1962 satellite relayed live television signals across the Atlantic?", "Telstar 1", "Sputnik 1", "Explorer 1", "Luna 2"),
            supplementalQuestion("Which US satellite became the country's first successful Earth satellite in 1958?", "Explorer 1", "Vanguard TV3", "Telstar 1", "Mariner 2"),
            supplementalQuestion("Which electronic component helped make radios and computers smaller after World War II?", "The transistor", "The steam valve", "The telegraph key", "The vacuum pump"),
        ),
        10 to listOf(
            supplementalQuestion("Which 1975 mission linked American and Soviet spacecraft in orbit?", "The Apollo–Soyuz Test Project", "Skylab 1", "Vostok 6", "Apollo 13"),
            supplementalQuestion("Which 1973 agreement led to the withdrawal of US combat forces from Vietnam?", "The Paris Peace Accords", "The Geneva Accords of 1954", "The Helsinki Final Act", "The Camp David Accords"),
            supplementalQuestion("Which 1975 agreement addressed European security and human rights during détente?", "The Helsinki Final Act", "The Treaty of Rome", "The Schengen Agreement", "The Atlantic Charter"),
            supplementalQuestion("Which US president pardoned Richard Nixon?", "Gerald Ford", "Jimmy Carter", "Lyndon Johnson", "Ronald Reagan"),
        ),
        11 to listOf(
            supplementalQuestion("Which 1947 doctrine presented the world as divided between opposing political systems?", "The Zhdanov Doctrine", "The Brezhnev Doctrine", "The Hallstein Doctrine", "The Nixon Doctrine"),
            supplementalQuestion("Which 1948 event was the first major international crisis over access to West Berlin?", "The Berlin Blockade", "The building of the Berlin Wall", "The Prague Spring", "The Hungarian Revolution"),
            supplementalQuestion("What was Cominform?", "A Soviet-led organization coordinating communist parties", "A Western European trade union", "A United Nations relief agency", "A nuclear arms treaty"),
            supplementalQuestion("Which conflict shaped Margaret Thatcher's government in 1982?", "The Falklands War", "The Korean War", "The Suez Crisis", "The Gulf War"),
        ),
        12 to listOf(
            supplementalQuestion("Which 1987 treaty eliminated an entire class of US and Soviet land-based missiles?", "The INF Treaty", "SALT I", "The Nuclear Non-Proliferation Treaty", "The Partial Test Ban Treaty"),
            supplementalQuestion("Which 1989 human chain crossed Estonia, Latvia, and Lithuania?", "The Baltic Way", "The Velvet Chain", "The Solidarity March", "The Helsinki Line"),
            supplementalQuestion("Which 1986 summit in Iceland brought Ronald Reagan and Mikhail Gorbachev close to a broad arms agreement?", "The Reykjavík Summit", "The Malta Summit", "The Geneva Summit of 1955", "The Helsinki Summit"),
            supplementalQuestion("Which city built to serve Chernobyl plant workers was evacuated after the 1986 disaster?", "Pripyat", "Minsk", "Kyiv", "Odessa"),
        ),
        13 to listOf(
            supplementalQuestion("Which currency entered electronic use in 1999 and cash circulation in 2002?", "The euro", "The ecu", "The pound sterling", "The franc CFA"),
            supplementalQuestion("Which organization succeeded much of the Soviet Union as a loose association in 1991?", "The Commonwealth of Independent States", "The Warsaw Pact", "The Council of Europe", "The Eurasian Coal Community"),
            supplementalQuestion("Which 1985 agreement sought to correct major currency imbalances by lowering the value of the US dollar?", "The Plaza Accord", "The Louvre Treaty", "The Bretton Woods Act", "The Schengen Agreement"),
            supplementalQuestion("Which coalition expelled Iraqi forces from Kuwait in 1991?", "A UN-authorized multinational coalition", "The Warsaw Pact", "The Non-Aligned Movement", "The Arab League alone"),
        ),
        14 to listOf(
            supplementalQuestion("Which Chinese city became an early Special Economic Zone under Deng Xiaoping?", "Shenzhen", "Beijing", "Xi'an", "Harbin"),
            supplementalQuestion("Which peaceful 1989 movement ended communist rule in Czechoslovakia?", "The Velvet Revolution", "The Rose Revolution", "The Orange Revolution", "The Carnation Revolution"),
            supplementalQuestion("Which body investigated apartheid-era abuses in South Africa?", "The Truth and Reconciliation Commission", "The Rivonia Council", "The African Union Court", "The Commonwealth Tribunal"),
            supplementalQuestion("Which 1990 agreement settled the international aspects of German reunification?", "The Two Plus Four Treaty", "The Treaty of Rome", "The Élysée Treaty", "The Warsaw Treaty of 1955"),
        ),
        15 to listOf(
            supplementalQuestion("Which organization carried out the September 11 attacks?", "Al-Qaeda", "The Taliban government", "The IRA", "The Red Brigades"),
            supplementalQuestion("Which country did a US-led coalition invade in late 2001 after the September 11 attacks?", "Afghanistan", "Iraq", "Libya", "Syria"),
            supplementalQuestion("Which treaty reformed the European Union and entered into force in 2009?", "The Treaty of Lisbon", "The Treaty of Versailles", "The Treaty of Nice of 1892", "The Treaty of Brussels of 1948"),
            supplementalQuestion("Which organization classified COVID-19 as a Public Health Emergency of International Concern in January 2020?", "The World Health Organization", "The World Trade Organization", "The International Monetary Fund", "The Council of Europe"),
        ),
        16 to listOf(
            supplementalQuestion("Which forum became a central meeting place for leaders of major advanced and emerging economies after 2008?", "The G20", "The G7 alone", "The OECD", "The Council of Europe"),
            supplementalQuestion("Which continental organization replaced the Organization of African Unity in 2002?", "The African Union", "The African Development Bank", "The Arab League", "ECOWAS"),
            supplementalQuestion("Which court began operating in The Hague in 2002 under the Rome Statute?", "The International Criminal Court", "The International Court of Justice", "The European Court of Human Rights", "The Permanent Court of Arbitration"),
            supplementalQuestion("Which group originally linked Brazil, Russia, India, China, and later South Africa?", "BRICS", "ASEAN", "NAFTA", "OPEC"),
        ),
        17 to listOf(
            supplementalQuestion("Who invented the World Wide Web while working at CERN?", "Tim Berners-Lee", "Vint Cerf", "Steve Jobs", "Linus Torvalds"),
            supplementalQuestion("Which transport innovation sharply lowered the cost of moving goods across oceans?", "Standardized shipping containers", "Supersonic passenger aircraft", "Nuclear-powered freight trains", "Airships"),
            supplementalQuestion("Which organization began operating in 1995 as the successor to GATT?", "The World Trade Organization", "The International Monetary Fund", "The World Bank", "The OECD"),
            supplementalQuestion("What is a global supply chain?", "A production network spread across multiple countries", "A single country's military logistics system", "A fixed exchange-rate treaty", "A social media contact list"),
        ),
        18 to listOf(
            supplementalQuestion("Which two UN bodies established the Intergovernmental Panel on Climate Change in 1988?", "The WMO and UNEP", "The WHO and WTO", "UNESCO and UNICEF", "The IMF and World Bank"),
            supplementalQuestion("In which country did the Arab Spring begin in late 2010?", "Tunisia", "Egypt", "Libya", "Syria"),
            supplementalQuestion("Which NASA rover landed in Gale Crater in 2012?", "Curiosity", "Spirit", "Opportunity", "Sojourner"),
            supplementalQuestion("Which 1987 agreement targeted chemicals that deplete the ozone layer?", "The Montreal Protocol", "The Kyoto Protocol", "The Paris Agreement", "The Rio Declaration"),
        ),
        19 to listOf(
            supplementalQuestion("Which laboratory operated the Large Hadron Collider when it circulated its first beam in 2008?", "CERN", "NASA", "Fermilab", "ITER"),
            supplementalQuestion("Which international science project announced completion of its reference human genome sequence in 2003?", "The Human Genome Project", "The Manhattan Project", "The Apollo Program", "The Biosphere 2 Project"),
            supplementalQuestion("In which city is the African Union headquartered?", "Addis Ababa", "Nairobi", "Accra", "Dakar"),
            supplementalQuestion("Which space station has maintained continuous human presence since November 2000?", "The International Space Station", "Mir", "Skylab", "Tiangong-1"),
        ),
        20 to listOf(
            supplementalQuestion("Which global goals did all UN member states adopt in 2015 as part of the 2030 Agenda?", "The Sustainable Development Goals", "The Millennium Charter", "The Geneva Targets", "The Bretton Woods Goals"),
            supplementalQuestion("What is the main role of the Intergovernmental Panel on Climate Change?", "Assessing published climate science for policymakers", "Negotiating binding trade treaties", "Operating weather satellites", "Enforcing national emissions laws"),
            supplementalQuestion("Which treaty is credited with driving the phaseout of many ozone-depleting substances?", "The Montreal Protocol", "The Ramsar Convention", "The Antarctic Treaty", "The Basel Convention"),
            supplementalQuestion("Which UN body hosts the annual Conference of the Parties climate negotiations?", "The UNFCCC", "The UN Security Council", "The World Trade Organization", "The International Court of Justice"),
        ),
    )
}
