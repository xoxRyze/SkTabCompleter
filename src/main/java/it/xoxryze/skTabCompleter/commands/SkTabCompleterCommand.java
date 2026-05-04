package it.xoxryze.skTabCompleter.commands;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.utils.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SkTabCompleterCommand implements CommandExecutor {

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
                if (!Permission.hasCMDPermission(sender, "reload")) {
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
                if (!Permission.hasCMDPermission(sender, "commands")) {
                    sender.sendMessage(Component.text("§cYou don't have the permission to do it!"));
                    return true;
                }
                if (main.getConfigManager().getCommands().isEmpty()) {
                    sender.sendMessage(Component.text("§cThere is no command configured!"));
                    return true;
                }
                sender.sendMessage(Component.empty());
                sender.sendMessage(Component.text(String.format(
                        " §b§lConfigured Commands §7(%s)", main.getConfigManager().getCommands().size())));
                for (String cmd : main.getConfigManager().getCommands()) {

                    StringBuilder hover = new StringBuilder("§7/" + cmd);
                    org.bukkit.configuration.ConfigurationSection section = main.getConfigManager().getSection(cmd);
                    if (section != null) {
                        int argIndex = 1;
                        while (section.contains("arg-" + argIndex)) {
                            java.util.List<String> argValues = section.getStringList("arg-" + argIndex);
                            if (!argValues.isEmpty()) {
                                hover.append(" §7<§f").append(String.join("§7 | §f", argValues)).append("§7>");
                            }
                            argIndex++;
                        }
                    }
                    sender.sendMessage(Component.text(" §8» §7/" + cmd)
                            .hoverEvent(HoverEvent.showText(Component.text(hover.toString()))));
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
                .hoverEvent(HoverEvent.showText(Component.text("§7View configured commands"))));
        player.sendMessage(Component.text(" /sktabcompleter reload", color)
                .hoverEvent(HoverEvent.showText(Component.text("§7Reload configurations"))));
        player.sendMessage(Component.empty());
        return;
    }

}
