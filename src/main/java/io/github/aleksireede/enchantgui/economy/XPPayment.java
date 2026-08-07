package io.github.aleksireede.enchantgui.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class XPPayment implements PaymentStrategy {
    @Override
    public String name() {
        return "XPPayment";
    }

    @Override
    public void withdraw(@NotNull final Player player, final double amount) {
        int levels = (int) amount;
        if (hasSufficientFunds(player, levels)) {
            player.giveExpLevels(-levels);
        }
    }

    @Override
    public boolean hasSufficientFunds(@NotNull final Player player, final double amount) {
        return player.getLevel() >= (int) amount;
    }

    @Override
    public String getCurrency() {
        return "levels";
    }
}
