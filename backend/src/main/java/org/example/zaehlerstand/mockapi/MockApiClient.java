package org.example.zaehlerstand.mockapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
}
