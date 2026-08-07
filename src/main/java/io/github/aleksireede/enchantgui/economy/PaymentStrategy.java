package io.github.aleksireede.enchantgui.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PaymentStrategy {
    void withdraw(@NotNull Player player, double amount);

    boolean hasSufficientFunds(@NotNull Player player, double amount);

    String name();

    String getCurrency();
}
