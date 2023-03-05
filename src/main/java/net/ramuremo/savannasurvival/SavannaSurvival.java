package net.ramuremo.savannasurvival;

import com.mongodb.client.MongoDatabase;
import net.ramuremo.savannasurvival.database.DatabaseHandler;
import net.ramuremo.savannasurvival.discord.DiscordHandler;
import net.ramuremo.savannasurvival.vote.VoteHandler;
import net.ramuremo.savannasurvival.config.SurvivalConfig;
import net.ramuremo.savannasurvival.world.WorldHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class SavannaSurvival extends JavaPlugin {
    private static SavannaSurvival instance;
    private static SurvivalConfig config;
    private static DiscordHandler discordHandler;
    private static DatabaseHandler databaseHandler;
    private static WorldHandler worldHandler;
    private static VoteHandler voteHandler;

    @Override
    public void onEnable() {
        instance = this;
        config = new SurvivalConfig(this);

        discordHandler = new DiscordHandler(config.getValue(SurvivalConfig.Path.DISCORD_TOKEN));

        databaseHandler = new DatabaseHandler();
        databaseHandler.connect(
                config.getValue(SurvivalConfig.Path.DATABASE_HOST),
                config.getValue(SurvivalConfig.Path.DATABASE_PORT)
        );

        worldHandler = new WorldHandler(this);
        worldHandler.loadWorlds();

        voteHandler = new VoteHandler(this);

        getLogger().info("The plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        discordHandler.shutdown();
        worldHandler.unloadWorlds();

        getLogger().info("The plugin has been disabled.");
    }

    public static MongoDatabase getDatabase() {
        return getDatabaseHandler().getClient().getDatabase("savanna-survival");
    }

    public static SavannaSurvival getInstance() {
        return instance;
    }

    public static SurvivalConfig getConfigFile() {
        return config;
    }

    public static DiscordHandler getDiscordHandler() {
        return discordHandler;
    }

    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public static WorldHandler getWorldHandler() {
        return worldHandler;
    }

    public static VoteHandler getVoteHandler() {
        return voteHandler;
    }
}