package ru.moonmag;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpaceRGBFix extends JavaPlugin implements Listener {

    private static final Pattern HEX_PATTERN = Pattern.compile("(&#|<#)([0-9a-fA-F]{6})(>|&)?");
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final String AUTHOR = "m0onmag";
    private static final String LINK = "<gradient:#00f5ff:#00d9ff>https://t.me/spacestudiomc</gradient>";

    private void log(String line) {
        Bukkit.getConsoleSender().sendMessage(MM.deserialize(line));
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        log("<#ff7c00>╔");
        log("<#ff7c00>║ <white>Запуск плагина...");
        log("<#ff7c00>║ <#00ff17>Плагин запустился! <white>Кодер: <#ff6e00>" + AUTHOR);
        log("<#ff7c00>║ " + LINK);
        log("<#ff7c00>╚");

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ChatFormatFixer(), this);

        if (getConfig().getBoolean("metrics")) {
            new Metrics(this, 25597);
        }

        if (getConfig().getBoolean("update-checker")) {
            new UpdateChecker(this).checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        log("<#ff7c00>╔");
        log("<#ff7c00>║ <white>Отключение плагина...");
        log("<#ff7c00>║ <#ff0000>Плагин отключился! <white>Кодер: <#ff6e00>" + AUTHOR);
        log("<#ff7c00>║ " + LINK);
        log("<#ff7c00>╚");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        message = applyHexFormatting(message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        event.setMessage(message);
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