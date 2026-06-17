package it.xoxryze.skTabCompleter;

import ch.njol.skript.Skript;
import ch.njol.skript.bstats.bukkit.Metrics;
import it.xoxryze.skTabCompleter.commands.SkTabCompleterCommand;
import it.xoxryze.skTabCompleter.commands.SkTabCompleterTabCompleter;
import it.xoxryze.skTabCompleter.listeners.PlayerJoinListener;
import it.xoxryze.skTabCompleter.managers.ConfigManager;
import it.xoxryze.skTabCompleter.listeners.TabCompleteListener;
import it.xoxryze.skTabCompleter.managers.PlaceholderManager;
import it.xoxryze.skTabCompleter.managers.TabCompleterManager;
import it.xoxryze.skTabCompleter.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkTabCompleter extends JavaPlugin {

    private ConfigManager configManager;
    private TabCompleterManager tabCompleterManager;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!checkSkript()) return;
        initManagers();
        initListeners();
        Bukkit.getScheduler().runTaskLater(this, this::initCommands, 20L * 5);
        initMetrics();
        initUpdateChecker();
        if (checkPapi()) {
            initPapi();
        } else {
            getLogger().info("PlaceholderAPI not found. All the functions with placeholders will be disabled.");
        }
        getCommand("sktabcompleter").setExecutor(new SkTabCompleterCommand(this));
        getCommand("sktabcompleter").setTabCompleter(new SkTabCompleterTabCompleter());
        for (String cmdName : configManager.getCommands()) {
            registerTabCompleterOnCommand(cmdName);
        }
        getLogger().info("SkTabCompleter v" + getDescription().getVersion() + " has been enabled!");
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
            getLogger().info("Loaded " + configManager.getCommands().size() + " commands");
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
        Metrics metrics = new Metrics(this, 30817);
    }

    private void initUpdateChecker() {
        updateChecker = new UpdateChecker(this);
        updateChecker.check();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, updateChecker), this);
    }

    private boolean checkPapi() {
        getLogger().info("Checking for PlaceholderAPI presence...");
        return getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    private void initPapi() {
        PlaceholderManager.setEnabled(true);
        getLogger().info("PlaceholderAPI found! Placeholder functions have been enabled.");
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

}
