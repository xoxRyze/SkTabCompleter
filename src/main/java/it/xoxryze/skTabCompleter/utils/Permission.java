package it.xoxryze.skTabCompleter.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permission {;

    private static final String PERMISSION_BASE = "sktabcompleter.";
    private static final String COMMAND_PERMISSION = PERMISSION_BASE + "command.";

    public static boolean hasCMDPermission(CommandSender sender, String permission) {

        if (!(sender instanceof Player)) return true;

        return hasPermission((Player) sender, COMMAND_PERMISSION + permission);
    }

    private static boolean hasPermission(Player player, String permission) {
        return player != null && player.hasPermission(permission);
    }
}