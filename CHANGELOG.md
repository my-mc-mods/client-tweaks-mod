# Unreleased

# 0.1.0 - 2026-11-07

First release of the client-side mod, for Minecraft 1.21.1, 1.21.11, 26.1.x, 26.2 and 26.3 on Fabric and NeoForge,
plus 26.4 snapshots on Fabric.

## Added

- **Numeric ping** — shows the latency next to the connection bars in the player list, with an option to hide the bars.
  The color comes from the first matching rule; rules are expressions such as `> 600`, `>= 0` or `*`, each with its own
  color and opacity.
- **Client ping refresh** — sends its own ping requests at a 1–29 second interval and uses the measured round-trip time
  for your own entry, so it stays current instead of waiting for the server to update it.
- **Unfocused FPS limit** — caps the frame rate while the window is unfocused (10 FPS by default, 0 disables) and
  restores your normal limit when focus returns.
- **Unfocused mute** — mutes all game audio while the window is unfocused.
- **Hide fire overlay** — hides the first-person fire overlay while you have Fire Resistance or are in Creative mode.
- **Remove Realms button** — removes the Realms button from the title screen and moves the remaining buttons up to fill
  the gap.
- **Hide insecure warning toast** — hides the toast shown when joining a server that is not secure.
- **Language code filter** — lets the language selection search match language codes such as `en_US` or `enUS`
  (Minecraft 1.21.11 and newer).
- **Config screen** — built on Cloth Config and reachable from Mod Menu on Fabric and from the mod list on NeoForge.
  Without Cloth Config it opens a notice with buttons to reload the config or open its folder instead.
- **Per-mixin switches** — the config screen lists every applied mixin and can disable individual ones; changes take
  effect after a restart.

Settings are stored in `config/gizmoclienttweaks.json`. Cloth Config and Mod Menu are optional; the mod works without
them.