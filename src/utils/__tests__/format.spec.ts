import { describe, expect, it } from 'vitest'
import { formatMeterNumber } from '../format'

describe('formatMeterNumber', () => {
  it('gruppiert die Zählernummer in 4er-Blöcke', () => {
    expect(formatMeterNumber('1ESY1160123456')).toBe('1ESY 1160 1234 56')
  })

  it('entfernt vorhandene Leerzeichen vor dem Gruppieren', () => {
    expect(formatMeterNumber('1ESY 116 0123456')).toBe('1ESY 1160 1234 56')
  })
})
