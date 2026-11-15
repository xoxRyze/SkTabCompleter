package it.xoxryze.skTabCompleter.commands;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.api.utils.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkTabCompleterCommand implements CommandExecutor {

    private final SkTabCompleter main;
    private MiniMessage miniMessage = MiniMessage.miniMessage();

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
                    sender.sendMessage(miniMessage.deserialize("<red>You don't have the permission to do it!"));
                    return true;
                }
                long now = System.currentTimeMillis();
                main.reload();
                long end = System.currentTimeMillis();
                sender.sendMessage(miniMessage.deserialize(
                        String.format("<green>Configurations reloaded in <yellow>%s ms<green>!", end - now)));
                break;
            case "commands":
                if (!Permission.hasCMDPermission(sender, "commands")) {
                    sender.sendMessage(miniMessage.deserialize("<red>You don't have the permission to do it!"));
                    return true;
                }
                if (main.getConfigManager().getCommands().isEmpty()) {
                    sender.sendMessage(miniMessage.deserialize("<red>There is no command configured!"));
                    return true;
                }
                sender.sendMessage(Component.empty());
                sender.sendMessage(miniMessage.deserialize(String.format(
                        " <aqua><bold>Configured Commands <gray>(%s)", main.getConfigManager().getCommands().size())));
                for (String cmd : main.getConfigManager().getCommands()) {

                    StringBuilder hover = new StringBuilder("<gray>/" + cmd);
                    ConfigurationSection section = main.getConfigManager().getSection(cmd);
                    if (section != null) {
                        int argIndex = 1;
                        while (section.contains("arg-" + argIndex)) {
                            java.util.List<String> argValues = section.getStringList("arg-" + argIndex);
                            if (!argValues.isEmpty()) {
                                hover.append(" <gray><white><").append(String.join(" <gray><white> | <white>", argValues)).append("> <gray>");
                            }
                            argIndex++;
                        }
                    }
                    List<String> aliases = main.getConfigManager().getAliases(cmd);
                    if (!aliases.isEmpty()) {
                        hover.append("\n<gray>Aliases: <white>/").append(String.join("<gray>, <white>/", aliases));
                    }
                    sender.sendMessage(miniMessage.deserialize(" <gray>» <white>/" + cmd)
                            .hoverEvent(HoverEvent.showText(miniMessage.deserialize(hover.toString()))));
                }
                sender.sendMessage(Component.empty());
                break;
            default:
                sender.sendMessage(miniMessage.deserialize("<red>Command not found! Use /sktabcompleter for information"));
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender player) {
        TextColor color = TextColor.color(24, 187, 229);
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("\uD83D\uDEC8 /sktabcompleter", color)
                .hoverEvent(HoverEvent.showText(miniMessage.deserialize("<gray>Click to view SkTabCompleter GitHub page")))
                .clickEvent(ClickEvent.openUrl("https://github.com/xoxRyze/SkTabCompleter")));
        player.sendMessage(Component.text(" /sktabcompleter commands", color)
                .hoverEvent(HoverEvent.showText(miniMessage.deserialize("<gray>View configured commands"))));
        player.sendMessage(Component.text(" /sktabcompleter reload", color)
                .hoverEvent(HoverEvent.showText(miniMessage.deserialize("<gray>Reload configurations"))));
        player.sendMessage(Component.empty());
        return;
    }

}
