import { describe, expect, it } from 'vitest'
import { DEFAULT_SUCCESS_MESSAGE, readWidgetConfig } from '../config'

describe('readWidgetConfig', () => {
  it('übernimmt alle gepflegten Werte', () => {
    expect(
      readWidgetConfig({ energyType: 'strom', showHistory: 'true', successMessage: 'Vielen "Dank"!' }),
    ).toEqual({ energyType: 'strom', showHistory: true, successMessage: 'Vielen "Dank"!' })
  })

  it('nutzt Standardwerte, wenn nichts gepflegt ist', () => {
    expect(readWidgetConfig({})).toEqual({
      energyType: undefined,
      showHistory: false,
      successMessage: DEFAULT_SUCCESS_MESSAGE,
    })
  })

  it('ignoriert eine unbekannte Energieart', () => {
    expect(readWidgetConfig({ energyType: 'wasser' }).energyType).toBeUndefined()
  })

  it('behandelt eine leere Erfolgsmeldung wie keine', () => {
    expect(readWidgetConfig({ successMessage: '   ' }).successMessage).toBe(DEFAULT_SUCCESS_MESSAGE)
  })
})
