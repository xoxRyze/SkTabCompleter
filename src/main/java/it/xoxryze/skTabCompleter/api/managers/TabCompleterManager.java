package it.xoxryze.skTabCompleter.api.managers;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TabCompleterManager {
    @Nullable List<String> getCompletions(@NotNull CommandSender sender,
                                          @NotNull String cmdName,
                                          @NotNull String[] args);
}
