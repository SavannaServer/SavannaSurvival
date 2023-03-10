package net.ramuremo.savannasurvival;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.ramuremo.savannasurvival.coin.Coin;
import net.ramuremo.savannasurvival.coin.CoinTradeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import tokyo.ramune.savannacore.utility.EventUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TradeMenu {
    private final InventoryView view;
    private final ItemStack buyButton;
    private final List<MerchantRecipe> recipes = new ArrayList<>();
    private CoinTradeRegistry selectedRecipe;

    public TradeMenu(@Nonnull Player player, @Nonnull CoinTradeRegistry lastRegistry) {
        this(player);
        selectRecipe(lastRegistry);
    }

    public TradeMenu(@Nonnull Player player) {
        final long coin = Coin.getCoin(player.getUniqueId());
        final Merchant merchant = Bukkit.createMerchant(
                Component.text("コイン トレード ").color(TextColor.color(0x0))
                        .append(Component.text("所持コイン: " + coin))
        );
        for (CoinTradeRegistry registry : CoinTradeRegistry.values()) {
            recipes.add(registry.serialize());
        }
        merchant.setRecipes(recipes);
        view = player.openMerchant(merchant, true);
        EventUtil.register(
                SavannaSurvival.getInstance(),
                new TradeListener()
        );

        buyButton = new ItemStack(Material.CAMPFIRE);
        final ItemMeta buyButtonMeta = buyButton.getItemMeta();
        buyButtonMeta.displayName(Component.text("選択した項目をトレードする").color(TextColor.color(0xFFB031)));
        buyButtonMeta.lore(List.of(Component.text("<クリックでトレード>").color(TextColor.color(0xEAFF00))));
        buyButton.setItemMeta(buyButtonMeta);
        view.setItem(0, buyButton);
    }

    private void selectRecipe(@Nonnull CoinTradeRegistry registry) {
        selectedRecipe = registry;
        view.setItem(1, registry.serialize().getResult());
    }

    private final class TradeListener implements Listener {
        @EventHandler
        public void onTradeSelect(TradeSelectEvent event) {
            final InventoryView inventoryView = event.getView();
            if (!inventoryView.equals(view)) return;
            final Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);

            final MerchantRecipe clickedRecipe = recipes.get(event.getIndex());
            final CoinTradeRegistry registry = Arrays.stream(CoinTradeRegistry.values())
                    .filter(reg -> reg.serialize().getResult().equals(clickedRecipe.getResult()))
                    .findFirst()
                    .orElse(null);
            if (registry == null) return;
            selectRecipe(registry);
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            final InventoryView inventoryView = event.getView();
            if (!inventoryView.equals(view)) return;
            event.setCancelled(true);

            final ItemStack currentItem = event.getCurrentItem();
            final Player player = (Player) event.getWhoClicked();
            if (currentItem == null) return;
            if (selectedRecipe == null) return;

            if (currentItem.equals(buyButton)) {
                final long coin = Coin.getCoin(player.getUniqueId());
                if (selectedRecipe.getIngredient() > coin) {
                    sendNotEnoughCoin(player);
                    return;
                }
                Coin.setCoin(player.getUniqueId(), coin - selectedRecipe.getIngredient());
                selectedRecipe.getOnTrade().accept(player);
                sendSuccess(player);
                new TradeMenu(player, selectedRecipe);
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent event) {
            final InventoryView inventoryView = event.getView();
            if (!inventoryView.equals(view)) return;
            event.getView().setItem(0, new ItemStack(Material.AIR));
            event.getView().setItem(1, new ItemStack(Material.AIR));
            HandlerList.unregisterAll(this);
        }

        private void sendNotEnoughCoin(@Nonnull Player player) {
            player.sendMessage(Component.text("トレードに必要なコインが足りません。").color(TextColor.color(0xFF0C00))
                    .append(Component.text("\nヒント: 毎日投票をしてコインを集めよう!").color(TextColor.color(0xEAFF00))));
            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_GROWL, 1, 2);
            player.closeInventory();
        }

        private void sendSuccess(@Nonnull Player player) {
            player.sendMessage(Component.text("トレードされました!").color(TextColor.color(0x7FF22)));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.3F);
        }
    }
}
