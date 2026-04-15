package it.xoxryze.skTabCompleter.listeners;

import it.xoxryze.skTabCompleter.managers.ConfigManager;
import it.xoxryze.skTabCompleter.managers.TabCompleterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener implements Listener {

    private final TabCompleterManager tabCompleterManager;
    private final ConfigManager configManager;

    public TabCompleteListener(TabCompleterManager tabCompleterManager, ConfigManager configManager) {
        this.tabCompleterManager = tabCompleterManager;
        this.configManager = configManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTabComplete(TabCompleteEvent event) {
        String command = event.getEventName();
        if (!configManager.commandExist(command.toLowerCase())) return;
        String buffer = event.getBuffer();

        if (!buffer.startsWith("/")) return;

        String[] parts = buffer.substring(1).split(" ", -1);
        String cmdName = parts[0].toLowerCase();

        if (cmdName.contains(":")) {
            cmdName = cmdName.substring(cmdName.indexOf(':') + 1);
        }

        int argsCount = parts.length - 1;
        String[] args = new String[argsCount > 0 ? argsCount : 1];
        if (argsCount > 0) {
            System.arraycopy(parts, 1, args, 0, argsCount);
        } else {
            args[0] = "";
        }

        List<String> completions = tabCompleterManager.getCompletions(event.getSender(), cmdName, args);
        if (completions != null) {
            event.setCompletions(completions);
        }
    }
}

