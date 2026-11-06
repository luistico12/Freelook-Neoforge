# Freelook (NeoForge 1.21.1, Client-Side)

Mit einer frei belegbaren Taste (Standard: Linke Alt-Taste) kannst du frei um deinen Spieler herumsehen,
ohne dass sich Spieler, Kopf oder Blickrichtung drehen. Die Kamera geht nicht durch Blöcke.

- Taste: Optionen > Steuerung > Tastenbelegung > Kategorie "Freelook"
- Modus (Halten / Umschalten) und Kameraabstand: Mods > Freelook > Konfiguration
  (oder `config/freelook-client.toml`)
- Rein clientseitig: Die Mod muss nicht auf dem Server installiert sein.

## Bauen

Voraussetzung: JDK 21 und der Gradle-Wrapper aus dem offiziellen NeoForge-MDK
(Ordner `gradle/` sowie `gradlew` und `gradlew.bat` hierher kopieren) oder ein installiertes Gradle.

    ./gradlew build       # fertige Mod: build/libs/freelook-1.0.0.jar
    ./gradlew runClient   # Testen im Entwicklungs-Client

Die JAR kommt in den `mods`-Ordner deiner NeoForge-1.21.1-Instanz.
