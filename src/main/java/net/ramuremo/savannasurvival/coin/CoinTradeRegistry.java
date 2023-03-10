package net.ramuremo.savannasurvival.coin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import tokyo.ramune.savannacore.utility.Util;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public enum CoinTradeRegistry {
    IRON(5, Material.IRON_INGOT, title("鉄インゴット", TextColor.color(0xBABABA), 1, "個"), player -> Util.giveItem(player, new ItemStack(Material.IRON_INGOT))),
    DIAMOND(35, Material.DIAMOND, title("ダイアモンド", TextColor.color(0x199CFF), 1, "個"), player -> Util.giveItem(player, new ItemStack(Material.DIAMOND))),
    NAME_COLOR(35, Material.WRITABLE_BOOK, title("ネームカラー変更権利書", TextColor.color(0x1ED8C3), 7, "日間"), player -> {}),
    NICKNAME(50, Material.WRITABLE_BOOK, title("ニックネーム変更権利書", TextColor.color(0xA067FF), 7, "日間"), player -> {});

    private static Component title(@Nonnull String title, @Nonnull TextColor color, int amount, @Nonnull String suffix) {
        return Component.text(title + " ").color(color)
                .append(Component.text("x" + amount + suffix));
    }

    private final int ingredient;
    private final Material icon;
    private final Component displayName;
    private final Consumer<Player> onTrade;

    CoinTradeRegistry(int ingredient, Material icon, Component displayName, Consumer<Player> onTrade) {
        this.ingredient = ingredient;
        this.icon = icon;
        this.displayName = displayName;
        this.onTrade = onTrade;
    }

    public int getIngredient() {
        return ingredient;
    }

    public Material getIcon() {
        return icon;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Consumer<Player> getOnTrade() {
        return onTrade;
    }

    public MerchantRecipe serialize() {
        final ItemStack result = new ItemStack(icon);
        final ItemMeta resultMeta = result.getItemMeta();
        resultMeta.displayName(displayName);
        resultMeta.lore(List.of(
                Component.text("必要コイン: " + ingredient + "枚").color(TextColor.color(0xD49A13)),
                Component.text("<クリックで選択>").color(TextColor.color(0xEAFF00))
        ));
        result.setItemMeta(resultMeta);

        final MerchantRecipe recipe = new MerchantRecipe(result, 0);
        final int i = (ingredient - (ingredient % 64)) / 64;

        final ItemStack goldIngot = new ItemStack(Material.GOLD_INGOT, i),
                goldNugget = new ItemStack(Material.GOLD_NUGGET, ingredient % 64);
        final ItemMeta goldIngotMeta = goldIngot.getItemMeta(), goldNuggetMeta = goldNugget.getItemMeta();
        goldIngotMeta.displayName(Component.text("必要コイン: " + ingredient + "枚").color(TextColor.color(0xD49A13)));
        goldNuggetMeta.displayName(Component.text("必要コイン: " + ingredient + "枚").color(TextColor.color(0xD49A13)));
        goldIngot.setItemMeta(goldIngotMeta);
        goldNugget.setItemMeta(goldNuggetMeta);

        recipe.setIngredients(List.of(goldIngot, goldNugget));

        recipe.setMaxUses(100000000);
        recipe.setIgnoreDiscounts(true);
        recipe.setExperienceReward(false);
        recipe.setVillagerExperience(0);

        return recipe;
    }
}
