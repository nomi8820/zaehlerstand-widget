package org.example.zaehlerstand.reading;

import org.example.zaehlerstand.mockapi.MockApiClient;
import org.example.zaehlerstand.mockapi.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Testet die Plausibilitätsregeln ohne HTTP; die Mock-API wird durch Mockito ersetzt. */
class ReadingServiceTest {

  private static final double MAX_VALUE = 999_999.999;

  private MockApiClient mockApi;
  private ReadingService service;

  @BeforeEach
  void setUp() {
    mockApi = mock(MockApiClient.class);
    service = new ReadingService(mockApi, MAX_VALUE);
  }

  private static Reading reading(int id, double value, String readAt) {
    return new Reading(id, 1, value, readAt, "customer");
  }

  @Test
  @DisplayName("speichert einen Stand, der größer als der letzte ist")
  void acceptsHigherValue() {
    when(mockApi.getReadingList(1)).thenReturn(List.of(reading(1, 11820.1, "2026-06-30")));
    Reading created = reading(2, 11900.5, "2026-11-04");
    when(mockApi.createReading(1, 11900.5)).thenReturn(created);

    assertThat(service.create(1, 11900.5)).isEqualTo(created);
  }

  @Test
  @DisplayName("erlaubt einen Stand gleich dem letzten")
  void acceptsEqualValue() {
    when(mockApi.getReadingList(1)).thenReturn(List.of(reading(1, 11820.1, "2026-06-30")));

    service.create(1, 11820.1);

    verify(mockApi).createReading(1, 11820.1);
  }

  @Test
  @DisplayName("bestimmt den letzten Stand nach Datum, nicht nach Position (Reihenfolge wie in den Seed-Daten)")
  void usesNewestByDateNotLastInList() {
    when(mockApi.getReadingList(1)).thenReturn(List.of(
      reading(1, 10234.5, "2025-12-31"),
      reading(2, 11820.1, "2026-06-30"),
      reading(3, 9120.0, "2025-06-30")));

    // 10000 wäre größer als das letzte Element (9120), aber kleiner als der neueste Stand.
    assertThatThrownBy(() -> service.create(1, 10000))
      .isInstanceOf(PlausibilityException.class)
      .hasMessageContaining("11820.1");
    verify(mockApi, never()).createReading(anyInt(), anyDouble());
  }

  @Test
  @DisplayName("nimmt bei gleichem Datum die höhere id als letzten Stand")
  void sameDateUsesHigherId() {
    when(mockApi.getReadingList(1)).thenReturn(List.of(
      reading(8, 500.0, "2026-11-04"),
      reading(7, 400.0, "2026-11-04")));

    assertThatThrownBy(() -> service.create(1, 450))
      .isInstanceOf(PlausibilityException.class);
  }

  @Test
  @DisplayName("lehnt einen Stand über dem Maximalwert ab, ohne die Mock-API zu fragen")
  void rejectsTooHighValue() {
    assertThatThrownBy(() -> service.create(1, 5_000_000))
      .isInstanceOf(PlausibilityException.class)
      .hasMessageContaining("zu hoch");
    verify(mockApi, never()).getReadingList(anyInt());
    verify(mockApi, never()).createReading(anyInt(), anyDouble());
  }

  @Test
  @DisplayName("erlaubt jeden Stand, wenn es noch keinen gibt")
  void acceptsAnyValueWithoutHistory() {
    when(mockApi.getReadingList(1)).thenReturn(List.of());

    service.create(1, 0);

    verify(mockApi).createReading(1, 0);
  }
}
