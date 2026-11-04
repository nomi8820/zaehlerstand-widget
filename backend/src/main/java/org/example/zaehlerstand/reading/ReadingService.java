package org.example.zaehlerstand.reading;

import org.example.zaehlerstand.mockapi.MockApiClient;
import org.example.zaehlerstand.mockapi.Reading;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.Optional;

/** Prüft einen neuen Stand auf Plausibilität und speichert ihn dann in der Mock-API. */
@Service
public class ReadingService {

  private final MockApiClient mockApi;
  private final double maxValue;

  public ReadingService(MockApiClient mockApi, @Value("${plausibility.max-value}") double maxValue) {
    this.mockApi = mockApi;
    this.maxValue = maxValue;
  }

  public Reading create(int meterId, double value) {
    if (value > maxValue) {
      throw new PlausibilityException("Der Zählerstand ist zu hoch (maximal " + maxValue + ").");
    }
    Optional<Reading> latest = findLatest(meterId);
    if (latest.isPresent() && value < latest.get().value()) {
      throw new PlausibilityException(
        "Der neue Stand darf nicht kleiner als der letzte Stand (" + latest.get().value() + ") sein.");
    }
    return mockApi.createReading(meterId, value);
  }

  private Optional<Reading> findLatest(int meterId) {
    return mockApi.getReadingList(meterId).stream()
      .max(Comparator.comparing(Reading::readAt).thenComparingInt(Reading::id));
  }
}
