import { describe, expect, it } from 'vitest'
import { parseGermanNumber, validateReading } from '../validation'

describe('parseGermanNumber', () => {
  it.each([
    ['11900', 11900],
    ['11900,5', 11900.5],
    ['11.900,5', 11900.5],
    ['1.234.567', 1234567],
    ['  42,0  ', 42],
  ])('liest "%s" als %d', (input, expected) => {
    expect(parseGermanNumber(input)).toBe(expected)
  })

  it.each(['', 'abc', '11900.5', '1.23', '-5', '1,2,3', '12,'])('lehnt "%s" ab', (input) => {
    expect(parseGermanNumber(input)).toBeNull()
  })
})

describe('validateReading', () => {
  it('meldet eine leere Eingabe', () => {
    expect(validateReading('   ')).toEqual({ ok: false, error: 'Bitte gib einen Zählerstand ein.' })
  })

  it('meldet eine ungültige Zahl', () => {
    expect(validateReading('abc').ok).toBe(false)
  })

  it('akzeptiert Komma-Eingaben (gemeldeter Fehler 11900,5)', () => {
    expect(validateReading('11900,5', 11820.1)).toEqual({ ok: true, value: 11900.5 })
  })

  it('lehnt einen Stand unter dem letzten ab', () => {
    const result = validateReading('11000', 11820.1)
    expect(result).toEqual({
      ok: false,
      error: 'Der neue Stand darf nicht kleiner als der letzte Stand (11.820,1) sein.',
    })
  })

  it('erlaubt einen Stand gleich dem letzten', () => {
    expect(validateReading('11820,1', 11820.1)).toEqual({ ok: true, value: 11820.1 })
  })

  it('erlaubt jeden gültigen Stand, wenn es noch keinen letzten gibt', () => {
    expect(validateReading('0')).toEqual({ ok: true, value: 0 })
  })
})
