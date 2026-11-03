/**
 * Sortiert Ablesungen nach Datum, neueste zuerst.
 */
export function sortNewestFirst<T extends { id: number; readAt: string }>(readings: T[]): T[] {
  return [...readings].sort((a, b) => b.readAt.localeCompare(a.readAt) || b.id - a.id)
}
