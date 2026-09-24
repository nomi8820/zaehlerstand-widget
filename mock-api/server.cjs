/*
 * Mock-API für die Probeaufgabe.
 * Basiert auf json-server 0.17 – Daten liegen in db.json (wird aus db.seed.json erzeugt).
 *
 *   npm run mock-api          -> startet auf http://localhost:3001
 *   npm run mock-api:reset    -> setzt die Daten auf den Ausgangszustand zurück
 *
 * Beschreibung der Endpunkte: siehe openapi.yaml
 */
const fs = require('node:fs')
const path = require('node:path')
const jsonServer = require('json-server')

const PORT = Number(process.env.PORT ?? 3001)
const DELAY_MS = Number(process.env.DELAY_MS ?? 600)
const dbFile = path.join(__dirname, 'db.json')
const seedFile = path.join(__dirname, 'db.seed.json')

if (!fs.existsSync(dbFile) || process.argv.includes('--reset')) {
  fs.copyFileSync(seedFile, dbFile)
}

const server = jsonServer.create()
const router = jsonServer.router(dbFile)

server.use(jsonServer.defaults({ logger: true }))
server.use(jsonServer.bodyParser)

// Künstliche Latenz – wie im echten Leben.
server.use((req, res, next) => setTimeout(next, DELAY_MS))

// Simulierter Ausfall: Die Ablesungen für Zähler 3 sind "gerade nicht verfügbar".
server.use((req, res, next) => {
  if (/^\/api\/meters\/3\/readings/.test(req.path)) {
    return res.status(503).json({ error: 'SERVICE_UNAVAILABLE', message: 'Der Dienst ist vorübergehend nicht erreichbar.' })
  }
  next()
})

// Minimale Eingangsprüfung für neue Zählerstände (keine Plausibilitätsprüfung!).
server.post('/api/meters/:id/readings', (req, res, next) => {
  const meterId = Number(req.params.id)
  if (!router.db.get('meters').find({ id: meterId }).value()) {
    return res.status(404).json({ error: 'NOT_FOUND', message: `Zähler ${req.params.id} existiert nicht.` })
  }
  const { value } = req.body ?? {}
  if (typeof value !== 'number' || !Number.isFinite(value) || value < 0) {
    return res.status(400).json({
      error: 'VALIDATION_ERROR',
      field: 'value',
      message: 'value muss eine nicht-negative Zahl sein.',
    })
  }
  req.body = {
    value,
    readAt: req.body.readAt ?? new Date().toISOString().slice(0, 10),
    source: 'customer',
    meterId,
  }
  // Direkt an die readings-Collection weiterreichen (json-server legt den Datensatz an).
  req.url = '/readings'
  next()
})

server.use(jsonServer.rewriter({ '/api/*': '/$1' }))
server.use(router)

server.listen(PORT, () => {
  console.log(`Mock-API läuft auf http://localhost:${PORT}/api (Verzögerung ${DELAY_MS} ms)`)
})
