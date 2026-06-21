package me.xydesu.priceguide;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PriceGuidePlugin extends JavaPlugin {

    private PriceManager priceManager;
    private final Map<UUID, Double> pendingPrices = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        priceManager = new PriceManager(this);
        priceManager.loadPrices();

        getCommand("priceguide").setExecutor(new PriceGuideCommand(this, priceManager));
        getServer().getPluginManager().registerEvents(new ShopCreateListener(this, priceManager), this);
        getServer().getPluginManager().registerEvents(new ChatInterceptor(this), this);

        getLogger().info("QuickShopPriceGuide enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("QuickShopPriceGuide disabled.");
    }

    public Map<UUID, Double> getPendingPrices() {
        return pendingPrices;
    }
}
