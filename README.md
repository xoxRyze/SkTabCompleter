# ⭐ SkTabCompleter

An addon for the Skript Minecraft plugin that adds simple tab completion for commands created with the Skript language. For Minecraft server (Spigot/Paper).

<b>Version:</b> 1.21.1+

> **Note:** This plugin require [Skript](https://github.com/SkriptLang/Skript/releases) to Work.  
> For suggestions or bug reports, contact me on [Telegram](https://t.me/xoxRyze) or [Discord](https://discord.com/users/746303863975575563).

## 🔎 Config.yml spoiler

<details>
<summary>📂 Config.yml</summary>

```yml
# PLACEHOLDERS FOR ARGUMENTS
#
# OFFLINE_PLAYERS_LIST = list of all online and offline players
# PLAYERS_LIST = list of all online players
# WORLDS_LIST = list of all worlds in the server
# MATERIAL_LIST = list of all materials
# ENCHANTMENTS_LIST = list of all enchantments
# POTION_EFFECTS_LIST = list of all potion effects
# ENTITY_TYPES_LIST = list of all entity types
# PLACEHOLDER:%<placeholder>% = a PlaceholderAPI placeholder for the player executing the command
#                      (requires PlaceholderAPI 2.12.2+). Example: "PLACEHOLDER:%player_name%"
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
    arg-3:
      - OFFLINE_PLAYERS_LIST

  example_3:
    permission: ""
    arg-1:
      - "PLACEHOLDER:%team_name%"
      - "@e"
```
</details>

## 🧾 Commands

* `/sktabcompleter reload` (Reload configuration file)
* `/sktabcompleter commands` (See configured commands)
 
## 🔐 Permissions

* `/sktabcompleter reload` » `sktabcompleter.command.reload`
* `/sktabcompleter commands` » `sktabcompleter.command.commands`
 
##   
[![bStats Graph](https://bstats.org/signatures/bukkit/SkTabCompleter.svg)](https://bstats.org/plugin/bukkit/SkTabCompleter/30817)

##
![Downloads](https://img.shields.io/github/downloads/xoxRyze/SkTabCompleter/total?style=for-the-badge)

