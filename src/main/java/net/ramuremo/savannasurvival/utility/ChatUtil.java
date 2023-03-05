package net.ramuremo.savannasurvival.utility;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public final class ChatUtil {
    private static final RateLimiter<CommandSender> RATE_LIMITER = new RateLimiter<>(1);
    private static final String PREFIX = "§f[§6Savanna§f]";

    public static void sendMessage(Player player, String message, boolean addPrefix) {
        player.sendMessage(addPrefix ? PREFIX : " " + message);
    }

    public static void sendMessage(Player player, String message, boolean addPrefix, boolean rateLimited) {
        if (rateLimited && !RATE_LIMITER.tryAcquire(player))
            return;

        sendMessage(player, message, addPrefix);
    }

    public static void sendMessage(CommandSender sender, String message, boolean addPrefix) {
        sender.sendMessage(addPrefix ? PREFIX : " " + message);
    }

    public static void sendMessage(CommandSender sender, String message, boolean addPrefix, boolean rateLimited) {
        if (rateLimited && !RATE_LIMITER.tryAcquire(sender))
            return;

        sendMessage(sender, message, addPrefix);
    }

    public static void requirePermission(CommandSender sender, Permission permission) {
        sendMessage(sender, "§cYou need the permission of " + permission.getName() + " to do that.", true);
    }
}
