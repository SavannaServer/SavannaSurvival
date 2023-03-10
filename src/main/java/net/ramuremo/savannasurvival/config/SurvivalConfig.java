package net.ramuremo.savannasurvival.config;

import org.bukkit.plugin.Plugin;
import tokyo.ramune.savannacore.config.ConfigFile;

import javax.annotation.Nonnull;

public final class SurvivalConfig extends ConfigFile {
    public SurvivalConfig(Plugin plugin) {
        super(plugin, "config.yml");

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public <T> T getValue(@Nonnull Path<T> path) {
        return getConfig().getObject("config." + path.getPath(), path.getClazz(), path.getDefault());
    }

    public <T> void setValue(@Nonnull Path<T> path, T value) {
        getConfig().set("config." + path.getPath(), value);
    }

    public interface Path<T> {
        Path<String> DATABASE_HOST = new Path<String>() {
            @Override
            public String getPath() {
                return "database.host";
            }

            @Override
            public String getDefault() {
                return "localhost";
            }

            @Override
            public Class<String> getClazz() {
                return String.class;
            }
        };
        Path<Integer> DATABASE_PORT = new Path<Integer>() {
            @Override
            public String getPath() {
                return "database.port";
            }

            @Override
            public Integer getDefault() {
                return 27017;
            }

            @Override
            public Class<Integer> getClazz() {
                return Integer.class;
            }
        };
        Path<String> DISCORD_TOKEN = new Path<String>() {
            @Override
            public String getPath() {
                return "discord.token";
            }

            @Override
            public String getDefault() {
                return "token";
            }

            @Override
            public Class<String> getClazz() {
                return String.class;
            }
        };
        Path<Long> VOTE_ANNOUNCE_CHANNEL_ID = new Path<Long>() {
            @Override public String getPath() {return "vote.announce-channel-id";}
            @Override public Long getDefault() {return 0L;}
            @Override public Class<Long> getClazz() {return Long.class;}
        };

        String getPath();
        T getDefault();
        Class<T> getClazz();
    }
}
