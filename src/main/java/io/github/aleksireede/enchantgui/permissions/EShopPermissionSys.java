package io.github.aleksireede.enchantgui.permissions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EShopPermissionSys {
    private EShopPermissionSys() {
        throw new UnsupportedOperationException();
    }
    public static final String USE = "eshop.use";
    public static final String ENCHANTING_TABLE = "eshop.enchanting-table";
    public static final String TOGGLE = "eshop.enchanting-table.toggle";
    public static final String RELOAD = "eshop.reload";

    private static final String BASE = "eshop.enchants.";

    public static boolean hasEnchantPermission(final @NotNull Player player, final Enchantment enchantment, int level) {
        if (player.isOp()) return true;
        String enchantmentName = (enchantment.getKey().toString().toLowerCase()).split(":")[1];
        String perm = getEnchantmentLevelPermission(enchantmentName, level);

        return player.hasPermission(perm) || player.hasPermission(BASE + enchantmentName + ".all") || player.hasPermission(BASE + "all");
    }

    @Contract(pure = true)
    private static @NotNull String getEnchantmentPermission(final String name) {
        return BASE + name;
    }

    @Contract(pure = true)
    private static @NotNull String getEnchantmentLevelPermission(final String name, int level) {
        return getEnchantmentPermission(name) + "." + level;
    }

}
