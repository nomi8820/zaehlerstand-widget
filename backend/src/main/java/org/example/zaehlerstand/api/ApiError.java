package org.example.zaehlerstand.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Fehlerantwort im Format der Mock-API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String error, String field, String message) {

  public ApiError(String error, String message) {
    this(error, null, message);
  }
}
