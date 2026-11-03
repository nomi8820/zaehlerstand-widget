package org.example.zaehlerstand.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

/** Wandelt Fehler aus allen Controllern in passende HTTP-Antworten um. */
@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(RestClientResponseException.class)
  public ResponseEntity<String> upstreamError(RestClientResponseException e) {
    return ResponseEntity.status(e.getStatusCode())
      .contentType(MediaType.APPLICATION_JSON)
      .body(e.getResponseBodyAsString());
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<ApiError> upstreamUnreachable(ResourceAccessException e) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
      .body(new ApiError("UPSTREAM_UNAVAILABLE", "Die Zählerdaten sind gerade nicht erreichbar."));
  }
}
