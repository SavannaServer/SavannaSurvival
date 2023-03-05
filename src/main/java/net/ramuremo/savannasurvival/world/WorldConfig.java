package net.ramuremo.savannasurvival.world;

import net.ramuremo.savannasurvival.config.ConfigFile;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public final class WorldConfig extends ConfigFile {
    public WorldConfig(Plugin plugin, File parent) {
        super(plugin, parent, "savanna-survival.yml");

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        if (Objects.equals(getValue(Path.CREATED_MONTH), -1)) {
            setValue(Path.CREATED_MONTH, Calendar.getInstance(Locale.JAPAN).get(Calendar.MONTH));
            saveConfig();
        }
    }

    public <T> T getValue(@Nonnull Path<T> path) {
        return getConfig().getObject(path.getPath(), path.getClazz(), path.getDefault());
    }

    public <T> void setValue(@Nonnull Path<T> path, T value) {
        getConfig().set(path.getPath(), value);
    }

    public interface Path<T> {
        Path<Integer> CREATED_MONTH = new Path<Integer>() {
            @Override public String getPath() {return "created-month";}
            @Override public Integer getDefault() {return -1;}
            @Override public Class<Integer> getClazz() {return Integer.class;}
        };

        String getPath();
        T getDefault();
        Class<T> getClazz();
    }
}
