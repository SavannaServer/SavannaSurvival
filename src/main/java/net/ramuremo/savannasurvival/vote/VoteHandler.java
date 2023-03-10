package net.ramuremo.savannasurvival.vote;

import net.ramuremo.savannasurvival.vote.listener.VoteListener;
import org.bukkit.plugin.java.JavaPlugin;
import tokyo.ramune.savannacore.utility.EventUtil;

import javax.annotation.Nonnull;

public final class VoteHandler {
    public VoteHandler(@Nonnull JavaPlugin plugin) {
        EventUtil.register(
                plugin,
                new VoteListener(this)
        );
    }
}