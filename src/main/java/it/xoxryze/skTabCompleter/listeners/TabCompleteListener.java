package it.xoxryze.skTabCompleter.listeners;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.api.managers.ConfigManager;
import it.xoxryze.skTabCompleter.api.managers.TabCompleterManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TabCompleteListener implements Listener, TabCompleter {

    private final SkTabCompleter main;
    private final TabCompleterManager tabCompleterManager;
    private final ConfigManager configManager;

    public TabCompleteListener(SkTabCompleter main) {
        this.main = main;
        this.tabCompleterManager = main.getTabCompleterManager();
        this.configManager = main.getConfigManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTabComplete(TabCompleteEvent event) {
        String buffer = event.getBuffer();
        if (!buffer.startsWith("/")) return;

        String[] parts = buffer.substring(1).split(" ", -1);

        String cmdName = parts[0].toLowerCase();
        if (cmdName.contains(":")) cmdName = cmdName.substring(cmdName.indexOf(':') + 1);

        if (!configManager.commandExist(cmdName)) return;

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        return tabCompleterManager.getCompletions(sender, command.getName().toLowerCase(), args);
    }

}