package it.xoxryze.skTabCompleter.api.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permission {;

    private static final String PERMISSION_BASE = "sktabcompleter.";

    public static boolean hasCMDPermission(CommandSender sender, String permission) {

        if (!(sender instanceof Player)) return true;

        return hasPermission((Player) sender, "command." + permission);
    }

    public static boolean hasPermission(Player player, String permission) {
        return player != null && player.hasPermission(PERMISSION_BASE + permission);
    }
}