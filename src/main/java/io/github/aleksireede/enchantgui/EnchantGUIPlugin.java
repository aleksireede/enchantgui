package io.github.aleksireede.enchantgui;

import co.aikar.commands.PaperCommandManager;
import io.github.aleksireede.enchantgui.commands.ShopCommand;
import io.github.aleksireede.enchantgui.config.EShopConfig;
import io.github.aleksireede.enchantgui.config.Enchants;
import io.github.aleksireede.enchantgui.event.EventManager;
import io.github.aleksireede.enchantgui.localization.LocalizationManager;
import io.github.aleksireede.enchantgui.menu.ShopMenu;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnchantGUIPlugin extends JavaPlugin {
    private static EnchantGUIPlugin instance;
    private final Set<UUID> toggleRightClickPlayers = new HashSet<>();

    private ShopMenu shopMenu;
    private EShopConfig config;
    private LocalizationManager lm;

    private PlayerPointsAPI ppApi;

    @Override
    public void onEnable() {
        setInstance(this);
        config = new EShopConfig();
        lm = new LocalizationManager();

        this.shopMenu = new ShopMenu(new Enchants());
        // Register event manager
        getServer().getPluginManager().registerEvents(new EventManager(), this);

        // Register command
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new ShopCommand());

        // Enable Metrics
        if (!getConfig().getBoolean("opt-out")) {
            new Metrics(this, 3871);
        }

        hookPlayerPoints();

        if (isPaperServer()) {
            getLogger().warning(() -> "You can ignore the message above about Commands API, we are aware, and are waiting on a fix from the commands library.");
        }

        getLogger().info(() -> getNameWithVersion() + " enabled!");
        getLogger().info(() -> getNameWithVersion() + " using: " + getMainConfig().getPaymentStrategy().name());
    }

    private String getNameWithVersion() {
        return "%s %s".formatted(getName(), getPluginMeta().getVersion());
    }

    @Override
    public void onDisable() {
        setInstance(null);
        getLogger().info(() -> getName() + " " + getPluginMeta().getVersion() + " disabled!");
    }

    public void onReload() {
        config.reload();

        hookPlayerPoints();
    }

    public static void debug(String msg) {
        if (EnchantGUIPlugin.getInstance().getMainConfig().getDebug()) {
            EnchantGUIPlugin.getInstance().getLogger().info(() -> String.format("DEBUG %s", msg));
        }
    }


    private void hookPlayerPoints() {
        if (!config.getPaymentType().equalsIgnoreCase("playerpoints")) {
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppApi = PlayerPoints.getInstance().getAPI();
        }
    }

    @NotNull
    public EShopConfig getMainConfig() {
        return config;
    }

    public static EnchantGUIPlugin getInstance() {
        return instance;
    }

    private static void setInstance(EnchantGUIPlugin instance) {
        EnchantGUIPlugin.instance = instance;
    }

    public LocalizationManager getLm() {
        return lm;
    }

    public Set<UUID> getToggleRightClickPlayers() {
        return toggleRightClickPlayers;
    }

    public PlayerPointsAPI getPpApi() {
        return ppApi;
    }

    public ShopMenu getShopMenu() {
        return shopMenu;
    }

    public boolean isPaperServer() {
        try {
            Class.forName("io.papermc.paper.ServerBuildInfo");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}