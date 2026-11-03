import type { EnergyType } from '../types'

export interface WidgetConfig {
  energyType?: EnergyType
  showHistory: boolean
  successMessage: string
}

export const DEFAULT_SUCCESS_MESSAGE = 'Danke! Dein Zählerstand wurde gespeichert.'

/**
 * Liest die Einstellungen aus den data-*-Attributen der CMS-Ausgabe.
 */
export function readWidgetConfig(dataset: DOMStringMap): WidgetConfig {
  const { energyType, showHistory, successMessage } = dataset
  return {
    energyType: energyType === 'gas' || energyType === 'strom' ? energyType : undefined,
    showHistory: showHistory === 'true',
    successMessage: successMessage?.trim() || DEFAULT_SUCCESS_MESSAGE,
  }
}
