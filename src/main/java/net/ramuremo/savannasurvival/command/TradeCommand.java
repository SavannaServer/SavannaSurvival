package net.ramuremo.savannasurvival.command;

import net.ramuremo.savannasurvival.TradeMenu;
import net.ramuremo.savannasurvival.coin.Coin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public final class TradeCommand extends Command {
    public TradeCommand() {
        super("trade");
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String commandLabel, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) return false;
        new TradeMenu(player);
        return false;
    }
}
