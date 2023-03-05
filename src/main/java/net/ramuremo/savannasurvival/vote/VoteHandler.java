package net.ramuremo.savannasurvival.vote;

import com.mongodb.client.MongoCollection;
import com.vexsoftware.votifier.model.Vote;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.ramuremo.savannasurvival.SavannaSurvival;
import net.ramuremo.savannasurvival.config.SurvivalConfig;
import net.ramuremo.savannasurvival.utility.EventUtil;
import net.ramuremo.savannasurvival.vote.listener.VoteListener;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.awt.*;

public final class VoteHandler {
    public VoteHandler(@Nonnull JavaPlugin plugin) {
        EventUtil.register(
                plugin,
                new VoteListener(this)
        );
    }

    public MongoCollection<Document> getDBCollection() {
        return SavannaSurvival.getDatabase().getCollection("vote");
    }

    public void sendSuccessAnnounce(@Nonnull Vote vote) {
        final JDA jda = SavannaSurvival.getDiscordHandler().getJda();
        final TextChannel channel = jda.getTextChannelById(SavannaSurvival.getConfigFile().getValue(SurvivalConfig.Path.VOTE_ANNOUNCE_CHANNEL_ID));
        if (channel == null) return;
        final EmbedBuilder builder = new EmbedBuilder();
        final String iconUrl = "https://minotar.net/avatar/" + vote.getUsername() + ".png";
        builder.setColor(new Color(0xFF9FAC));
        builder.setAuthor("投票ありがとう!", iconUrl, iconUrl);
        builder.setTitle("投票を受け取りました! by " + vote.getUsername());
        builder.setDescription("ソース: " + vote.getServiceName());
        channel.sendMessageEmbeds(builder.build()).queue();
    }
}
