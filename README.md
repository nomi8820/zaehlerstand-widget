# Probeaufgabe: Zählerstand-Widget

Willkommen und danke für dein Interesse!

In dieser Aufgabe entwickelst du ein kleines, bestehendes Feature weiter: ein **Vue-Widget**, mit dem
Kund*innen ihren Zählerstand melden können, und die **FirstSpirit-Absatzvorlage**, über die die
Redaktion das Widget in Seiten einbaut.

Der Code in diesem Repository ist **bewusst nicht in gutem Zustand**: Es gibt Fehler, Lint-Fehler
und Stellen, die man besser machen kann. Das Finden und Beheben gehört zur Aufgabe.

---

## Rahmen

- **Zeit:** Plane etwa **4 Stunden** ein und bleib dabei. Du musst nicht alles schaffen –
  **wie du priorisierst**, ist Teil der Aufgabe. Schreib im README auf, was du weggelassen hast und warum.
- **Umfang:** Bearbeite so viel, wie du kannst. Wenn am Ende nicht alles abgearbeitet ist, ist das kein Problem. Viel wichtiger und interessanter ist die Vorgehensweise.
- **Hilfsmittel:** Doku, Suchmaschinen und KI-Assistenten sind erlaubt. Gib im README kurz an,
  wofür du KI genutzt hast. In der Nachbesprechung solltest du jede Zeile erklären können.
- **Abgabe:** als Link zu einem Git-Repository oder als Zip **inklusive `.git`-Ordner**.
  Wir schauen uns auch deine Commits an – kleine, nachvollziehbare Commits sind gern gesehen.
- **Nachbesprechung:** ca. 45 Minuten. Du stellst deine Lösung vor.

## Voraussetzungen und Start

- Node.js **22** oder neuer
- Für Teil C: .NET 8+ **oder** Java 21+

```bash
npm install
npm start            # startet Mock-API (Port 3001) und Vite-Dev-Server (Port 5173)
```

Dann <http://localhost:5173> öffnen.

| Befehl | Zweck |
|---|---|
| `npm start` | Mock-API und Dev-Server zusammen starten |
| `npm run mock-api` / `npm run dev` | einzeln starten |
| `npm run mock-api:reset` | Mock-Daten auf den Ausgangszustand zurücksetzen |
| `npm test` | Unit-Tests (Vitest) |
| `npm run lint` | ESLint |
| `npm run typecheck` | TypeScript-Prüfung (vue-tsc) |
| `npm run check:xml` | Prüft die XML-Dateien unter `cms/` auf Wohlgeformtheit |

## Was ist drin?

```
src/                    Vue-3-Widget (TypeScript)
mock-api/               Mock-REST-API (json-server) + openapi.yaml
cms/                    FirstSpirit-Absatzvorlage (Formular, Regeln, HTML-Ausgabe) + Anforderungen
backend/                leer – Platz für Teil C
.github/workflows/      leer – Platz für Teil D
index.html              simuliert eine vom CMS erzeugte Seite mit zwei Widget-Absätzen
```

Die Endpunkte der API und ihr (teils absichtlich unfreundliches) Verhalten sind in
[`mock-api/openapi.yaml`](mock-api/openapi.yaml) beschrieben.

---

## Die Aufgaben

### Teil A – Vue-Widget überarbeiten

Nutzer*innen und Tester*innen haben unter anderem Folgendes gemeldet:

- „Wenn ich einen anderen Zähler auswähle, ändert sich nichts.“
- „Als letzter Stand wird ein alter Wert angezeigt.“
- „Ich habe `11900,5` eingegeben, es kam ‚Danke!‘ – aber gespeichert wurde nichts.“
- „Beim Stromzähler steht m³.“
- „Manchmal passiert einfach gar nichts, und ich weiß nicht, ob es lädt oder kaputt ist.“
- „Auf der Seite mit zwei Widgets funktioniert nur eins.“

1. **Behebe die gemeldeten Fehler.** Du findest bestimmt noch weitere – nimm mit, was dir sinnvoll erscheint.
2. **Nutzerführung:** Lade-, Fehler- und Erfolgszustände im Widget statt `alert()`, verständliche
   Fehlermeldungen, bedienbar per Tastatur, Formularfelder mit Beschriftung.
3. **Validierung:** Der neue Stand darf nicht kleiner als der letzte Stand sein. Deutsche Eingaben mit
   Komma sollen funktionieren. Lagere die Validierung so aus, dass sie ohne Komponente testbar ist.
4. **Code-Qualität:** Typen statt `any`, `npm run lint` und `npm run typecheck` ohne Fehler,
   eine Struktur, die du im Team gern weiterentwickeln würdest.

### Teil B – FirstSpirit-Absatzvorlage erweitern

Die Anforderungen der Redaktion und das Zielbild für das HTML stehen in
[`cms/README.md`](cms/README.md).

