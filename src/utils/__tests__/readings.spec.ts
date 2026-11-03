import { describe, expect, it } from 'vitest'
import { sortNewestFirst } from '../readings'

describe('sortNewestFirst', () => {
  it('sortiert nach Ablesedatum, neueste zuerst – unabhängig von der API-Reihenfolge', () => {
    const readings = [
      { id: 1, readAt: '2025-12-31' },
      { id: 2, readAt: '2026-06-30' },
      { id: 3, readAt: '2025-06-30' },
    ]
    expect(sortNewestFirst(readings).map((r) => r.id)).toEqual([2, 1, 3])
  })

  it('nimmt bei gleichem Datum die zuletzt angelegte Ablesung zuerst', () => {
    const readings = [
      { id: 7, readAt: '2026-11-03' },
      { id: 8, readAt: '2026-11-03' },
    ]
    expect(sortNewestFirst(readings).map((r) => r.id)).toEqual([8, 7])
  })

  it('verändert das übergebene Array nicht', () => {
    const readings = [
      { id: 1, readAt: '2025-01-01' },
      { id: 2, readAt: '2026-01-01' },
    ]
    sortNewestFirst(readings)
    expect(readings.map((r) => r.id)).toEqual([1, 2])
  })
})
