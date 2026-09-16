---
name: Imperial Antiquity
colors:
  surface: '#161214'
  surface-dim: '#161214'
  surface-bright: '#3c383a'
  surface-container-lowest: '#100d0f'
  surface-container-low: '#1e1b1d'
  surface-container: '#221f21'
  surface-container-high: '#2d292b'
  surface-container-highest: '#383436'
  on-surface: '#e8e0e3'
  on-surface-variant: '#d9c1c3'
  inverse-surface: '#e8e0e3'
  inverse-on-surface: '#332f31'
  outline: '#a18c8d'
  outline-variant: '#534344'
  surface-tint: '#ffb2bc'
  primary: '#ffb2bc'
  on-primary: '#5a1827'
  primary-container: '#5a1827'
  on-primary-container: '#d97d8c'
  inverse-primary: '#944553'
  secondary: '#e9c176'
  on-secondary: '#412d00'
  secondary-container: '#604403'
  on-secondary-container: '#dab36a'
  tertiary: '#d4c3be'
  on-tertiary: '#392e2b'
  tertiary-container: '#392e2b'
  on-tertiary-container: '#a49591'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffd9dd'
  primary-fixed-dim: '#ffb2bc'
  on-primary-fixed: '#3e0313'
  on-primary-fixed-variant: '#772e3d'
  secondary-fixed: '#ffdea5'
  secondary-fixed-dim: '#e9c176'
  on-secondary-fixed: '#261900'
  on-secondary-fixed-variant: '#5d4201'
  tertiary-fixed: '#f1dfda'
  tertiary-fixed-dim: '#d4c3be'
  on-tertiary-fixed: '#231917'
  on-tertiary-fixed-variant: '#504441'
  background: '#161214'
  on-background: '#e8e0e3'
  surface-variant: '#383436'
typography:
  display-lg:
    fontFamily: Libre Caslon Text
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: 0.04em
  display-lg-mobile:
    fontFamily: Libre Caslon Text
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: 0.03em
  headline-lg:
    fontFamily: Libre Caslon Text
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
    letterSpacing: 0.02em
  headline-md:
    fontFamily: Libre Caslon Text
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: 0.01em
  headline-sm:
    fontFamily: Libre Caslon Text
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  title-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.05em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.06em
  label-sm:
    fontFamily: Inter
    fontSize: 10px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.08em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system establishes an authoritative, tactile imperial aesthetic for an elite historical trivia and progression environment. Drawing inspiration from Roman monumental epigraphy, lapidary lapis, imperial Tyrian porphyry, and bronze-inlaid tablets, the experience balances dignified architectural weight with crisp digital utility.

The personality is scholarly, commanding, and prestigious. It avoids cartoonish gamification in favor of structured historical gravitas. Interactions evoke physical contact with cold chiseled marble, lacquered dark wood, and cast metal. Surfaces feature subtle physical bevels, finely etched borders, and restrained metallic accents. Utilitarian sans-serif typography ensures rapid comprehension under trivia time pressure, framed by classical, inscriptional display headers.

## Colors

The palette is rooted in dark imperial majesty:
- **Primary (`#5A1827`)**: Tyrian Burgundy. Used for core brand elements, primary call-to-action surfaces, victory banners, and prominent UI containers.
- **Secondary (`#C5A059`)**: Antique Gold. Strictly reserved for progression milestones, laurel embellishments, score multipliers, and elite mastery stars. It is never used as large background fills.
- **Tertiary (`#2E2421`)**: Weathered Bronze. Serves as intermediate structural framing, inactive toggle tracks, subtle dividers, and tertiary interactive chips.
- **Neutral (`#131012`)**: Imperial Charcoal Basalt. The deep obsidian-tinted backdrop providing contrast against warm stone cards (`#1F1A1C`) and elevated trivia plates (`#282225`).

Text colors conform to high-legibility standards: Parchment White (`#F3EFE6`) for titles and body questions, Muted Sand (`#B8B0A5`) for secondary metadata, and Oxidized Iron (`#6E6561`) for disabled states.

## Typography

The type system pairs monumental Latin-style serifs with high-efficiency sans-serif letterforms:
- **Display & Headlines (`Libre Caslon Text`)**: Emulates classical inscriptional stonework. Titles, era headers, chapter banners, and victory declarations use this family with subtle positive letter spacing to invoke monumental Roman capitals.
- **Interface, Trivia & Body (`Inter`)**: Guarantees zero cognitive friction during timed gameplay. Trivia question stems, answer matrices, analytics, and operational microcopy rely on Inter's tall x-height and neutral geometry.

