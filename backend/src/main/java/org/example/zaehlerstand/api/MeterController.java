package org.example.zaehlerstand.api;

import org.example.zaehlerstand.mockapi.MockApiClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST-Endpunkte für Zähler und Zählerstände, die das Widget aufruft. */
@RestController
@RequestMapping(value = "/api/meters", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeterController {

  private final MockApiClient mockApi;

  public MeterController(MockApiClient mockApi) {
    this.mockApi = mockApi;
  }

  @GetMapping
  public String meters(@RequestParam(name = "energyType", required = false) String energyType) {
    return mockApi.getMeters(energyType);
  }

  @GetMapping("/{id}/readings")
  public String readings(@PathVariable("id") int meterId) {
    return mockApi.getReadings(meterId);
  }
}
