/**
 * Formatiert eine Zählernummer für die Anzeige in 4er-Blöcken.
 * Beispiel: "1ESY1160123456" -> "1ESY 1160 1234 56"
 */
export function formatMeterNumber(meterNumber: string): string {
  return meterNumber.replace(/\s+/g, '').replace(/(.{4})(?=.)/g, '$1 ')
}
