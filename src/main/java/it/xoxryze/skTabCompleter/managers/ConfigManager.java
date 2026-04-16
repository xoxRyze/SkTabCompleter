package it.xoxryze.skTabCompleter.managers;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final SkTabCompleter main;

    private final List<String> commands = new ArrayList<>();

    public ConfigManager(SkTabCompleter main) {
        this.main = main;
    }

    public void addCommand(String name) {
        commands.add(name);
    }

    public void clearCommands() {
        commands.clear();
    }

    public boolean commandExist(String name) {
        return commands.contains(name);
    }

    public ConfigurationSection getSection(String command) {
        return main.getConfig().getConfigurationSection("commands." + command.toLowerCase());
    }

    public String getPermission(String command) {
        return main.getConfig().getString("commands." + command.toLowerCase() + ".permission", "");
    }

    public List<String> getCommands() {
        return commands;
    }
}