All uppercase labels (`label-sm`, `label-md`) use extended letter spacing to retain legibility against dark slate backdrops.

## Layout & Spacing

The layout is built for an immersive mobile-first portrait view (360dp to 430dp standard viewport widths) scaled symmetrically across tablets:
- **Base Rhythm**: Uses an 8dp structural grid paired with a 4dp micro-step for compact trivia option stacks and status badges.
- **Gutters & Canvas Margins**: Outer canvas padding stays strictly at `1rem` (16dp) on mobile viewports to maximize surface area for question text and answer interaction targets. Tablet screens expand gutters and side margins to `1.5rem` (24dp) while capping content widths at 600dp for focused trivia containment.
- **Reflow & Flow**: Vertically stacked single-column layouts dominate trivia rounds. Progress maps transition from a vertical serpentine layout on mobile to an expanded split-column vista on wide tablets.

## Elevation & Depth

Visual hierarchy uses physical, low-relief tactile depth (2–3dp equivalent) reminiscent of carved stone and embossed medals:

1. **Base Layer (Ground)**: Deep matte obsidian (`#131012`) with a faint micro-grain texture simulating aged basalt.
2. **Layer 1 (Card & Tablet Surface)**: Dark burgundy-charcoal (`#1F1A1C`) raised via a 1px top border highlight (`rgba(255, 255, 255, 0.08)`) and a bottom drop shadow (`0 2px 4px rgba(0, 0, 0, 0.60)`).
3. **Layer 2 (Interactive Trivia Options & Action Plates)**: `#282225` with an engraved 1px border (`#44383D`) and a distinct downward shadow (`0 3px 6px rgba(0, 0, 0, 0.75)`).
4. **Layer 3 (Modals & Victory Sheets)**: `#1F1A1C` surrounded by a dual-line stroke: an inner 1px hairline (`#5A1827`) and an outer 1px bronze accent (`#C5A059` at 40% opacity), paired with a deep ambient shadow (`0 8px 24px rgba(0, 0, 0, 0.85)`).

Pressed states drop elevation to 0dp with an inset top shadow (`inset 0 2px 4px rgba(0, 0, 0, 0.8)`), reinforcing a satisfying stone-block depression.

## Shapes

The design system employs a soft, architectural roundedness factor (`1`). 

- Standard components (buttons, answer tiles, input fields) use `0.25rem` (4dp) corner radii, preserving the structured, chiseled silhouette of hand-cut masonry.
- Medium containers and modal sheets adopt `rounded-lg` (`0.5rem` / 8dp) for slightly gentler transitions without softening into casual bubbly forms.
- Progression nodes and rank seals utilize pure circular forms (`rounded-full`), echoing cast Roman coins, imperial medallions, and signet wax seals.

## Components

### Trivia Answer Tiles & Buttons
- **Default Answer Tile**: Full-width block with `0.25rem` radius, `#282225` fill, 1px border of `#44383D`, and text set in `title-md` (`#F3EFE6`). Height is fixed at a minimum of 56dp for touch accuracy.
- **Selected State**: Border shifts to `#C5A059` (Gold) with an inner glow (`0 0 8px rgba(197, 160, 89, 0.25)`).
- **Correct State**: Border and accent glow transition to Laurel Green (`#2E6F40`), surface tint shifts toward dark jade.
- **Incorrect State**: Border and accent glow transition to Crimson Rust (`#8C2222`), accompanied by a sharp haptic pulse.
- **Primary CTA Button**: Solid Tyrian Burgundy (`#5A1827`) with gold inscriptional caps (`label-lg`), featuring a 1px antique bronze perimeter frame.

### Progression Nodes & Laurel Milestones
- **Nodes**: Circular coin-like buttons (48dp diameter) with a stepped bronze-to-gold border. Locked nodes are recessed basalt (`#1A1618`); active nodes pulse with a subtle gold perimeter; mastered nodes display an embossed laurel wreath icon in `#C5A059`.
- **Connector Path**: 3dp dashed or solid chiseled channel set in `#2E2421`.

### Chips & Era Filters
- Compact 32dp tags with `0.25rem` corners. Inactive tags feature dark parchment backgrounds with hairline borders; active tags use deep Tyrian purple fills with gold serif text.

### Cards & Question Slates
- Question slates replicate an archival tablet. Enclosed in a dark stone frame with a 1px incised inner border (`rgba(255, 255, 255, 0.05)`). Header typography in `headline-sm` designates the historical era, followed by `body-lg` question stems with generous line-height.

### Inputs & Search Bars
- Recessed baseline wells with dark stone fills (`#181416`), interior drop shadow, and a 1px border that shifts from muted bronze to antique gold when focused.