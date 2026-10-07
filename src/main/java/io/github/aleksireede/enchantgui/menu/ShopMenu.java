package io.github.aleksireede.enchantgui.menu;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.aleksireede.enchantgui.EnchantGUIPlugin;
import io.github.aleksireede.enchantgui.NbtUtils;
import io.github.aleksireede.enchantgui.config.Enchants;
import io.github.aleksireede.enchantgui.config.EShopConfig;
import io.github.aleksireede.enchantgui.economy.PaymentStrategy;
import io.github.aleksireede.enchantgui.localization.LocalizationManager;
import io.github.aleksireede.enchantgui.permissions.EShopPermissionSys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author aleksireede
 */
public class ShopMenu {
    private final Enchants enchants;

    public ShopMenu(final Enchants enchants) {
        this.enchants = enchants;
    }

    public void showMainMenu(Player player) {
        Gui gui = Gui.gui()
                .rows(6)
                .title(Component.text(EnchantGUIPlugin.getInstance().getMainConfig().getMenuName()))
                .create();

        List<ItemStack> enchantList = enchants.getEnchantList();
        Set<String> disabled = EnchantGUIPlugin.getInstance().getMainConfig().getDisabledEnchants();
        for (ItemStack itemStack : enchantList) {
            // Defensive check: also filter disabled enchantments at display time
            // in case the Enchants list was built before a config reload
            boolean itemDisabled = false;
            for (Enchantment ench : itemStack.getEnchantments().keySet()) {
                String registryKey = ench.getKey().getKey();
                if (disabled.contains(registryKey) || disabled.contains(EShopConfig.normalizeEnchantKey(registryKey))) {
                    itemDisabled = true;
                    break;
                }
            }
            if (itemDisabled) {
                continue;
            }
            GuiItem guiItem = new GuiItem(itemStack);
            guiItem.setAction(_ -> generateEnchantMenu(gui, itemStack, player).open(player));

            if (!EnchantGUIPlugin.getInstance().getMainConfig().getShowPerItem()) {
                gui.addItem(guiItem);
            } else if (isShowPerItem(itemStack, player.getInventory().getItemInMainHand())) {
                gui.addItem(guiItem);
            }

            EnchantGUIPlugin.debug("ShowPerItem= %b".formatted(EnchantGUIPlugin.getInstance().getMainConfig().getShowPerItem()));
            EnchantGUIPlugin.debug("IsShowPerItem= %b".formatted(isShowPerItem(itemStack, player.getInventory().getItemInMainHand())));
            EnchantGUIPlugin.debug("ShopItem= %s".formatted(itemStack.toString()));
            EnchantGUIPlugin.debug("HeldItem= %s".formatted(player.getInventory().getItemInMainHand().toString()));
        }

        EnchantGUIPlugin.debug("showMainMenu: enchantList size = " + enchantList.size());
        gui.open(player);
    }

    private boolean isShowPerItem(final @NotNull ItemStack shopEnchant, final ItemStack itemInMainHand) {
        for (Enchantment enchantment : shopEnchant.getEnchantments().keySet()) {
            if (enchantment.canEnchantItem(itemInMainHand)) {
                return true;
            }
        }
        return false;
    }

