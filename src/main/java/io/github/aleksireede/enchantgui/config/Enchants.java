package io.github.aleksireede.enchantgui.config;

import io.github.aleksireede.enchantgui.EnchantGUIPlugin;
import io.github.aleksireede.enchantgui.localization.LocalizationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Enchants {
    private List<ItemStack> enchantList;

    public Enchants() {
        this.enchantList = new ArrayList<>();
        createEnchantList();
    }

    public List<ItemStack> getEnchantList() {
        return enchantList;
    }

    public void reload() {
        this.enchantList = new ArrayList<>();
        createEnchantList();
    }

    /**
     * Reads the list of supported materials for an enchantment from the shop configuration.
     * Supports both list format (YAML list) and string format (single material, for backward compatibility).
     */
    private List<Material> matchEnchants(@NotNull String enchantment) {
        String path = "shop.enchants." + enchantment;
        var shopFile = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile();

        List<String> materialNames = shopFile.getStringList(path);
        if (materialNames.isEmpty()) {
            // Fall back to single string format for backward compatibility
            String single = shopFile.getString(path);
            if (single != null && !single.isEmpty()) {
                materialNames = Collections.singletonList(single);
            }
        }

        List<Material> result = new ArrayList<>();
        for (String mat : materialNames) {
            Material material = Material.matchMaterial(mat);
            if (material != null) {
                result.add(material);
            } else {
                EnchantGUIPlugin.getInstance().getLogger().warning(() -> "Invalid material '" + mat + "' for enchant: " + enchantment);
            }
        }
        return result;
    }

    /**
     * Generates the list of all enchants with their Enchantment,
     * Materials and a display name.
     */
    private void createEnchantList() {
        LocalizationManager lm = EnchantGUIPlugin.getInstance().getLm();
        var disabled = EnchantGUIPlugin.getInstance().getMainConfig().getDisabledEnchants();
        addItem(Enchantment.POWER, "power", matchEnchants("power"), lm.getLanguageString("enchant.power"), disabled);
        addItem(Enchantment.FLAME, "flame", matchEnchants("flame"), lm.getLanguageString("enchant.flame"), disabled);
        addItem(Enchantment.INFINITY, "infinity", matchEnchants("infinity"), lm.getLanguageString("enchant.infinity"), disabled);
        addItem(Enchantment.PUNCH, "punch", matchEnchants("punch"), lm.getLanguageString("enchant.punch"), disabled);
        addItem(Enchantment.SHARPNESS, "sharpness", matchEnchants("sharpness"), lm.getLanguageString("enchant.sharpness"), disabled);
        addItem(Enchantment.BANE_OF_ARTHROPODS, "bane_of_arthropods", matchEnchants("bane_of_arthropods"), lm.getLanguageString("enchant.bane_of_arthropods"), disabled);
        addItem(Enchantment.SMITE, "smite", matchEnchants("smite"), lm.getLanguageString("enchant.smite"), disabled);
        addItem(Enchantment.DEPTH_STRIDER, "depth_strider", matchEnchants("depth_strider"), lm.getLanguageString("enchant.depth_strider"), disabled);
        addItem(Enchantment.EFFICIENCY, "efficiency", matchEnchants("efficiency"), lm.getLanguageString("enchant.efficiency"), disabled);
        addItem(Enchantment.UNBREAKING, "unbreaking", matchEnchants("unbreaking"), lm.getLanguageString("enchant.unbreaking"), disabled);
        addItem(Enchantment.FIRE_ASPECT, "fire_aspect", matchEnchants("fire_aspect"), lm.getLanguageString("enchant.fire_aspect"), disabled);
        addItem(Enchantment.KNOCKBACK, "knockback", matchEnchants("knockback"), lm.getLanguageString("enchant.knockback"), disabled);
        addItem(Enchantment.FORTUNE, "fortune", matchEnchants("fortune"), lm.getLanguageString("enchant.fortune"), disabled);
        addItem(Enchantment.LOOTING, "looting", matchEnchants("looting"), lm.getLanguageString("enchant.looting"), disabled);
        addItem(Enchantment.LUCK_OF_THE_SEA, "luck_of_the_sea", matchEnchants("luck_of_the_sea"), lm.getLanguageString("enchant.luck_of_the_sea"), disabled);
        addItem(Enchantment.LURE, "lure", matchEnchants("lure"), lm.getLanguageString("enchant.lure"), disabled);
        addItem(Enchantment.RESPIRATION, "respiration", matchEnchants("respiration"), lm.getLanguageString("enchant.respiration"), disabled);
        addItem(Enchantment.PROTECTION, "protection", matchEnchants("protection"), lm.getLanguageString("enchant.protection"), disabled);
        addItem(Enchantment.BLAST_PROTECTION, "blast_protection", matchEnchants("blast_protection"), lm.getLanguageString("enchant.blast_protection"), disabled);
        addItem(Enchantment.FEATHER_FALLING, "feather_falling", matchEnchants("feather_falling"), lm.getLanguageString("enchant.feather_falling"), disabled);
        addItem(Enchantment.FIRE_PROTECTION, "fire_protection", matchEnchants("fire_protection"), lm.getLanguageString("enchant.fire_protection"), disabled);
        addItem(Enchantment.PROJECTILE_PROTECTION, "projectile_protection", matchEnchants("projectile_protection"), lm.getLanguageString("enchant.projectile_protection"), disabled);
        addItem(Enchantment.SILK_TOUCH, "silk_touch", matchEnchants("silk_touch"), lm.getLanguageString("enchant.silk_touch"), disabled);
        addItem(Enchantment.THORNS, "thorns", matchEnchants("thorns"), lm.getLanguageString("enchant.thorns"), disabled);
        addItem(Enchantment.AQUA_AFFINITY, "aqua_affinity", matchEnchants("aqua_affinity"), lm.getLanguageString("enchant.aqua_affinity"), disabled);
        addItem(Enchantment.FROST_WALKER, "frost_walker", matchEnchants("frost_walker"), lm.getLanguageString("enchant.frost_walker"), disabled);
        addItem(Enchantment.MENDING, "mending", matchEnchants("mending"), lm.getLanguageString("enchant.mending"), disabled);
        addItem(Enchantment.SWEEPING_EDGE, "sweeping", matchEnchants("sweeping"), lm.getLanguageString("enchant.sweeping"), disabled);
        addItem(Enchantment.CHANNELING, "channeling", matchEnchants("channeling"), lm.getLanguageString("enchant.channeling"), disabled);
        addItem(Enchantment.IMPALING, "impaling", matchEnchants("impaling"), lm.getLanguageString("enchant.impaling"), disabled);
        addItem(Enchantment.LOYALTY, "loyalty", matchEnchants("loyalty"), lm.getLanguageString("enchant.loyalty"), disabled);
        addItem(Enchantment.RIPTIDE, "riptide", matchEnchants("riptide"), lm.getLanguageString("enchant.riptide"), disabled);

        addItem(Enchantment.PIERCING, "piercing", matchEnchants("piercing"), lm.getLanguageString("enchant.piercing"), disabled);
        addItem(Enchantment.MULTISHOT, "multishot", matchEnchants("multishot"), lm.getLanguageString("enchant.multishot"), disabled);
        addItem(Enchantment.QUICK_CHARGE, "quick_charge", matchEnchants("quick_charge"), lm.getLanguageString("enchant.quick_charge"), disabled);

        addItem(Enchantment.SOUL_SPEED, "soul_speed", matchEnchants("soul_speed"), lm.getLanguageString("enchant.soul_speed"), disabled);
        addItem(Enchantment.SWIFT_SNEAK, "swift_sneak", matchEnchants("swift_sneak"), lm.getLanguageString("enchant.swift_sneak"), disabled);
        addItem(Enchantment.BREACH, "breach", matchEnchants("breach"), lm.getLanguageString("enchant.breach"), disabled);
        addItem(Enchantment.DENSITY, "density", matchEnchants("density"), lm.getLanguageString("enchant.density"), disabled);
        addItem(Enchantment.WIND_BURST, "wind_burst", matchEnchants("wind_burst"), lm.getLanguageString("enchant.wind_burst"), disabled);
        addItem(Enchantment.LUNGE, "lunge", matchEnchants("lunge"), lm.getLanguageString("enchant.lunge"), disabled);
        addItem(Enchantment.BINDING_CURSE, "binding_curse", matchEnchants("binding_curse"), lm.getLanguageString("enchant.binding_curse"), disabled);
        addItem(Enchantment.VANISHING_CURSE, "vanishing_curse", matchEnchants("vanishing_curse"), lm.getLanguageString("enchant.vanishing_curse"), disabled);
    }

    /**
     * Add item to enchant list.
     * Creates one display item per enchantment using the first valid material.
     * All supported materials are listed in the item's lore so the player can see
     * every item the enchantment can be applied to. Materials that don't actually
     * support the enchantment are skipped via addEnchantment's IllegalArgumentException.
     */
    private void addItem(Enchantment type, @NotNull String key, List<Material> mats, String displayName, @NotNull Set<String> disabled) {
        if (disabled.contains(key) || disabled.contains(type.getKey().getKey())) {
            return;
        }
        if (mats.isEmpty()) {
            EnchantGUIPlugin.getInstance().getLogger().warning(() -> "Missing shop materials for enchant: " + key);
            return;
        }

        // Find all valid materials that support this enchantment
        List<Material> validMats = new ArrayList<>();
        Material displayMat = null;
        for (Material mat : mats) {
            ItemStack testItem = new ItemStack(mat);
            try {
                testItem.addEnchantment(type, 1);
            } catch (IllegalArgumentException e) {
                EnchantGUIPlugin.getInstance().getLogger().warning(() -> mat.name() + " doesn't support enchant " + type.getKey() + " — skipped for: " + key);
                continue;
            }
            validMats.add(mat);
            if (displayMat == null) {
                displayMat = mat;
            }
        }

        if (displayMat == null) {
            EnchantGUIPlugin.getInstance().getLogger().warning(() -> "No valid material found for enchant " + type.getKey() + ": " + key);
            return;
        }

        ItemStack item = new ItemStack(displayMat);
        item.addEnchantment(type, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));

            // Add lore listing all supported materials (if more than one)
            if (validMats.size() > 1) {
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("Supported items:", NamedTextColor.DARK_GRAY));
                for (Material m : validMats) {
                    lore.add(Component.text("• " + m.name(), NamedTextColor.GRAY));
                }
                meta.lore(lore);
            }

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            enchantList.add(item);
        }
    }

}
