package net.ramuremo.savannasurvival.vote;

import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.UUID;

public final class Vote {
    private final UUID uuid;
    private long amount;

    public Vote(@Nonnull UUID uuid, long amount) {
        this.uuid = uuid;
        this.amount = amount;
    }

    public Vote(@Nonnull Document document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.amount = document.getLong("amount");
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

    public Document toDocument() {
        final Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("amount", amount);
        return document;
    }
}
