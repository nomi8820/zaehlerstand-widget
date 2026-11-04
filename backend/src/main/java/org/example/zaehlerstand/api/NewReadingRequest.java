package org.example.zaehlerstand.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** Eingabe für einen neuen Zählerstand; value muss gesetzt und ≥ 0 sein. */
public record NewReadingRequest(@NotNull @PositiveOrZero Double value) {
}
