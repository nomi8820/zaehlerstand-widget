import { createApp } from 'vue'
import ZaehlerstandWidget from './components/ZaehlerstandWidget.vue'

// Jedes Widget auf der Seite bekommt eine eigene App.
document.querySelectorAll<HTMLElement>('[data-zaehlerstand-widget]').forEach((el, index) => {
  const app = createApp(ZaehlerstandWidget)
  app.config.idPrefix = `zsw${index}`
  app.mount(el)
})
