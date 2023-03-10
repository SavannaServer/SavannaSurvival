package net.ramuremo.savannasurvival;

import com.mongodb.client.MongoDatabase;
import net.ramuremo.savannasurvival.command.TradeCommand;
import net.ramuremo.savannasurvival.vote.VoteHandler;
import net.ramuremo.savannasurvival.config.SurvivalConfig;
import net.ramuremo.savannasurvival.world.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tokyo.ramune.savannacore.SavannaCore;
import tokyo.ramune.savannacore.utility.CommandUtil;

public final class SavannaSurvival extends JavaPlugin {
    private static SavannaSurvival instance;
    public SurvivalConfig config;
    public WorldHandler worldHandler;
    public VoteHandler voteHandler;

    @Override
    public void onEnable() {
        instance = this;

        if (SavannaCore.getInstance().discordHandler == null) {
            getLogger().warning("This plugin use JDA from SavannaCore.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        config = new SurvivalConfig(this);

        CommandUtil.register(
                "savanna-survival",
                new TradeCommand()
        );

        worldHandler = new WorldHandler(this);
        worldHandler.loadWorlds();

         voteHandler = new VoteHandler(this);

        getLogger().info("The plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        worldHandler.unloadWorlds();

        getLogger().info("The plugin has been disabled.");
    }

    public static SavannaSurvival getInstance() {
        return instance;
    }

    public MongoDatabase getDatabase() {
        return SavannaCore.getInstance().databaseHandler.getClient().getDatabase("savanna");
    }
}