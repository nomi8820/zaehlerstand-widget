# FirstSpirit-Absatzvorlage „Zählerstand-Widget“

Redakteur*innen bauen das Zählerstand-Widget über eine **Absatzvorlage** in beliebige
Seiten ein. Die Vorlage besteht – wie in FirstSpirit üblich – aus drei Teilen:

| Datei | FirstSpirit-Reiter | Inhalt |
|---|---|---|
| `sectiontemplates/zaehlerstand_widget/form.xml` | Formular | Eingabekomponenten (`CMS_MODULE`) |
| `sectiontemplates/zaehlerstand_widget/rules.xml` | Regeln | Regeldefinitionen (`RULES`) |
| `sectiontemplates/zaehlerstand_widget/output_html.fs` | Ausgabekanal HTML | HTML mit FirstSpirit-Vorlagensyntax (`$CMS_VALUE(...)$` usw.) |

Die Dateien liegen hier als einfache Textdateien vor. Ein FirstSpirit-Server wird **nicht**
benötigt. Mit `npm run check:xml` kannst du prüfen, ob deine XML-Dateien wohlgeformt sind.
Ob die FirstSpirit-Syntax fachlich stimmt, schauen wir uns gemeinsam in der Nachbesprechung an.

Referenz: [FirstSpirit Online-Dokumentation](https://docs.e-spirit.com/odfs/) –
insbesondere *Vorlagenentwicklung → Formulare*, *Regeln* und *Vorlagensyntax*.

## Anforderungen der Redaktion

Die Redaktion möchte das Widget pro Absatz konfigurieren können:

| Feld | Pflicht | Hinweise |
|---|---|---|
| Überschrift | ja | max. 80 Zeichen |
| Einleitungstext | nein | kurzer Text, einfache Formatierung wäre schön |
| Energieart | ja | „Gas“ oder „Strom“ – das Widget zeigt dann nur passende Zähler |
| Verlauf anzeigen | nein | Ja/Nein – ob die Tabelle mit bisherigen Ständen angezeigt wird |
| Erfolgsmeldung | nein | max. 200 Zeichen; ist nichts gepflegt, zeigt das Widget einen Standardtext |

Rahmenbedingungen:

- Die Website ist **einsprachig (Deutsch)**.
- Eine Seite kann **mehrere** Absätze dieses Typs enthalten.
- Die Formularfelder sollen den üblichen Namenskonventionen folgen (Präfix `st_`).

## Schnittstelle zwischen CMS-Ausgabe und Widget (Zielbild)

Die Ausgabe der Absatzvorlage soll künftig folgendes Markup erzeugen. Das Vue-Widget liest
seine Konfiguration aus den `data-*`-Attributen:

```html
<section class="zaehlerstand-teaser">
  <h2 class="zaehlerstand-teaser__headline">Zählerstand melden</h2>
  <div class="zaehlerstand-teaser__text"><p>Einleitungstext …</p></div>
  <div
    data-zaehlerstand-widget
    data-energy-type="gas"
    data-show-history="true"
    data-success-message="Danke! Wir haben deinen Zählerstand erhalten."
  ></div>
</section>
```

| Attribut | Werte | Bedeutung |
|---|---|---|
| `data-zaehlerstand-widget` | – | Markiert den Einhängepunkt für das Widget |
| `data-energy-type` | `gas` \| `strom` | Nur Zähler dieser Energieart anzeigen |
| `data-show-history` | `true` \| `false` | Verlauf der bisherigen Stände anzeigen |
| `data-success-message` | Text (optional) | Meldung nach erfolgreichem Absenden |

Elemente ohne gepflegten Inhalt (z. B. ein leerer Einleitungstext) sollen **nicht** ausgegeben werden.
