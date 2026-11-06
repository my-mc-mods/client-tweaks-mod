# Gizmo's Client Tweaks

[English](./README.md) | [简体中文](./README_zh.md)

A client-side Minecraft mod that bundles small, configurable quality-of-life tweaks for vanilla gameplay.

## Features

### Ping and player list

- **Numeric Ping** — shows the actual latency value (for example
  `42 ms`) next to the connection bars in the player list, optionally hiding the bars. The color comes from the first
  rule that matches.
- **Client Ping Refresh** — sends its own ping requests at a configurable interval and uses the measured round-trip time
  for your own entry in the player list, so your latency stays current instead of waiting for the server's update.
  Disabled by default; set the interval to 1–29 seconds to enable it.

### Window focus

- **Unfocused FPS Limit** — caps the frame rate while the game window is unfocused (10 FPS by default) and restores your
  normal frame rate limit when the window regains focus.
- **Unfocused Mute** — mutes all game audio while the window is unfocused.

### Interface

- **Hide Fire Overlay** — hides the first-person fire overlay when you have Fire Resistance or are in Creative mode,
  since the flames are only visual noise in those cases.
- **Remove Realms Button** — removes the Realms button from the title screen and moves the remaining buttons up to fill
  the gap.
- **Hide Insecure Warning Toast** — hides the warning toast shown when you join a server that is not secure.
- **Language Code Filter** — lets the language selection search match language codes such as `en_US` or `enUS`
  (Minecraft 1.21.11 and newer).

## Contributing

Issues and pull requests are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) first to learn about the commit
message conventions, PR guidelines, and the policy on AI-assisted contributions.

## License

This project is licensed under the [MIT License](LICENSE.txt).

The source code in this repository is fully MIT-licensed. Official releases, however, may include an additional icon
marked "All Rights Reserved" (ARR). That icon is not covered by the MIT license, so releases containing it may not be
redistributed without permission. This does not affect the project's license. The icon is not part of this repository,
and builds made from this repository do not include it.