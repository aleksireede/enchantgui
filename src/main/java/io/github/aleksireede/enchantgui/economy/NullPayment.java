package io.github.aleksireede.enchantgui.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Used to disable payment.
 */
public class NullPayment implements PaymentStrategy {
    @Override
    public String name() {
        return "NullPayment";
    }

    @Override
    public void withdraw(@NotNull final Player player, final double amount) {
    }

    @Override
    public boolean hasSufficientFunds(@NotNull final Player player, final double amount) {
        return true;
    }

    @Override
    public String getCurrency() {
        return "";
    }
}
