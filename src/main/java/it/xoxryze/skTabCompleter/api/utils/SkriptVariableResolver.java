package it.xoxryze.skTabCompleter.api.utils;

import ch.njol.skript.SkriptConfig;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.variables.Variables;
import it.xoxryze.skTabCompleter.managers.BasePlaceholderManager;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class SkriptVariableResolver {

    private static final Pattern VARIABLE = Pattern.compile("%\\{(.+?)\\}%");
    private static final Pattern PLAYER = Pattern.compile("%player%", Pattern.CASE_INSENSITIVE);

    private SkriptVariableResolver() {
    }

    public static boolean contains(String text) {
        return VARIABLE.matcher(text).find();
    }

    public static String resolve(String text, Player player) {
        Matcher matcher = VARIABLE.matcher(text);
        if (!matcher.find()) return text;

        StringBuilder result = new StringBuilder();
        do {
            matcher.appendReplacement(result, Matcher.quoteReplacement(lookup(matcher.group(1), player)));
        } while (matcher.find());
        matcher.appendTail(result);
        return result.toString();
    }

    private static String lookup(String rawName, Player player) {
        String name = rawName;
        if (player != null) {
            name = PLAYER.matcher(name).replaceAll(Matcher.quoteReplacement(playerKey(player)));
        }

        name = BasePlaceholderManager.getPlaceholder(name, player).toLowerCase(Locale.ENGLISH);

        Object value = Variables.getVariable(name, null, false);

        if (value == null) return "";

        if (value instanceof Map<?, ?> map) {
            return map.keySet().stream()
                    .filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(","));
        }
        return Classes.toString(value);
    }

    private static String playerKey(Player player) {
        return SkriptConfig.usePlayerUUIDsInVariableNames.value()
                ? player.getUniqueId().toString()
                : player.getName().toLowerCase(Locale.ENGLISH);
    }
}
