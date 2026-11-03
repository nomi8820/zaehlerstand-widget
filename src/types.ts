export type EnergyType = 'gas' | 'strom'

export interface Meter {
  id: number
  meterNumber: string
  energyType: EnergyType
  unit: string
  address: string
  label: string
}

export interface Reading {
  id: number
  meterId: number
  value: number
  readAt: string
  source: 'customer' | 'estimated' | 'technician'
}
