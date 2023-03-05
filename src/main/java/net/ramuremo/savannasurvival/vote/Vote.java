package net.ramuremo.savannasurvival.vote;

import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.UUID;

public class Vote {
    private final UUID uuid;
    private long amount;
    private long claimed;

    public Vote(@Nonnull UUID uuid, long amount, long claimed) {
        this.uuid = uuid;
        this.amount = amount;
        this.claimed = claimed;
    }

    public Vote(@Nonnull Document document) {
        this.uuid = UUID.fromString((String) document.get("uuid"));
        this.amount = document.getLong("amount");
        this.claimed = document.getLong("claimed");
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getClaimed() {
        return claimed;
    }

    public void setClaimed(long claimed) {
        this.claimed = claimed;
    }

    public Document toDocument() {
        final Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("amount", amount);
        document.put("claimed", claimed);
        return document;
    }
}
