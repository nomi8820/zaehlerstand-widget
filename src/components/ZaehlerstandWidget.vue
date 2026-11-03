<template>
  <div style="font-family: Arial; max-width: 600px">
    <h3 style="color: #004b87">Zählerstand melden</h3>

    <select v-model="selectedMeter" style="width: 100%; padding: 8px; margin-bottom: 10px">
      <option v-for="m in meters" :value="m.id">{{ m.label }} – {{ formatMeterNumber(m.meterNumber) }}</option>
    </select>

    <div v-if="latestReading">
      <p>
        Letzter Stand: <b>{{ latestReading.value }} {{ currentMeter?.unit }}</b> vom
        {{ latestReading.readAt }}
      </p>
    </div>

    <input v-model="newValue" placeholder="Neuer Zählerstand" style="padding: 8px; width: 200px">
    <button
      style="background: #004b87; color: white; padding: 8px 16px; border: none; margin-left: 8px"
      @click="send"
    >
      Senden
    </button>

    <table v-if="readings.length" style="margin-top: 20px; width: 100%">
      <tr>
        <th>Datum</th>
        <th>Stand</th>
        <th>Quelle</th>
      </tr>
      <tr v-for="r in readings">
        <td>{{ r.readAt }}</td>
        <td>{{ r.value }}</td>
        <td>{{ r.source }}</td>
      </tr>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { formatMeterNumber } from '../utils/format'
import { sortNewestFirst } from '../utils/readings'
import { validateReading } from '../utils/validation'

const meters = ref<any[]>([])
const selectedMeter = ref<any>(null)
const readings = ref<any[]>([])
const newValue = ref('')
const currentMeter = computed(() => meters.value.find((m) => m.id === selectedMeter.value))
const latestReading = computed(() => readings.value[0])

async function loadReadings(meterId: number) {
  readings.value = [] // keine Werte vom vorher gewählten Zähler zeigen
  const res = await fetch('http://localhost:3001/api/meters/' + meterId + '/readings')
  if (!res.ok) return
  readings.value = sortNewestFirst(await res.json())
}

// Bei jeder Zählerauswahl die Ablesungen neu laden.
watch(selectedMeter, (meterId) => {
  if (meterId !== null) loadReadings(meterId)
})

onMounted(async () => {
  const res = await fetch('http://localhost:3001/api/meters')
  meters.value = await res.json()
  selectedMeter.value = meters.value[0].id
})

async function send() {
  const result = validateReading(newValue.value, latestReading.value?.value)
  if (!result.ok) {
    alert(result.error)
    return
  }
  const res = await fetch('http://localhost:3001/api/meters/' + selectedMeter.value + '/readings', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ value: result.value }),
  })
  if (!res.ok) {
    alert('Der Zählerstand konnte nicht gespeichert werden. Bitte versuche es erneut.')
    return
  }
  const data = await res.json()
  readings.value = sortNewestFirst([...readings.value, data])
  alert('Danke!')
}
</script>
