package it.xoxryze.skTabCompleter.api;

import it.xoxryze.skTabCompleter.managers.ConfigManager;
import it.xoxryze.skTabCompleter.managers.TabCompleterManager;

public interface SkTabCompleterAPI {
    void reload();

    void registerTabCompleterOnCommand(String cmdName);

    ConfigManager getConfigManager();

    TabCompleterManager getTabCompleterManager();
}
