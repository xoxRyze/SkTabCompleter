package it.xoxryze.skTabCompleter.commands;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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

        if (sender instanceof Player player && !player.hasPermission("sktabcompleter.command.reload")) {
            player.sendMessage(Component.text("§cYou don't have the permission!"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(Component.empty());
            sender.sendMessage(Component.text(
                    String.format(" §b§lSkTabCompleter§b v%s by RyZeeTheBest", main.getPluginMeta().getVersion())).hoverEvent(
                    HoverEvent.showText(Component.text("§7Click to get my GitHub account!")))
                    .clickEvent(ClickEvent.openUrl("https://github.com/xoxRyze")));
            sender.sendMessage(Component.text("§b /sktabcompleter commands").hoverEvent(
                    HoverEvent.showText(Component.text("§7View configured commands"))
            ));
            sender.sendMessage(Component.text(
                    "§b /sktabcompleter reload").hoverEvent(HoverEvent.showText(
                    Component.text("§7Reload configuration file")
            )));
            sender.sendMessage(Component.empty());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                long now = System.currentTimeMillis();
                main.reload();
                long end = System.currentTimeMillis();
                sender.sendMessage(Component.text(
                        String.format("§aConfigurations reloaded in §e%s ms§a!", end-now)));
                break;
            case "commands":
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
