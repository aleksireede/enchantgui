package io.github.aleksireede.enchantgui.economy;

import io.github.aleksireede.enchantgui.EnchantGUIPlugin;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author aleksireede
 */
public class PlayerPointsPayment implements PaymentStrategy {
    private final PlayerPointsAPI ppApi;

    public PlayerPointsPayment() {
        this.ppApi = EnchantGUIPlugin.getInstance().getPpApi();
    }

    @Override
    public void withdraw(@NotNull final Player player, final double amount) {
        int points = (int) amount;
        if (!hasSufficientFunds(player, points)) {
            return;
        }
        ppApi.take(player.getUniqueId(), points);
    }

    @Override
    public boolean hasSufficientFunds(@NotNull final Player player, final double amount) {
        return ppApi.look(player.getUniqueId()) >= (int) amount;
    }

    @Override
    public String name() {
        return "PlayerPointsPayment";
    }

    @Override
    public String getCurrency() {
        return "pp";
    }
}
