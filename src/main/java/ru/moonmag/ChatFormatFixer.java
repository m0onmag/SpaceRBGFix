package ru.moonmag;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFormatFixer implements Listener {

    private static final Pattern HEX_PATTERN = Pattern.compile("(&#|<#)([0-9a-fA-F]{6})(>|&)?");

    @EventHandler
    public void onChatFormat(AsyncPlayerChatEvent event) {
        String format = event.getFormat();
        format = applyHexFormatting(format);
        format = ChatColor.translateAlternateColorCodes('&', format);
        event.setFormat(format);
    }

    private String applyHexFormatting(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            String hexColor = matcher.group(2);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement("§x" + convertHex(hexColor)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private String convertHex(String hex) {
        StringBuilder builder = new StringBuilder();
        for (char c : hex.toCharArray()) {
            builder.append("§").append(c);
        }
        return builder.toString();
    }
}