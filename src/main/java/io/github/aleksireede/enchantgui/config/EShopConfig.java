package io.github.aleksireede.enchantgui.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import io.github.aleksireede.enchantgui.EnchantGUIPlugin;
import io.github.aleksireede.enchantgui.economy.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EShopConfig {
    private YamlDocument config;
    private final EnchantGUIPlugin plugin;
    private static PaymentStrategy economy;

    public EShopConfig() {
        this.plugin = EnchantGUIPlugin.getInstance();
        this.createAndLoad();
    }

    private void createAndLoad() {
        try {
            this.config = YamlDocument.create(
                    new File(plugin.getDataFolder(), "config.yml"), Objects.requireNonNull(plugin.getResource("config.yml")),
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build()
            );
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to load config.yml");
        }
    }

    public void reload() {
        try {
            this.config.reload();
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to reload config.yml");
        }
    }

    public boolean getIgnoreItemType() {
        return config.getBoolean("ignore-itemtype");
    }

    public boolean getDebug() {
        return config.getBoolean("debug");
    }

    public double getPrice(@NotNull Enchantment enchantment, int level) {
        String path = enchantment.getKey().toString().toLowerCase();
        path = path.split(":")[1];
        path = normalizeEnchantKey(path) + ".level" + level;
        return config.getDouble(path);
    }

    public boolean isNotRightClickEnchantingTable() {
        return !config.getBoolean("right-click-enchanting-table");
    }

    public String getMenuName() {
        var path = "menu-name";
        final String defaultName = "EnchantGUI";

        if (config.getString(path, defaultName).length() > 32) {
            return config.getString(path, defaultName).substring(0, 32);
        }

        return config.getString(path, defaultName);
    }

    public boolean getShowPerItem() {
        return config.getBoolean("show-per-item");
    }

    @NotNull
    public Set<String> getDisabledEnchants() {
        List<String> disabled = config.getStringList("disabled-enchants");
        Set<String> result = new HashSet<>();
        for (String key : disabled) {
            if (key != null && !key.isBlank()) {
                result.add(key.toLowerCase(Locale.ROOT).trim());
            }
        }
        return result;
    }

    public @NotNull String[] getEnchantLevels(@NotNull Enchantment enchantment) {
        String path = enchantment.getKey().toString().toLowerCase();
        path = path.split(":")[1];
        path = normalizeEnchantKey(path);
        EnchantGUIPlugin.debug(path);
        Map<String, Object> enchantMap = config.getSection(path).getStringRouteMappedValues(false);
        String[] enchantLevels = new String[enchantMap.size()];

        var position = 0;
        for (Map.Entry<String, Object> entry : enchantMap.entrySet()) {
            enchantLevels[position] = entry.getKey();
            position++;
        }

        return enchantLevels;
    }

    /**
     * Maps enchantment registry key names to the legacy config key names.
     * Minecraft 1.20.5 renamed several enchantment registry keys:
     *   binding_curse → binding_curve
     *   vanishing_curse → vanishing_curve
     * The config.yml uses the legacy names, so we map back when looking up.
     */
    private static String normalizeEnchantKey(String key) {
        return switch (key) {
            case "binding_curve" -> "binding_curse";
            case "vanishing_curve" -> "vanishing_curse";
            default -> key;
        };
    }

    public String getPaymentType() {
        return config.getString("payment-currency", "xp");
    }

    public PaymentStrategy getPaymentStrategy() {
        if (economy == null) {
            switch (getPaymentType().toLowerCase()) {
                case "money" -> economy = new VaultPayment();
                case "xp" -> economy = new XPPayment();
                case "playerpoints" -> economy = new PlayerPointsPayment();
                case "disable" -> economy = new NullPayment();
                default -> {
                    final Material possibleMaterial = checkMaterialCurrency();
                    if (possibleMaterial == Material.AIR) {
                        economy = new NullPayment();
                    } else {
                        economy = new MaterialPayment(possibleMaterial);
                    }
                }
            }
        }
        return economy;
    }

    private Material checkMaterialCurrency() {
        final String paymentType = getPaymentType();
        if (paymentType.startsWith("material")) {
            final String possibleMaterial = paymentType.split(":")[1];
            return Material.matchMaterial(possibleMaterial.toUpperCase());
        }

        EnchantGUIPlugin.getInstance().getLogger().warning(() -> "Could not find matching material.");
        return Material.AIR;
    }

    public String getLanguage() {
        return config.getString("language", "en");
    }
}
