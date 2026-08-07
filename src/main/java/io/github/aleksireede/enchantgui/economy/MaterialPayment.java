package io.github.aleksireede.enchantgui.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class MaterialPayment implements PaymentStrategy {
    private final Material material;

    public MaterialPayment(Material material) {
        this.material = material;
    }

    @Override
    public void withdraw(@NotNull Player player, double amount) {
        int needed = (int) amount;
        if (!hasSufficientFunds(player, needed)) {
            return;
        }

        List<ItemStack> matchingStacks = Stream.of(player.getInventory().getContents())
                .filter(stack -> stack != null && stack.isSimilar(new ItemStack(material)))
                .sorted(Comparator.comparingInt(ItemStack::getAmount))
                .toList();

        int remaining = needed;
        for (ItemStack stack : matchingStacks) {
            int stackAmount = stack.getAmount();
            if (remaining <= stackAmount) {
                stack.setAmount(stackAmount - remaining);
                break;
            }
            remaining -= stackAmount;
            stack.setAmount(0);
        }

        player.updateInventory();
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, double amount) {
        final int count = Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getType() == material)
                .mapToInt(ItemStack::getAmount)
                .sum();

        return count >= (int) amount;
    }

    @Override
    public String name() {
        return "MaterialPayment";
    }

    @Override
    public String getCurrency() {
        return material.name().toLowerCase();
    }
}
