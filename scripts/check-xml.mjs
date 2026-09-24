// Prüft alle XML-Dateien unter cms/ auf Wohlgeformtheit.
// Hinweis: Das ist KEINE Prüfung gegen die FirstSpirit-Syntax.
import { readdirSync, readFileSync, statSync } from 'node:fs'
import { join, relative } from 'node:path'
import { fileURLToPath } from 'node:url'
import { XMLValidator } from 'fast-xml-parser'

const root = fileURLToPath(new URL('../cms', import.meta.url))

function findXmlFiles(dir) {
  return readdirSync(dir).flatMap((name) => {
    const path = join(dir, name)
    if (statSync(path).isDirectory()) return findXmlFiles(path)
    return name.endsWith('.xml') ? [path] : []
  })
}

let failed = 0
for (const file of findXmlFiles(root)) {
  const result = XMLValidator.validate(readFileSync(file, 'utf8'))
  const name = relative(process.cwd(), file)
  if (result === true) {
    console.log(`✔ ${name}`)
  } else {
    failed++
    const { msg, line, col } = result.err
    console.error(`✖ ${name}:${line}:${col} – ${msg}`)
  }
}

if (failed > 0) {
  console.error(`\n${failed} Datei(en) nicht wohlgeformt.`)
  process.exit(1)
}