    private @NotNull Gui generateEnchantMenu(final Gui sourceGui, final @NotNull ItemStack item, final Player player) {
        Gui gui = Gui.gui()
                .rows(4)
                .title(Component.text(EnchantGUIPlugin.getInstance().getMainConfig().getMenuName()))
                .create();

        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        for (final ItemStack itemStack : generatePurchaseMenuItems(player, item, enchantment)) {
            GuiItem guiItem = new GuiItem(itemStack);
            guiItem.setAction(event -> {
                purchaseEnchant(player, event.getCurrentItem());
                gui.close(player);
            });
            gui.addItem(guiItem);
        }

        final String material = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.back-item.material", "EMERALD");
        final String displayName = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.back-item.display-name", "Go back");
        final int slot = (gui.getRows() * 9) - 9;

        // Use Paper's native ItemMeta.displayName(Component) API directly instead of
        // ItemBuilder.name(Component), which internally calls BukkitNameLoreHandler.serializeComponent()
        // that relies on net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer —
        // a class removed in Adventure 5.x (used by Paper 26.2).
        ItemStack backItem = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)));
        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.displayName(Component.text(displayName));
        backItem.setItemMeta(backMeta);
        gui.setItem(slot, new GuiItem(backItem, _ -> sourceGui.open(player)));
        return gui;
    }

    private void purchaseEnchant(@NotNull Player player, final ItemStack item) {
        if (item == null)
            return;
        LocalizationManager lm = EnchantGUIPlugin.getInstance().getLm();
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        ItemStack playerHand = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        NamespacedKey levelKey = new NamespacedKey(EnchantGUIPlugin.getInstance(), NbtUtils.LEVEL);
        NamespacedKey priceKey = new NamespacedKey(EnchantGUIPlugin.getInstance(), NbtUtils.PRICE);
        Integer levelObj = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        Double priceObj = meta.getPersistentDataContainer().get(priceKey, PersistentDataType.DOUBLE);
        int level = levelObj != null ? levelObj : 0;
        double price = priceObj != null ? priceObj : 0.0;


        if (playerHand.getType() == Material.AIR) {
            tell(player, lm.getLanguageString("cant-enchant"));
            return;
        }
        if (!enchantment.canEnchantItem(playerHand) && !EnchantGUIPlugin.getInstance().getMainConfig().getIgnoreItemType()) {
            tell(player, lm.getLanguageString("item-cant-be-enchanted"));
            return;
        }

        // Prevent applying an enchantment that's already on the item
        if (playerHand.getEnchantmentLevel(enchantment) > 0) {
            tell(player, lm.getLanguageString("already-enchanted", "Your item already has this enchantment!"));
            return;
        }

        PaymentStrategy payment = EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy();
        if (!payment.hasSufficientFunds(player, price)) {
            tell(player, lm.getLanguageString("insufficient-funds"));
            return;
        }
        
        payment.withdraw(player,price);
        enchantItem(playerHand, enchantment, level);
        final String enchantName = LegacyComponentSerializer.legacySection().serialize(
                Objects.requireNonNullElse(item.getItemMeta().displayName(), Component.empty()));
        tell(player, "%s &d%s %d &ffor &6%.2f%s".formatted(lm.getLanguageString("item-enchanted"), enchantName, level, price, EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy().getCurrency()));
    }

    private void enchantItem(ItemStack playerHand, @NotNull Enchantment enchantment, int level) {
        if (level > enchantment.getMaxLevel() || !enchantment.canEnchantItem(playerHand)) {
            // Unsafe enchant
            playerHand.addUnsafeEnchantment(enchantment, level);
        } else {
            // Safe, regular enchant
            playerHand.addEnchantment(enchantment, level);
        }
    }

    public void reload() {
        this.enchants.reload();
    }

    private @NotNull List<ItemStack> generatePurchaseMenuItems(final Player player, final ItemStack item, final Enchantment enchantment) {
        List<ItemStack> itemList = new ArrayList<>();
        String[] enchantLevels = EnchantGUIPlugin.getInstance().getMainConfig().getEnchantLevels(enchantment);

        if (item == null)
            return Collections.emptyList();

        for (String enchantLevel : enchantLevels) {
            enchantLevel = enchantLevel.substring(5);
            int level = Integer.parseInt(enchantLevel);
            if (!EShopPermissionSys.hasEnchantPermission(player, enchantment, level)) {
                continue;
            }

            itemList.add(generateItemWithMeta(item, level, enchantment));
            //TODO: Upgrade option, pass the original item as an object and compare the enchantments. Make sure to account for negative price.
            EnchantGUIPlugin.debug(item.toString());
        }
        return itemList;
    }

    private @NotNull ItemStack generateItemWithMeta(@NotNull ItemStack item, int level, Enchantment enchantment) {
        ItemStack tempItem = item.clone();
        ItemMeta meta = tempItem.getItemMeta();

        double price = EnchantGUIPlugin.getInstance().getMainConfig().getPrice(enchantment, level);

        List<Component> lore = new ArrayList<>();
        lore.add(LegacyComponentSerializer.legacySection().deserialize(formatLevel(level)));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(formatPrice(price)));

        meta.lore(lore);

        NamespacedKey levelKey = new NamespacedKey(EnchantGUIPlugin.getInstance(), NbtUtils.LEVEL);
        NamespacedKey priceKey = new NamespacedKey(EnchantGUIPlugin.getInstance(), NbtUtils.PRICE);
        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        meta.getPersistentDataContainer().set(priceKey, PersistentDataType.DOUBLE, price);

        tempItem.setItemMeta(meta);
        tempItem.setAmount(level);

        return tempItem;
    }

    @NotNull
    private String formatLevel(int type) {
        String string = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.level");
        return MessageFormat.format(ChatUtil.color(string), type);
    }

    @NotNull
    private String formatPrice(double type) {
        String string = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.price");
        return MessageFormat.format(ChatUtil.color(string), type);
    }

    private void tell(Player player, String message) {
        ChatUtil.sendMessage(player, EnchantGUIPlugin.getInstance().getLm().getPrefix() + message);
    }
}
