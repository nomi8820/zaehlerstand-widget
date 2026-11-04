package org.example.zaehlerstand.mockapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Schickt die Anfragen an die Mock-API. */
@Component
public class MockApiClient {

  private final RestClient restClient;

  public MockApiClient(@Value("${mock-api.base-url}") String baseUrl) {
    this.restClient = RestClient.builder().baseUrl(baseUrl).build();
  }

  public String getMeters(String energyType) {
    return restClient.get()
      .uri(uri -> uri.path("/meters")
        .queryParamIfPresent("energyType", Optional.ofNullable(energyType))
        .build())
      .retrieve()
      .body(String.class);
  }

  public String getReadings(int meterId) {
    return restClient.get()
      .uri("/meters/{id}/readings", meterId)
      .retrieve()
      .body(String.class);
  }
  public List<Reading> getReadingList(int meterId) {
    return restClient.get()
      .uri("/meters/{id}/readings", meterId)
      .retrieve()
      .body(new ParameterizedTypeReference<List<Reading>>() {});
  }

  public Reading createReading(int meterId, double value) {
    return restClient.post()
      .uri("/meters/{id}/readings", meterId)
      .contentType(MediaType.APPLICATION_JSON)
      .body(Map.of("value", value))
      .retrieve()
      .body(Reading.class);
  }
}
