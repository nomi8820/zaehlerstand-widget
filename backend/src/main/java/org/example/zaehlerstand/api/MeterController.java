package org.example.zaehlerstand.api;

import org.example.zaehlerstand.mockapi.MockApiClient;
import org.example.zaehlerstand.reading.ReadingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.example.zaehlerstand.mockapi.Reading;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/** REST-Endpunkte für Zähler und Zählerstände, die das Widget aufruft. */
@RestController
@RequestMapping(value = "/api/meters", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeterController {

  private final MockApiClient mockApi;
  private final ReadingService readingService;

  public MeterController(MockApiClient mockApi, ReadingService readingService) {
    this.mockApi = mockApi;
    this.readingService = readingService;
  }

  @GetMapping
  public String meters(@RequestParam(name = "energyType", required = false) String energyType) {
    return mockApi.getMeters(energyType);
  }

  @GetMapping("/{id}/readings")
  public String readings(@PathVariable("id") int meterId) {
    return mockApi.getReadings(meterId);
  }

  @PostMapping(value = "/{id}/readings", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Reading create(@PathVariable("id") int meterId, @Valid @RequestBody NewReadingRequest request) {
    return readingService.create(meterId, request.value());
  }
}
