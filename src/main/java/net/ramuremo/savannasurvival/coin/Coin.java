package net.ramuremo.savannasurvival.coin;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.ramuremo.savannasurvival.SavannaSurvival;
import org.bson.Document;
import org.bson.conversions.Bson;
import tokyo.ramune.savannacore.SavannaCore;

import javax.annotation.Nonnull;
import java.util.UUID;

public class Coin {
    public static long getCoin(@Nonnull UUID uuid) {
        final MongoCollection<Document> collection = SavannaSurvival.getInstance().getDatabase().getCollection("coin");
        final Bson filter = Filters.eq("uuid", uuid.toString());
        final Document found = collection.find(filter).first();
        if (found == null) {
            return 0;
        } else {
            final Coin coin = new Coin(found);
            return coin.getAmount();
        }
    }

    public static void setCoin(@Nonnull UUID uuid, long amount) {
        final MongoCollection<Document> collection = SavannaSurvival.getInstance().getDatabase().getCollection("coin");
        final Bson filter = Filters.eq("uuid", uuid.toString());
        final Document found = collection.find(filter).first();
        if (found == null) {
            collection.insertOne(new Coin(uuid, amount).toDocument());
        } else {
            collection.findOneAndReplace(filter, new Coin(uuid, amount).toDocument());
        }
    }

    private final UUID uuid;
    private long amount;

    public Coin(@Nonnull UUID uuid, long amount) {
        this.uuid = uuid;
        this.amount = amount;
    }

    public Coin(@Nonnull Document document) {
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
