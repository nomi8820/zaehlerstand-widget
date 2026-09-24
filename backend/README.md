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
