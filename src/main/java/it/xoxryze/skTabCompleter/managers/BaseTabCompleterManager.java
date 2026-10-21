package it.xoxryze.skTabCompleter.managers;

import it.xoxryze.skTabCompleter.api.managers.ConfigManager;
import it.xoxryze.skTabCompleter.api.managers.TabCompleterManager;
import it.xoxryze.skTabCompleter.cache.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseTabCompleterManager implements TabCompleterManager {

    private final ConfigManager configManager;

    public BaseTabCompleterManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public @Nullable List<String> getCompletions(@NotNull CommandSender sender,
                                                 @NotNull String cmdName,
                                                 @NotNull String[] args) {

        if (!configManager.commandExist(cmdName)) return Collections.emptyList();
        if (!(sender instanceof Player player)) return Collections.emptyList();

        String permission = configManager.getPermission(cmdName);
        if (!permission.isEmpty() && !player.hasPermission(permission)) return Collections.emptyList();

        ConfigurationSection section = configManager.getSection(cmdName);
        if (section == null) return Collections.emptyList();

        String argKey = "arg-" + (args.length > 0 ? args.length : 1);
        if (!section.contains(argKey)) return Collections.emptyList();

        List<String> configured = new ArrayList<>(section.getStringList(argKey));
        List<String> completions = new ArrayList<>();

        if (configured.remove("OFFLINE_PLAYERS_LIST")) {
            Arrays.stream(Bukkit.getOfflinePlayers()).toList().forEach(o -> completions.add(o.getName()));
        }

        if (configured.remove("PLAYERS_LIST")) {
            Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }

        if (configured.remove("WORLDS_LIST")) {
            Bukkit.getWorlds().forEach(w -> completions.add(w.getName()));
        }

        if (configured.remove("MATERIAL_LIST")) {
            completions.addAll(CacheManager.getMaterials());
        }

        if (configured.remove("ENCHANTMENTS_LIST")) {
            completions.addAll(CacheManager.getEnchantments());
        }

        if (configured.remove("POTION_EFFECTS_LIST")) {
            completions.addAll(CacheManager.getPotionEffects());
        }

        if (configured.remove("ENTITY_TYPES_LIST")) {
            completions.addAll(CacheManager.getEntityTypes());
        }

        if (configured.remove("SOUNDS_LIST")) {
            completions.addAll(CacheManager.getSounds());
        }

        if (configured.remove("PLUGINS_LIST")) {
            Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .map(Plugin::getName)
                    .forEach(completions::add);
        }

        if (configured.remove("ENABLED_PLUGINS_LIST")) {
            Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .filter(Plugin::isEnabled)
                    .map(Plugin::getName)
                    .forEach(completions::add);
        }

        if (configured.remove("DISABLED_PLUGINS_LIST")) {
            Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .filter(plugin -> !plugin.isEnabled())
                    .map(Plugin::getName)
                    .forEach(completions::add);
        }

        if (configured.remove("BANNED_PLAYERS_LIST")) {
            addOfflinePlayerNames(completions, Bukkit.getBannedPlayers());
        }

        if (configured.remove("WHITELISTED_PLAYERS_LIST")) {
            addOfflinePlayerNames(completions, Bukkit.getWhitelistedPlayers());
        }

        if (configured.remove("OPS_PLAYERS_LIST")) {
            addOfflinePlayerNames(completions, Bukkit.getOperators());
        }

        List<String> placeholders = configured.stream()
                .filter(s -> s.startsWith("PLACEHOLDER:"))
                .toList();
        configured.removeAll(placeholders);

        if (BasePlaceholderManager.isEnabled()) {
            for (String entry : placeholders) {
                String rawPlaceholder = entry.substring("PLACEHOLDER:".length());
                String expanded = BasePlaceholderManager.getPlaceholder(rawPlaceholder, player);
                if (expanded.contains(",")) {
                    completions.addAll(Arrays.asList(expanded.split(",")));
                } else {
                    completions.add(expanded);
                }
            }
        }

        completions.addAll(configured);

        String currentArg = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentArg))
                .toList();
    }

    private static void addOfflinePlayerNames(List<String> completions, Collection<? extends OfflinePlayer> players) {
        players.stream()
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .forEach(completions::add);
    }
}