/**
 * Alle Aufrufe an die Zähler-API an einer Stelle.
 * Bei einem Fehlerstatus wird ein ApiError geworfen, bei fehlender Verbindung ein normaler Fehler von fetch.
 */
import type { Meter, Reading } from '../types'

const BASE_URL = '/api'

export class ApiError extends Error {
  readonly status: number

  constructor(status: number) {
    super(`HTTP ${status}`)
    this.status = status
  }
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(BASE_URL + path, init)
  if (!res.ok) throw new ApiError(res.status)
  return (await res.json()) as T
}

export function fetchMeters(): Promise<Meter[]> {
  return request('/meters')
}

export function fetchReadings(meterId: number): Promise<Reading[]> {
  return request(`/meters/${meterId}/readings`)
}

export function createReading(meterId: number, value: number): Promise<Reading> {
  return request(`/meters/${meterId}/readings`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ value }),
  })
}
