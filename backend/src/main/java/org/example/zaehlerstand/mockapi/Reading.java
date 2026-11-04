package org.example.zaehlerstand.mockapi;

/** Ein Zählerstand, wie ihn die Mock-API liefert. */
public record Reading(int id, int meterId, double value, String readAt, String source) {
}
