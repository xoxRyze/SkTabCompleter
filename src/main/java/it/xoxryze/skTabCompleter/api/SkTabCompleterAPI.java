package it.xoxryze.skTabCompleter.api;

import it.xoxryze.skTabCompleter.api.managers.ConfigManager;
import it.xoxryze.skTabCompleter.api.managers.PlaceholderManager;
import it.xoxryze.skTabCompleter.api.managers.TabCompleterManager;

public interface SkTabCompleterAPI {
    void reload();

    void registerTabCompleterOnCommand(String cmdName);

    ConfigManager getConfigManager();

    TabCompleterManager getTabCompleterManager();

    PlaceholderManager getPlaceholderManager();
}
