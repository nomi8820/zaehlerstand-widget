# Backend-Proxy (Teil C)

Hier kommt dein Backend hin – **C#/.NET oder Java**, deine Wahl.

Ziel: Das Widget spricht künftig nicht mehr direkt mit der Mock-API, sondern mit
diesem Proxy. Der Proxy

- leitet `GET /api/meters` und `GET /api/meters/{id}/readings` an die Mock-API weiter,
- nimmt `POST /api/meters/{id}/readings` entgegen und führt vorher eine
  **Plausibilitätsprüfung** durch (neuer Stand ≥ letzter Stand, sinnvoller Maximalwert),
- liefert verständliche Fehlerantworten (Format wie in `mock-api/openapi.yaml`),
- geht mit Fehlern der Mock-API sinnvoll um (z. B. 503 bei Zähler 3).

Eine Minimalversion reicht. Beschreibe im README, wie man den Proxy startet und
wie du ihn getestet hast.

---

## Meine Umsetzung

Ich habe den Proxy mit Spring Boot 4 und Java 21 umgesetzt. Er läuft auf Port 8080 und leitet die Anfragen an die Mock-API auf Port 3001 weiter. Die Einstellungen können in der `application.properties` angepasst werden.

### Starten

```bash
npm run mock-api          # im Projekthauptordner
cd backend
./mvnw spring-boot:run
```

Oder alles zusammen mit `npm start` im Projekthauptordner.

### Fehlerantworten

| Fall | Status                     |
|---|----------------------------|
| `value` fehlt, negativ oder keine Zahl | `400`                      |
| kleiner als der letzte Stand oder über 999.999,999 | `422`                      |
| Fehler der Mock-API (z. B. Zähler 3) | `503` (wird durchgereicht) |
| Mock-API nicht erreichbar | `502`                      |

### Wie ich getestet habe

- **Unit-Tests** (`./mvnw test`): Die Plausibilitätsprüfung in `ReadingService` wird ohne HTTP getestet, die Mock-API wird mit Mockito simuliert.
- **Manuell mit curl:**

```bash
curl -i localhost:8080/api/meters?energyType=strom                                                                # 200
curl -i localhost:8080/api/meters/3/readings                                                                      # 503
curl -i -X POST localhost:8080/api/meters/1/readings -H 'Content-Type: application/json' -d '{"value": 11900.5}'  # 201
curl -i -X POST localhost:8080/api/meters/1/readings -H 'Content-Type: application/json' -d '{"value": 5000}'     # 422
curl -i -X POST localhost:8080/api/meters/1/readings -H 'Content-Type: application/json' -d '{"value": 5000000}'  # 422
curl -i -X POST localhost:8080/api/meters/1/readings -H 'Content-Type: application/json' -d '{"value": -1}'       # 400
curl -i -X POST localhost:8080/api/meters/1/readings -H 'Content-Type: application/json' -d '{"value": "abc"}'    # 400
```
