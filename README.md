# ⭐ SkTabCompleter

An addon for the Skript Minecraft plugin that adds simple tab completion for commands created with the Skript language. For Minecraft server (Spigot/Paper).

> **Note:** This plugin require [Skript](https://github.com/SkriptLang/Skript/releases) to Work.  
> For suggestions or bug reports, contact me on Telegram: [@xoxRyze](https://t.me/xoxRyze)

## 🔎 Config.yml spoiler

<details>
<summary>📂 Config.yml</summary>

```yml
# PLACEHOLDERS FOR ARGUMENTS
#
# PLAYERS_LIST = list of all online players
# WORLDS_LIST = list of all worlds in the server
# MATERIAL_LIST = list of all materials
# ENCHANTMENTS_LIST = list of all enchantments
# POTION_EFFECTS_LIST = list of all potion effects
# ENTITY_TYPES_LIST = list of all entity types
#

commands:
  example:
    permission: "examplecommand.tabcomplete"
    arg-1:
      - "get"
      - "give"
    arg-2:
      - MATERIAL_LIST
    arg-3:
      - PLAYERS_LIST
    arg-4:
      - ENCHANTMENTS_LIST

  example_2:
    permission: "" # EMPTY = NO PERMISSION
    arg-1:
      - "selworld"
    arg-2:
      - WORLDS_LIST
```
</details>

## 🧾 Commands

* `/sktabcompleter reload` (Reload configuration file)
* `/sktabcompleter commands` (See configured commands)
 
## 🔐 Permissions

* `/sktabcompleter reload` » `sktabcompleter.command.reload`
* `/sktabcompleter commands` » `sktabcompleter.command.commands`
 
##
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge\&logo=java\&logoColor=white)
![Spigot/Bukkit](https://img.shields.io/badge/SpigotMC-F47B20?style=for-the-badge\&logo=apachemaven\&logoColor=white)
