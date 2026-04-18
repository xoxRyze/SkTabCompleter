package it.xoxryze.skTabCompleter.commands;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HostnameVerifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkTabCompleterCommand implements CommandExecutor, TabCompleter {

    private final SkTabCompleter main;

    public SkTabCompleterCommand(SkTabCompleter main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (args.length != 1) {
           sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("sktabcompleter.command.reload")) {
                    sender.sendMessage(Component.text("§cYou don't have the permission to do it!"));
                    return true;
                }
                long now = System.currentTimeMillis();
                main.reload();
                long end = System.currentTimeMillis();
                sender.sendMessage(Component.text(
                        String.format("§aConfigurations reloaded in §e%s ms§a!", end-now)));
                break;
            case "commands":
                if (!sender.hasPermission("sktabcompleter.command.commands")) {
                    sender.sendMessage(Component.text("§cYou don't have the permission to do it!"));
                    return true;
                }
                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text(" §b§lConfigured Commands"));
                for (String cmd : main.getConfigManager().getCommands()) {
                    if (cmd.isEmpty()) {
                        sender.sendMessage(Component.text("§cNessun comando configurato!"));
                        break;
                    }
                    sender.sendMessage(Component.text(" §8» §7/" + cmd));
                }
                sender.sendMessage(Component.empty());
                break;
            default:
                sender.sendMessage(Component.text("§cCommand not found! Use /sktabcompleter for information"));
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender player) {
        TextColor color = TextColor.color(24, 187, 229);
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("\uD83D\uDEC8 /sktabcompleter", color)
                .hoverEvent(HoverEvent.showText(Component.text("§7Click to view SkTabCompleter GitGub page")))
                .clickEvent(ClickEvent.openUrl("https://github.com/xoxRyze/SkTabCompleter")));
        player.sendMessage(Component.text(" /sktabcompleter commands", color)
                .hoverEvent(HoverEvent.showText(Component.text("§7Visualizza i comandi configurati"))));
        player.sendMessage(Component.text(" /sktabcompleter reload", color)
                .hoverEvent(HoverEvent.showText(Component.text("§7Ricarica le configurazioni"))));
        player.sendMessage(Component.empty());
        return;
    }

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
