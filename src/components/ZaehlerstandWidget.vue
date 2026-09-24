<template>
  <div style="font-family: Arial; max-width: 600px">
    <h3 style="color: #004b87">Zählerstand melden</h3>

    <select v-model="selectedMeter" style="width: 100%; padding: 8px; margin-bottom: 10px">
      <option v-for="m in meters" :value="m.id">{{ m.label }} – {{ formatMeterNumber(m.meterNumber) }}</option>
    </select>

    <div v-if="readings.length">
      <p>
        Letzter Stand: <b>{{ readings[readings.length - 1].value }} m³</b> vom
        {{ readings[readings.length - 1].readAt }}
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
import { ref, onMounted } from 'vue'
import { formatMeterNumber } from '../utils/format'

const meters = ref<any[]>([])
const selectedMeter = ref<any>(null)
const readings = ref<any[]>([])
const newValue = ref('')

onMounted(async () => {
  const res = await fetch('http://localhost:3001/api/meters')
  meters.value = await res.json()
  selectedMeter.value = meters.value[0].id
  const res2 = await fetch('http://localhost:3001/api/meters/' + selectedMeter.value + '/readings')
  readings.value = await res2.json()
  console.log('readings', readings.value)
})

async function send() {
  if (!newValue.value) {
    alert('Bitte Zählerstand eingeben')
    return
  }
  const last = readings.value[readings.value.length - 1]
  if (last && Number(newValue.value) < last.value) {
    alert('Der neue Zählerstand darf nicht kleiner als der letzte sein')
    return
  }
  const res = await fetch('http://localhost:3001/api/meters/' + selectedMeter.value + '/readings', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ value: Number(newValue.value) }),
  })
  const data = await res.json()
  readings.value.push(data)
  alert('Danke!')
}
</script>
