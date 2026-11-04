package org.example.zaehlerstand.reading;

/** Der Zählerstand ist formal gültig, aber fachlich nicht plausibel (z.B. kleiner als der letzte). */
public class PlausibilityException extends RuntimeException {

  public PlausibilityException(String message) {
    super(message);
  }
}
