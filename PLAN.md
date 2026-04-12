# PLAN.md - Minecraft Mod "MC Hammer"

## Ziel
Ein neuer Minecraft-Mod fuer Fabric (1.21.1) mit einem starken Werkzeug "Hammer".

Der Hammer soll:
- viele Blocktypen effizient abbauen (z. B. Stein, Erde, Sand, Kies)
- als Rezeptlogik Schaufel-Form nutzen (2 Stoecke), aber nur Blockmaterialien als Kopf akzeptieren
- sehr hohe Haltbarkeit haben
- als Lernprojekt Schritt fuer Schritt erweiterbar sein

## Scope V1
- ein einzelner Hammer (Start: Eisenblock-Hammer)
- Item-Registrierung
- Crafting-Rezept
- Mining-Effektivitaet ueber Block-Tags
- hohe Haltbarkeit + hoher Mining-Speed
- Basis-Assets (Name, Modell, Textur)

## Scope V2
- weitere Hammer-Varianten (Kupferblock, Diamantblock, Goldblock)
- Balancing-Matrix pro Material
- optionale Spezialmechanik (z. B. spaeter 3x3)

## Architekturplan

### Paketstruktur (geplant)
- src/main/java/<package>/McHammerMod.java
  - Mod-Initialisierung
- src/main/java/<package>/item/HammerItems.java
  - Registry fuer alle Hammer-Items
- src/main/java/<package>/item/HammerItem.java
  - zentrales Item-Verhalten (Mining, Haltbarkeit)
- src/main/java/<package>/tool/HammerToolTiers.java
  - Material-/Tier-Werte (Speed, Durability, Mining-Level)

### Resource-Struktur (geplant)
- src/main/resources/fabric.mod.json
- src/main/resources/assets/<modid>/lang/de_de.json
- src/main/resources/assets/<modid>/models/item/*.json
- src/main/resources/assets/<modid>/textures/item/*.png
- src/main/resources/data/<modid>/recipes/*.json
- src/main/resources/data/<modid>/tags/blocks/*.json

### Design-Prinzipien
- erst eine Variante, dann Skalierung
- Block-Effektivitaet ueber Tags statt harter Einzelblocklisten
- klare Trennung von Registrierung, Item-Logik, Material-Stats
- kurze Testzyklen nach jeder Funktion

## Umsetzungsphasen

### Phase 1: Projekt-Setup
1. Fabric-Template fuer 1.21.1 initialisieren.
2. Mod-ID, Paketname und Anzeigename final festlegen.
3. Gradle-Tasks pruefen (build, runClient).

### Phase 2: Hammer V1
1. Hammer-Item registrieren.
2. ToolTier/Stats setzen (hoch, aber testbar).
3. Rezept anlegen: Schaufel-Pattern mit Blockmaterial.
4. Block-Tags fuer Effizienz hinterlegen.

### Phase 3: Assets + UX
1. Name in de_de.json definieren.
2. Item-Modell + Textur integrieren.
3. Tooltip mit kurzer Erklaerung optional ergaenzen.

### Phase 4: Tests
1. Build ohne Fehler.
2. Ingame Crafting pruefen.
3. Mining-Speed gegen verschiedene Blocktypen pruefen.
4. Haltbarkeitsverbrauch pruefen.

### Phase 5: Erweiterung
1. Materialvarianten hinzufuegen.
2. Werte pro Material balancieren.
3. Regressionstest ueber alle Rezepte und Varianten.

## Done-Kriterien fuer V1
- Mod laedt im Dev-Client.
- Hammer ist craftbar.
- Hammer baut Zielblockgruppen schnell ab.
- Haltbarkeit sinkt korrekt und ist deutlich hoeher als bei Standard-Tools.
- README und Plan sind aktuell und konsistent.
