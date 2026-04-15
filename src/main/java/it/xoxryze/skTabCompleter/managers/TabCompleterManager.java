package it.xoxryze.skTabCompleter.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleterManager implements TabCompleter {

    private final ConfigManager configManager;

    public TabCompleterManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        return getCompletions(sender, command.getName().toLowerCase(), args);
    }

    public @Nullable List<String> getCompletions(@NotNull CommandSender sender,
                                                 @NotNull String cmdName,
                                                 @NotNull String[] args) {

        if (!configManager.commandExist(cmdName)) return Collections.emptyList();

        if (!(sender instanceof Player player)) return Collections.emptyList();

        String permission = configManager.getPermission(cmdName);
        if (!permission.isEmpty() && !player.hasPermission(permission)) return Collections.emptyList();

        ConfigurationSection section = configManager.getSection(cmdName);
        if (section == null) return Collections.emptyList();

        int argIndex = args.length > 0 ? args.length : 1;
        String argKey = "arg-" + argIndex;

        if (!section.contains(argKey)) return Collections.emptyList();

        List<String> configured = new ArrayList<>(section.getStringList(argKey));
        List<String> completions = new ArrayList<>();

        if (configured.remove("PLAYERS_LIST")) {
            Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }

        if (configured.remove("WORLDS_LIST")) {
            Bukkit.getWorlds().forEach(world -> completions.add(world.getName()));
        }

        if (configured.remove("MATERIAL_LIST")) {
            Arrays.stream(Material.values())
                    .filter(m -> !m.isLegacy())
                    .forEach(m -> completions.add(m.name().toLowerCase()));
        }

        completions.addAll(configured);

        String currentArg = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentArg))
                .collect(Collectors.toList());
    }
}
