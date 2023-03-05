package net.ramuremo.savannasurvival.database;

import com.mongodb.MongoClient;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public final class DatabaseHandler {
    private MongoClient client;

    public DatabaseHandler() {
    }

    public void connect(@Nonnull String host, int port) {
        if (client != null) client.close();
        try {
            client = new MongoClient(host, port);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.shutdown();
        }
    }

    public MongoClient getClient() {
        return client;
    }
}
