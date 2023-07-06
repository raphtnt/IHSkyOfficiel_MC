package be.raphtnt.ihworld;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Message {
    // Global
    PREFIX("§bIHSky §6>>§r "),
    PREFIXTEST("#11007aIH#06002aSky §6>&b>§r "),
    PREFIXTESTIG("&#11007aIH&#06002aSky &6>>&r "),
    ERROR(PREFIX.getMessage() + "§cVous n'avez pas la permission d'utiliser cette commande !");

    private String message;
    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return format(message);
    }

    public static String format(String message) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
        return  ChatColor.translateAlternateColorCodes('&', message);
    }


}
