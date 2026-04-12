# MC Hammer Mod (Minecraft Fabric 1.21.11)

Lernprojekt fuer den Einstieg ins Minecraft Modding.

Ziel ist ein neues Werkzeug **Hammer**, das sehr stark ist, viele Blockarten effizient abbauen kann und mit einer angepassten Rezeptlogik gecraftet wird.

Mod Name (offiziell): Minecraft Hammer
Mod Name kurz: "MC Ham"
Ingame Item: “Hammer”
Discord / Bot später: !ham oder !hammer

## Vision

Der Hammer soll:
- wie ein vielseitiges Multi-Tool nutzbar sein (Stein, Erde, Sand, Kies und weitere geeignete Blockgruppen)
- als Basisrezept eine Schaufel-Form nutzen (2 Stoecke)
- als Kopfmaterial **Bloecke** verwenden (z. B. Eisenblock, Kupferblock, Diamantblock), nicht klassische Ingot-Rezepte
- sehr hohe Haltbarkeit und starke Abbaugeschwindigkeit besitzen

## Projektstatus

- Status: Initiale Dokumentationsphase abgeschlossen
- Naechster Schritt: Fabric-Projektgeruest erzeugen und Hammer V1 implementieren

## Architektur

### Zielarchitektur (Code)

- `McHammerMod`:
	- Mod-Einstiegspunkt
	- startet Registrierung aller Inhalte
- `HammerItems`:
	- zentrale Registrierung der Hammer-Items
- `HammerItem`:
	- Verhalten des Hammers (Effizienz, Haltbarkeitsverbrauch, ggf. Speziallogik)
- `HammerToolTiers`:
	- Materialwerte je Hammer-Tier (Durability, Speed, Mining-Level)

### Zielarchitektur (Resources)

- `fabric.mod.json`: Mod-Metadaten und Entrypoints
- `assets/<modid>/lang/de_de.json`: sichtbare Namen und Texte
- `assets/<modid>/models/item/*.json`: Item-Modelle
- `assets/<modid>/textures/item/*.png`: Item-Texturen
- `data/<modid>/recipes/*.json`: Crafting-Rezepte
- `data/<modid>/tags/blocks/*.json`: Blockgruppen fuer Mining-Effektivitaet

### Designentscheidungen

- erst eine stabile V1 mit genau einem Hammer
- dann Erweiterung auf mehrere Metallblock-Varianten
- Effizienz ueber Block-Tags statt harter Einzelblocklisten
- kurze Build/Test-Zyklen nach jeder Aenderung

## Build

Sobald das Fabric-Projektgeruest steht:

```powershell
./gradlew build
```

Erwartetes Ergebnis:
- Build erfolgreich
- Mod-JAR unter `build/libs/`

## Run (Entwicklung)

Lokaler Dev-Client:

```powershell
./gradlew runClient
```

Optionaler Dev-Server:

```powershell
./gradlew runServer
```

## Deploy

### Lokale Verteilung
1. JAR aus `build/libs/` nutzen.
2. In den Minecraft `mods`-Ordner (passende Version/Loader) kopieren.
3. Spiel starten und Mod laden.

### Deploy per Skript (Windows, empfohlen)

Skriptpfad:

- `scripts/publish-to-minecraft.ps1`

Nur kopieren (wenn bereits gebaut):

```powershell
./scripts/publish-to-minecraft.ps1
```

Build + kopieren in einem Schritt:

```powershell
./scripts/publish-to-minecraft.ps1 -BuildFirst
```

Eigenen Minecraft-Ordner angeben (z. B. anderer Launcher):

```powershell
./scripts/publish-to-minecraft.ps1 -BuildFirst -MinecraftDir "D:\Games\Minecraft\.minecraft"
```

### Plattformen (optional spaeter)

- Modrinth
- CurseForge

Empfehlung fuer spaeteren Upload:
- saubere Versionierung (SemVer, z. B. `0.1.0`)
- Changelog pro Release
- klare Abhaengigkeiten (Fabric Loader, Fabric API, MC-Version)

## Geplanter Fahrplan

Der detaillierte Umsetzungsplan liegt in:

- `PLAN.md`

## Lernfokus

Mit diesem Projekt lernst du die wichtigsten Modding-Bausteine:
- Registry und Mod-Einstieg
- Item-Verhalten und Balancing
- Rezepte und Tags
- Asset-Pipeline (Lang/Modell/Textur)
- Build- und Test-Workflow

## Nächste konkrete Umsetzungsschritte

1. Fabric-Template fuer 1.21.11 initialisieren.
2. Grundstruktur aus PLAN.md anlegen.
3. Ersten Hammer (Eisenblock-Hammer) implementieren.
4. Build und Ingame-Test ausfuehren.
