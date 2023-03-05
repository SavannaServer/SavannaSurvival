package net.ramuremo.savannasurvival.utility;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public final class EventUtil {
    public static void register(@Nonnull JavaPlugin plugin, @Nonnull Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static void unregister(@Nonnull Listener... listeners) {
        for (Listener listener : listeners) {
            for (HandlerList handlerList : HandlerList.getHandlerLists()) {
                for (RegisteredListener registeredListener : handlerList.getRegisteredListeners()) {
                    if (registeredListener.getListener().getClass().equals(listener.getClass()))
                        HandlerList.unregisterAll(registeredListener.getListener());
                }
            }
        }
    }

    public static void callEvent(@Nonnull Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }
}
