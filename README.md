# Freelook (NeoForge 1.21.1, Client-Side)

Mit einer frei belegbaren Taste (Standard: Linke Alt-Taste) kannst du frei um deinen Spieler herumsehen,
ohne dass sich Spieler, Kopf oder Blickrichtung drehen. Die Kamera geht nicht durch Blöcke.

- Taste: Optionen > Steuerung > Tastenbelegung > Kategorie "Freelook"
- Modus (Halten / Umschalten) und Kameraabstand: Mods > Freelook > Konfiguration
  (oder `config/freelook-client.toml`)
- Rein clientseitig: Die Mod muss nicht auf dem Server installiert sein.

## Bauen

Voraussetzung: JDK 21.

    ./gradlew build       # fertige Mod: build/libs/freelook-1.0.0.jar
    ./gradlew runClient   # Testen im Entwicklungs-Client

Die JAR kommt in den `mods`-Ordner deiner NeoForge-1.21.1-Instanz.

## GitHub

Der Workflow `.github/workflows/build.yml` baut die Mod bei jedem Push und Pull Request.
Die JAR findest du dann im Actions-Tab unter "Artifacts".

Für ein Release einfach einen Tag pushen:

    git tag v1.0.0
    git push origin v1.0.0

GitHub erstellt dann automatisch ein Release mit der JAR als Download.

## Lizenz

MIT, siehe `LICENSE`.
