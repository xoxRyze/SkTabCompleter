package it.xoxryze.skTabCompleter;

import ch.njol.skript.Skript;
import ch.njol.skript.bstats.bukkit.Metrics;
import it.xoxryze.skTabCompleter.commands.SkTabCompleterCommand;
import it.xoxryze.skTabCompleter.managers.ConfigManager;
import it.xoxryze.skTabCompleter.listeners.TabCompleteListener;
import it.xoxryze.skTabCompleter.managers.TabCompleterManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkTabCompleter extends JavaPlugin {

    private ConfigManager configManager;
    private TabCompleterManager tabCompleterManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!checkSkript()) return;
        initManagers();
        initListeners();
        initCommands();
        initMetrics();
        SkTabCompleterCommand skTabCompleterCommand = new SkTabCompleterCommand(this);
        getCommand("sktabcompleter").setExecutor(skTabCompleterCommand);
        getCommand("sktabcompleter").setTabCompleter(skTabCompleterCommand);
        for (String cmdName : configManager.getCommands()) {
            registerTabCompleterOnCommand(cmdName);
        }
        getLogger().info("SkTabCompleter has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkTabCompleter has been disabled.");
    }

    private void initCommands() {
        getLogger().info("Initializing commands...");
        ConfigurationSection section = getConfig().getConfigurationSection("commands");
        if (section != null) {
            for (String commandName : section.getKeys(false)) {
                if (Skript.getInstance().getCommand(commandName) != null) {
                    configManager.addCommand(commandName);
                } else {
                    getLogger().warning("Skript command '" + commandName + "' not found.");
                }
            }
        }
    }

    private void initListeners() {
        getLogger().info("Initializing listeners...");
        getServer().getPluginManager().registerEvents(new TabCompleteListener(tabCompleterManager, configManager), this);
    }

    private void initManagers() {
        getLogger().info("Initializing managers...");
        configManager = new ConfigManager(this);
        tabCompleterManager = new TabCompleterManager(configManager);
    }

    private void initMetrics() {
        Metrics metrics = new Metrics(this, 30816);
    }

    public void reload() {
        reloadConfig();
        configManager.clearCommands();
        initCommands();
        for (String cmdName : configManager.getCommands()) {
            registerTabCompleterOnCommand(cmdName);
        }
    }

    private void registerTabCompleterOnCommand(String cmdName) {
        SimpleCommandMap map = (SimpleCommandMap) Bukkit.getCommandMap();
        Command cmd = map.getCommand(cmdName);
        if (cmd instanceof PluginCommand pc) {
            pc.setTabCompleter(tabCompleterManager);
        } else {
            getLogger().warning("Could not register tab completer for /" + cmdName);
        }
    }

    private boolean checkSkript() {
        getLogger().info("Checking for Skript presence...");
        if (getServer().getPluginManager().getPlugin("Skript") == null) {
            getLogger().severe("Skript not found! Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TabCompleterManager getTabCompleterManager() {
        return tabCompleterManager;
    }
}

