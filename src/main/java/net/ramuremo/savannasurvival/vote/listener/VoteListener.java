package net.ramuremo.savannasurvival.vote.listener;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.ramuremo.savannasurvival.SavannaSurvival;
import net.ramuremo.savannasurvival.coin.Coin;
import net.ramuremo.savannasurvival.config.SurvivalConfig;
import net.ramuremo.savannasurvival.vote.Vote;
import net.ramuremo.savannasurvival.vote.VoteHandler;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tokyo.ramune.savannacore.SavannaCore;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

public final class VoteListener implements Listener {
    private final VoteHandler handler;

    public VoteListener(@Nonnull VoteHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onVoteReceive(VotifierEvent event) {
        final String playerName = event.getVote().getUsername();
        final UUID uuid = Bukkit.getPlayerUniqueId(playerName);
        if (uuid == null) return;

        addVoteDatabase(uuid);
        addCoinDatabase(uuid);

        sendAnnounce(event.getVote());
    }

    private void addVoteDatabase(@Nonnull UUID uuid) {
        final MongoCollection<Document> voteCollection = SavannaSurvival.getInstance().getDatabase().getCollection("vote");
        final Bson voteFilter = Filters.eq("uuid", uuid.toString());
        final Document voteFound = voteCollection.find(voteFilter).first();
        if (voteFound == null) {
            voteCollection.insertOne(new Vote(uuid, 1).toDocument());
        } else {
            final Vote vote = new Vote(voteFound);
            vote.setAmount(vote.getAmount() + 1L);
            voteCollection.updateOne(voteFilter, vote.toDocument());
        }
    }

    private void addCoinDatabase(@Nonnull UUID uuid) {
        final MongoCollection<Document> collection = SavannaSurvival.getInstance().getDatabase().getCollection("coin");
        final Bson filter = Filters.eq("uuid", uuid.toString());
        final Document found = collection.find(filter).first();
        if (found == null) {
            collection.insertOne(new Coin(uuid, 5).toDocument());
        } else {
            final Coin coin = new Coin(found);
            coin.setAmount(coin.getAmount() + 5);
            collection.updateOne(filter, coin.toDocument());
        }
    }

    private void sendAnnounce(@Nonnull com.vexsoftware.votifier.model.Vote vote) {
        final JDA jda = SavannaCore.getInstance().discordHandler.getJda();
        final TextChannel channel = jda.getTextChannelById(SavannaSurvival.getInstance().config.getValue(SurvivalConfig.Path.VOTE_ANNOUNCE_CHANNEL_ID));
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