1. **Formular** (`form.xml`): Erweitere das Formular um alle geforderten Felder und bring die
   vorhandenen Felder in Ordnung (Namen, Beschriftungen für die Redaktion).
2. **Regeln** (`rules.xml`): Die Überschrift ist ein Pflichtfeld.
3. **Ausgabe** (`output_html.fs`): Erzeuge das Markup aus dem Zielbild mit der FirstSpirit-Vorlagensyntax.
   Nicht gepflegte Felder sollen nicht als leere Elemente ausgegeben werden.
4. Setze die Anforderungen aus der Tabelle in `cms/README.md` in Regeln bzw. Formular um
   (Pflichtfelder, Längen).
5. Sorg dafür, dass die Ausgabe auch bei **mehreren Absätzen** auf einer Seite und bei Sonderzeichen
   in Redaktionstexten (z. B. `"` in der Erfolgsmeldung) korrektes HTML ergibt.

### Teil A+B – Widget an die CMS-Konfiguration anbinden

Das Widget soll seine Konfiguration aus den `data-*`-Attributen lesen (Energieart, Verlauf anzeigen,
Erfolgsmeldung) und mit mehreren Instanzen auf einer Seite funktionieren. Pass `index.html` so an,
dass sie die Ausgabe **deiner** neuen Absatzvorlage simuliert. Nimm am besten zwei unterschiedlich
konfigurierte Absätze.

### Teil C – Backend-Proxy

Siehe [`backend/README.md`](backend/README.md). C#/.NET oder Java, eine Minimalversion reicht.

### Teil D – Continuous Integration

Leg unter `.github/workflows/` eine GitHub-Actions-Pipeline an, die bei jedem Push und Pull Request
Lint, Typecheck, Tests und `check:xml` ausführt. Wenn du Teil C gemacht hast: gern auch Build und
Tests des Backends.

### Teil E – Dokumentation

Ergänze am Ende dieser Datei einen Abschnitt **„Meine Lösung“** mit:

- Was hast du geändert und warum? (Stichpunkte reichen)
- Welche Annahmen hast du getroffen?
- Was hast du bewusst weggelassen, und was würdest du mit mehr Zeit als Nächstes tun?
- Wofür hast du KI-Werkzeuge genutzt?
- Wie würdest du Widget-Skript, CMS und Backend in Produktion zusammenbringen
  (Auslieferung, Caching, Fehlerfälle, Sicherheit)? Eine halbe Seite reicht.

---

Bei Fragen melde dich gern bei uns. Unklarheiten darfst du aber auch selbst entscheiden und im README
begründen. Viel Spaß!


## Meine Lösung

### Starten und testen

```bash
npm install
npm start                  # Mock-API, Backend und Frontend starten
npm test                   # Frontend-Tests
cd backend && ./mvnw test  # Backend-Tests
```

Für `npm start` wird Java 21 benötigt, da der Backend-Proxy mitgestartet wird.

Ohne Java kann man in der `vite.config.ts` den Proxy-Port von `8080` auf `3001` ändern und die Mock-API und das Frontend separat mit `npm run mock-api` und `npm run dev` starten.

### Was ich geändert habe

**Teil A: Vue-Widget**

Ich habe zuerst die gemeldeten Fehler behoben:

- Beim Zählerwechsel werden die Ablesungen jetzt neu geladen (`watch`).
- Der letzte Zählerstand wird anhand des Datums ermittelt, da die API unsortierte Daten liefert.
- Deutsche Zahlenformate wie `11900,5` werden richtig verarbeitet. Außerdem wird geprüft, ob das Speichern erfolgreich war.
- Die Einheit (m³ oder kWh) kommt jetzt aus den Zählerdaten.
- Statt `alert()` gibt es Lade-, Fehler- und Erfolgsmeldungen direkt im Widget.
- Mehrere Widgets können auf einer Seite verwendet werden, da jedes separat initialisiert wird.

Zusätzlich habe ich Fehler beim schnellen Zählerwechsel und bei fehlgeschlagenen API-Anfragen behoben.

Die Validierung und Sortierung habe ich in eigene Dateien ausgelagert, damit sie einfacher getestet werden können. Außerdem habe ich `any` durch TypeScript-Typen ersetzt und die API-Aufrufe getrennt.

Das Formular ist auch per Tastatur bedienbar. Lint und Typecheck laufen ohne Fehler, insgesamt gibt es 27 Unit-Tests.

**Teil B: FirstSpirit**

- Das Formular um die fehlenden Felder erweitert und die Beschriftungen angepasst.
- Pflichtfelder und Zeichenbegrenzungen ergänzt.
- Die HTML-Ausgabe an die Anforderungen angepasst. Leere Felder werden nicht ausgegeben und Sonderzeichen escaped.
- Darauf geachtet, dass mehrere Absätze auf einer Seite funktionieren, ohne doppelte IDs oder mehrfach eingebundene Scripts.

