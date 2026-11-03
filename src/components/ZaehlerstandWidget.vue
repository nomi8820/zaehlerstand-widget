<template>
  <div style="font-family: Arial, sans-serif; max-width: 600px">
    <h3 style="color: #004b87">Zählerstand melden</h3>

    <p v-if="metersLoading" role="status">Zähler werden geladen …</p>
    <p v-else-if="metersError" role="alert" style="color: #b00020">{{ metersError }}</p>
    <p v-else-if="!meters.length">Es sind keine Zähler hinterlegt.</p>

    <form v-else novalidate @submit.prevent="send">
      <label :for="`${uid}-meter`" style="display: block; font-weight: bold">Zähler</label>
      <select
        :id="`${uid}-meter`"
        v-model="selectedMeter"
        :disabled="saving"
        style="width: 100%; padding: 8px; margin: 4px 0 10px"
      >
        <option v-for="m in meters" :key="m.id" :value="m.id">
          {{ m.label }} – {{ formatMeterNumber(m.meterNumber) }}
        </option>
      </select>

      <p v-if="readingsLoading" role="status">Ablesungen werden geladen …</p>
      <p v-else-if="readingsError" role="alert" style="color: #b00020">{{ readingsError }}</p>
      <p v-else-if="latestReading">
        Letzter Stand: <b>{{ latestReading.value }} {{ currentMeter?.unit }}</b> vom
        {{ latestReading.readAt }}
      </p>

      <label :for="`${uid}-value`" style="display: block; font-weight: bold">
        Neuer Zählerstand ({{ currentMeter?.unit }})
      </label>
      <input
        :id="`${uid}-value`"
        v-model="newValue"
        inputmode="decimal"
        placeholder="z. B. 11900,5"
        :aria-invalid="formError ? 'true' : undefined"
        :aria-describedby="formError ? `${uid}-error` : undefined"
        style="padding: 8px; width: 200px; margin-top: 4px"
      >
      <button
        type="submit"
        :disabled="saving"
        style="background: #004b87; color: white; padding: 8px 16px; border: none; margin-left: 8px"
      >
        {{ saving ? 'Wird gesendet …' : 'Senden' }}
      </button>

      <p v-if="formError" :id="`${uid}-error`" role="alert" style="color: #b00020">{{ formError }}</p>
      <p v-if="successMessage" role="status" style="color: #1b7f3b">{{ successMessage }}</p>
    </form>

    <table v-if="readings.length && !readingsLoading" style="margin-top: 20px; width: 100%">
      <tr>
        <th>Datum</th>
        <th>Stand</th>
        <th>Quelle</th>
      </tr>
      <tr v-for="r in readings" :key="r.id">
        <td>{{ r.readAt }}</td>
        <td>{{ r.value }}</td>
        <td>{{ r.source }}</td>
      </tr>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, useId } from 'vue'
import { formatMeterNumber } from '../utils/format'
import { sortNewestFirst } from '../utils/readings'
import { validateReading } from '../utils/validation'

const meters = ref<any[]>([])
const selectedMeter = ref<any>(null)
const readings = ref<any[]>([])
const newValue = ref('')

const uid = useId()

const metersLoading = ref(true)
const metersError = ref('')
const readingsLoading = ref(false)
const readingsError = ref('')
const saving = ref(false)
const formError = ref('')
const successMessage = ref('')

const NETWORK_ERROR = 'Keine Verbindung zum Server. Bitte prüfe deine Internetverbindung.'
const currentMeter = computed(() => meters.value.find((m) => m.id === selectedMeter.value))
const latestReading = computed(() => readings.value[0])

// Zählt Ladevorgänge, damit eine späte Antwort keinen neueren Zählerwechsel überschreibt.
let latestRequest = 0

async function loadReadings(meterId: number) {
  const request = ++latestRequest
  readings.value = []
  readingsError.value = ''
  readingsLoading.value = true
  try {
    const res = await fetch('http://localhost:3001/api/meters/' + meterId + '/readings')
    const data = res.ok ? await res.json() : null
    if (request !== latestRequest) return
    if (!data) {
      readingsError.value = 'Die Ablesungen können gerade nicht geladen werden. Bitte versuche es später erneut.'
      return
    }
    readings.value = sortNewestFirst(data)
  } catch {
    if (request === latestRequest) readingsError.value = NETWORK_ERROR
  } finally {
    if (request === latestRequest) readingsLoading.value = false
  }
}

// Bei jeder Zählerauswahl die Ablesungen neu laden.
watch(selectedMeter, (meterId) => {
  formError.value = ''
  successMessage.value = ''
  if (meterId !== null) loadReadings(meterId)
})

onMounted(async () => {
  try {
    const res = await fetch('http://localhost:3001/api/meters')
    if (!res.ok) throw new Error(`HTTP ${res.status}`)
    meters.value = await res.json()
    selectedMeter.value = meters.value[0]?.id ?? null
  } catch {
    metersError.value = 'Deine Zähler konnten nicht geladen werden. Bitte lade die Seite neu.'
  } finally {
    metersLoading.value = false
  }
})

async function send() {
  if (selectedMeter.value === null || saving.value) return
  formError.value = ''
  successMessage.value = ''

  const result = validateReading(newValue.value, latestReading.value?.value)
  if (!result.ok) {
    formError.value = result.error
    return
  }

  saving.value = true
  try {
    const res = await fetch('http://localhost:3001/api/meters/' + selectedMeter.value + '/readings', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ value: result.value }),
    })
    if (!res.ok) {
      formError.value = 'Der Zählerstand konnte nicht gespeichert werden. Bitte versuche es erneut.'
      return
    }
    const data = await res.json()
    readings.value = sortNewestFirst([...readings.value, data])
    newValue.value = ''
    successMessage.value = 'Danke! Dein Zählerstand wurde gespeichert.'
  } catch {
    formError.value = NETWORK_ERROR
  } finally {
    saving.value = false
  }
}
</script>
