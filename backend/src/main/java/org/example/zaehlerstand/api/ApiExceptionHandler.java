package org.example.zaehlerstand.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.example.zaehlerstand.reading.PlausibilityException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

  @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ApiError> invalidInput(Exception e) {
    return ResponseEntity.badRequest()
      .body(new ApiError("VALIDATION_ERROR", "value", "value muss eine nicht-negative Zahl sein."));
  }

  @ExceptionHandler(PlausibilityException.class)
  public ResponseEntity<ApiError> implausible(PlausibilityException e) {
    return ResponseEntity.status(422)
      .body(new ApiError("PLAUSIBILITY_ERROR", "value", e.getMessage()));
  }
}