**Teil A+B: CMS-Anbindung**

Das Widget liest jetzt die Einstellungen aus den `data-*`-Attributen. Darüber werden die Energieart, der Verlauf und die Erfolgsmeldung gesteuert.

In der `index.html` habe ich zwei Widgets mit unterschiedlichen Einstellungen eingebaut, um das zu testen.

**Teil C: Backend**

Ich habe einen Backend-Proxy mit Spring Boot und Java 21 umgesetzt. Er leitet die Anfragen an die Mock-API weiter und prüft die eingegebenen Werte. Weitere Details stehen in der [backend/README.md](backend/README.md).

**Teil D: CI**

Eine GitHub-Actions-Pipeline führt bei Pushes und Pull Requests automatisch Lint, Typecheck, Frontend-Tests, XML-Prüfung und Backend-Tests aus.

### Annahmen

- Der letzte Zählerstand ist der mit dem neuesten Datum. Bei gleichem Datum entscheidet die höhere ID.
- Ich gehe von deutschen Zahlenformaten aus (Komma als Dezimaltrennzeichen).
-  Ein neuer Stand gleich dem letzten ist erlaubt (kein Verbrauch).
-  „Verlauf anzeigen“ ist standardmäßig aus. Eine fehlende oder unbekannte Energieart zeigt alle Zähler.
- Das Widget-Script wird einmal pro Seite eingebunden.
- Für das Backend habe ich einen konfigurierbaren Maximalwert von 999.999,999 angenommen.
- Sonderzeichen in Redaktionstexten (z. B. " in der Erfolgsmeldung) werden mit .convert2 escaped. Ich gehe davon aus, dass die Konvertierungsregel des Projekts dafür eingerichtet ist.

### Was ich weggelassen habe

Wegen der begrenzten Zeit konnte ich nicht alles umsetzen. Mit mehr Zeit würde ich:

- Komponententests für Vue und Integrationstests für das Backend ergänzen.
- Die API-Antworten genauer validieren und die Fehlerbehandlung zwischen Frontend und Backend verbessern.
- Die Darstellung überarbeiten, z. B. Zahlen und Datum im deutschen Format anzeigen und Inline-Styles ersetzen.
- Die OpenAPI-Dokumentation für den Proxy ergänzen.
- Den Zählerwechsel berücksichtigen, bei dem ein neuer Zähler wieder bei 0 beginnt.

Die FirstSpirit-Dateien konnte ich nur anhand der Dokumentation und mit der XML-Prüfung kontrollieren, da ich keinen FirstSpirit-Server zur Verfügung hatte.

### KI-Nutzung

Ich habe Claude als Unterstützung genutzt, vor allem bei der FirstSpirit-Syntax, beim Schreiben der Tests, bei Bugfixes und für Vorschläge zur Codestruktur.

Die Änderungen habe ich selbst durchgesehen und mit Tests, im Browser und teilweise mit curl überprüft.

### Zeitaufwand

Etwas mehr als 4 Stunden.

### Wie ich das Ganze in Produktion umsetzen würde

**Auslieferung**

Das Widget würde ich mit `vite build` erstellen und die fertige JavaScript-Datei über einen Server oder ein CDN bereitstellen. FirstSpirit bindet das Script einmal pro Seite ein. Die einzelnen Absätze enthalten nur das HTML und die Einstellungen über `data-*`-Attribute. So kann das Widget aktualisiert werden, ohne die CMS-Vorlage ändern zu müssen.

**Caching**

Die Script-Datei bekommt einen Hash im Dateinamen und kann deshalb lange gecacht werden. Die Zählerdaten sind personenbezogen und sollten nicht im CDN oder Browser-Cache landen.

**Backend und API**

Das Widget würde die API über `/api` auf derselben Domain aufrufen. Ein Reverse Proxy leitet die Anfragen an das Backend weiter, das mit der eigentlichen Fach-API kommuniziert. So bleiben interne API-Adressen verborgen und CORS-Probleme werden vermieden.

**Fehlerbehandlung**

Bei fehlgeschlagenen Anfragen sollte das Widget verständliche Fehlermeldungen anzeigen. Im Backend würde ich Timeouts einbauen und Fehler protokollieren, damit man Probleme später nachvollziehen kann. Für GET-Anfragen wären auch automatische Wiederholungsversuche sinnvoll.

**Sicherheit**

In Produktion müsste das Backend prüfen, ob der Nutzer angemeldet ist und ob der ausgewählte Zähler zu seinem Kundenkonto gehört.

Außerdem müssen Eingaben auch im Backend validiert werden, da die Prüfung im Frontend umgangen werden kann. Zusätzlich würde ich CSRF-Schutz bei Cookie-Anmeldung, sicheres Escaping von CMS-Texten und eine Begrenzung der Anfragen berücksichtigen.
