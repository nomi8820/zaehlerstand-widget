export type ValidationResult = { ok: true; value: number } | { ok: false; error: string }

/**
 * Liest eine Zahl im deutschen Format.
 */
export function parseGermanNumber(input: string): number | null {
  const text = input.trim()
  const plain = /^\d+(,\d+)?$/
  const withThousands = /^\d{1,3}(\.\d{3})+(,\d+)?$/
  if (!plain.test(text) && !withThousands.test(text)) return null
  return Number(text.replace(/\./g, '').replace(',', '.'))
}

export function validateReading(input: string, lastValue?: number): ValidationResult {
  if (!input.trim()) {
    return { ok: false, error: 'Bitte gib einen Zählerstand ein.' }
  }
  const value = parseGermanNumber(input)
  if (value === null) {
    return { ok: false, error: 'Bitte gib eine gültige Zahl ein, z. B. 11900,5.' }
  }
  if (lastValue !== undefined && value < lastValue) {
    const last = lastValue.toLocaleString('de-DE')
    return { ok: false, error: `Der neue Stand darf nicht kleiner als der letzte Stand (${last}) sein.` }
  }
  return { ok: true, value }
}
