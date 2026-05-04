package it.xoxryze.skTabCompleter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkTabCompleterTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {

        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) return Collections.emptyList();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("commands");
            return completions;
        }

        return Collections.emptyList();
    }
}
