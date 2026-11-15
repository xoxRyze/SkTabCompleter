package it.xoxryze.skTabCompleter.managers;

import it.xoxryze.skTabCompleter.api.managers.ConfigManager;
import it.xoxryze.skTabCompleter.api.managers.TabCompleterManager;
import it.xoxryze.skTabCompleter.api.data.PlaceholdersData;
import it.xoxryze.skTabCompleter.api.utils.SkriptVariableResolver;
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
            addOfflinePlayerNames(completions, Arrays.asList(Bukkit.getOfflinePlayers()));
        }

        if (configured.remove("PLAYERS_LIST")) {
            Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }

        if (configured.remove("WORLDS_LIST")) {
            Bukkit.getWorlds().forEach(w -> completions.add(w.getName()));
        }

        if (configured.remove("MATERIAL_LIST")) {
            completions.addAll(PlaceholdersData.getMaterials());
        }

        if (configured.remove("ENCHANTMENTS_LIST")) {
            completions.addAll(PlaceholdersData.getEnchantments());
        }

        if (configured.remove("POTION_EFFECTS_LIST")) {
            completions.addAll(PlaceholdersData.getPotionEffects());
        }

        if (configured.remove("ENTITY_TYPES_LIST")) {
            completions.addAll(PlaceholdersData.getEntityTypes());
        }

        if (configured.remove("SOUNDS_LIST")) {
            completions.addAll(PlaceholdersData.getSounds());
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

        for (String entry : placeholders) {
            String rawPlaceholder = entry.substring("PLACEHOLDER:".length());
            if (!BasePlaceholderManager.isEnabled() && !SkriptVariableResolver.contains(rawPlaceholder)) continue;
            addSplits(completions, BasePlaceholderManager.getPlaceholder(rawPlaceholder, player));
        }

        List<String> skriptEntries = configured.stream().filter(SkriptVariableResolver::contains).toList();
        configured.removeAll(skriptEntries);

        for (String entry : skriptEntries) {
            addSplits(completions, BasePlaceholderManager.getPlaceholder(entry, player));
        }

        completions.addAll(configured);

        String currentArg = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentArg))
                .toList();
    }

    private static void addSplits(List<String> completions, String expanded) {
        for (String part : expanded.split(",")) {
            String value = part.trim();
            if (!value.isEmpty()) completions.add(value);
        }
    }

    private static void addOfflinePlayerNames(List<String> completions, Collection<? extends OfflinePlayer> players) {
        players.stream().map(OfflinePlayer::getName).filter(Objects::nonNull).forEach(completions::add);
    }
}