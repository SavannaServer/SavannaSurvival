package net.ramuremo.savannasurvival.vote.listener;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.ramuremo.savannasurvival.SavannaSurvival;
import net.ramuremo.savannasurvival.vote.Vote;
import net.ramuremo.savannasurvival.vote.VoteHandler;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
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
        handler.sendSuccessAnnounce(event.getVote());

        final MongoCollection<Document> collection = SavannaSurvival.getVoteHandler().getDBCollection();
        final Bson query = Filters.eq("uuid", uuid);
        final Document found = collection.find(query).first();
        if (found == null) {
            collection.insertOne(new Vote(uuid, 1, 0).toDocument());
        } else {
            final Vote vote = new Vote(found);
            vote.setAmount(vote.getAmount() + 1L);
            collection.updateOne(found, vote.toDocument());
        }

        final Player cachedPlayer = Bukkit.getPlayer(uuid);
        if (cachedPlayer == null) return;

    }
}