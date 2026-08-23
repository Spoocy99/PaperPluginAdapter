# PaperAdapter

Utility classes to create plugins compatible with both Paper and Spigot.


## Modules

| Module                 | Artifact                                  | Purpose                                                                                                      |
|------------------------|-------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| `plugin-adapter`       | `dev.spoocy.adapter:plugin-adapter`       | Core adapter layer (plugin lifecycle helpers, config, logging, scheduling, message integrations, utilities). |
| `compatibility`        | `dev.spoocy.adapter:compatibility`        | Shared compatibility contracts and base abstractions for server-specific implementations.                    |
| `compatibility-paper`  | `dev.spoocy.adapter:compatibility-paper`  | Paper-specific compatibility implementations.                                                                |
| `compatibility-spigot` | `dev.spoocy.adapter:compatibility-spigot` | Spigot-specific compatibility implementations.                                                               |
| `gui-api`              | `dev.spoocy.adapter:gui-api`              | Inventory/GUI API for plugin user interfaces.                                                                |
| `message-wrapper`      | `dev.spoocy.adapter:message-wrapper`      | Message and localization wrapper types built around Adventure components.                                    |
| `reflection-api`       | `dev.spoocy.adapter:reflection-api`       | Reflection utilities.                                                                                        |

Make sure to shade and relocate the modules into your plugin.

## Requirements
- Java 11

