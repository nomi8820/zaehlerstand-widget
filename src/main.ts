import { createApp } from 'vue'
import ZaehlerstandWidget from './components/ZaehlerstandWidget.vue'
import { readWidgetConfig } from './utils/config'

// Jedes Widget auf der Seite bekommt eine eigene App.
document.querySelectorAll<HTMLElement>('[data-zaehlerstand-widget]').forEach((el, index) => {
  const app = createApp(ZaehlerstandWidget, { ...readWidgetConfig(el.dataset) })
  app.config.idPrefix = `zsw${index}`
  app.mount(el)
})
