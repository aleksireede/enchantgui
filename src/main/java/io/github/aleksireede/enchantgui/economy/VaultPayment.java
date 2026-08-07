package io.github.aleksireede.enchantgui.economy;

import io.github.aleksireede.enchantgui.EnchantGUIPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultPayment implements PaymentStrategy {
    private Economy econ;
    private final EnchantGUIPlugin plugin;

    @Override
    public String name() {
        return "MoneyPayment";
    }

    public VaultPayment() {
        this.plugin = EnchantGUIPlugin.getInstance();
        if (!setupEconomy()) {
            plugin.getLogger().severe("Dependency (Vault) not found. Disabling the plugin!");
            plugin.getLogger().warning("Please install vault and restart your server.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public void withdraw(@NotNull final Player player, final double amount) {
        if (!hasSufficientFunds(player, amount)) {
            return;
        }
        econ.withdrawPlayer(player, amount);
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Could not find vault");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().severe("Could not find Economy.class, missing economy plugin, install EssentialsX or something else.");
            return false;
        }

        econ = rsp.getProvider();
        return true;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull final Player player, final double amount) {
        return econ.has(player, amount);
    }

    @Override
    public String getCurrency() {
        return econ.currencyNameSingular().isEmpty() ? "$" : econ.currencyNameSingular();
    }
}
