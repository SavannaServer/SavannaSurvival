package net.ramuremo.savannasurvival.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;
import java.time.Duration;

public final class DiscordHandler {
    private final JDA jda;

    public DiscordHandler(@Nonnull String token) {
        final JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.setActivity(Activity.streaming("Server Working!", "https://savanna.ramuremo.net"));

        this.jda = builder.build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        jda.shutdownNow();
        try {
            jda.awaitShutdown(Duration.ofSeconds(5));
        } catch (Exception ignored) {
        }
    }

    public JDA getJda() {
        return jda;
    }
}
