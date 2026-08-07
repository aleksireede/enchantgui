package io.github.aleksireede.enchantgui.event;

import io.github.aleksireede.enchantgui.EnchantGUIPlugin;
import io.github.aleksireede.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class EventManager implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        // Fired once per hand; off-hand would open then immediately close the GUI.
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (EnchantGUIPlugin.getInstance().getMainConfig().isNotRightClickEnchantingTable()) {
            return;
        }
        if (EnchantGUIPlugin.getInstance().getToggleRightClickPlayers().contains(e.getPlayer().getUniqueId())) {
            return;
        }
        if (!e.getPlayer().hasPermission(EShopPermissionSys.ENCHANTING_TABLE)) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() == null) {
            return;
        }
        if (e.getClickedBlock().getType() != Material.ENCHANTING_TABLE) {
            return;
        }

        e.setCancelled(true);
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);
        EnchantGUIPlugin.getInstance().getShopMenu().showMainMenu(e.getPlayer());
    }

}
