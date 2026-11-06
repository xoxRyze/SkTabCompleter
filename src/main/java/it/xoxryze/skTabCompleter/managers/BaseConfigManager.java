package it.xoxryze.skTabCompleter.managers;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.api.managers.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class BaseConfigManager implements ConfigManager {

    private final SkTabCompleter main;

    private final List<String> commands = new ArrayList<>();

    private final Map<String, String> names = new HashMap<>();

    private final Map<String, List<String>> aliasesByCommand = new HashMap<>();

    public BaseConfigManager(SkTabCompleter main) {
        this.main = main;
    }

    @Override
    public void addCommand(String key) {
        if (key == null || key.trim().isEmpty()) {
            main.getLogger().warning("Attempted to register an empty command name.");
            return;
        }

        String normalizedKey = key.trim();
        String lower = normalizedKey.toLowerCase(Locale.ROOT);

        if (commands.contains(normalizedKey)) {
            main.getLogger().warning("Command '" + normalizedKey + "' is duplicated.");
            return;
        }

        String owner = names.get(lower);
        if (owner != null) {
            if (owner.equalsIgnoreCase(normalizedKey)) {
                main.getLogger().warning("Command '" + normalizedKey + "' is duplicated.");
                return;
            }

            List<String> previousAliases = aliasesByCommand.get(owner);
            if (previousAliases != null) {
                previousAliases.remove(lower);
            }
            main.getLogger().warning("'" + lower + "' is an alias of '" + owner + " but is a command.");
        }

        names.put(lower, normalizedKey);
        commands.add(normalizedKey);

        List<String> accepted = new ArrayList<>();
        aliasesByCommand.put(normalizedKey, accepted);

        ConfigurationSection section = main.getConfig().getConfigurationSection("commands." + normalizedKey);
        if (section == null) return;

        for (String raw : section.getStringList("aliases")) {
            if (raw == null) continue;
            String alias = raw.trim().toLowerCase(Locale.ROOT);
            if (alias.startsWith("/")) alias = alias.substring(1);
            if (alias.isEmpty()) continue;

            String existing = names.get(alias);
            if (existing != null) {
                if (!existing.equalsIgnoreCase(normalizedKey)) {
                    main.getLogger().warning("Alias '" + alias + "' of '" + normalizedKey
                            + "' is already used by '" + existing + "', ignoring it.");
                }
                continue;
            }

            names.put(alias, normalizedKey);
            accepted.add(alias);
        }
    }

    @Override
    public void clearCommands() {
        commands.clear();
        names.clear();
        aliasesByCommand.clear();
    }

    @Override
    public boolean commandExist(String name) {
        return names.containsKey(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public String resolveCommand(String name) {
        return names.get(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public ConfigurationSection getSection(String command) {
        String key = resolveCommand(command);
        if (key == null) return null;
        return main.getConfig().getConfigurationSection("commands." + key);
    }

    @Override
    public String getPermission(String command) {
        String key = resolveCommand(command);
        if (key == null) return "";
        return main.getConfig().getString("commands." + key + ".permission", "");
    }

    @Override
    public List<String> getCommands() {
        return commands;
    }

    @Override
    public List<String> getAliases(String command) {
        String key = resolveCommand(command);
        if (key == null) return Collections.emptyList();
        return Collections.unmodifiableList(aliasesByCommand.getOrDefault(key, Collections.emptyList()));
    }

    @Override
    public Set<String> getAllNames() {
        return Collections.unmodifiableSet(names.keySet());
    }
}
