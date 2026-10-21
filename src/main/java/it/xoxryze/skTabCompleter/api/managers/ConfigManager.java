package it.xoxryze.skTabCompleter.api.managers;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public interface ConfigManager {
    void addCommand(String name);

    void clearCommands();

    boolean commandExist(String name);

    String resolveCommand(String name);

    ConfigurationSection getSection(String command);

    String getPermission(String command);

    List<String> getCommands();

    List<String> getAliases(String command);

    Set<String> getAllNames();
}
