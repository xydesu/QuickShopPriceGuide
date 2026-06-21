package me.xydesu.priceguide;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PriceManager {

    private final Plugin plugin;
    private final Map<String, PriceData> prices = new HashMap<>();
    private double globalSellMultiplier = 1.0;
    private double globalBuyMultiplier = 1.0;
    private double taxRate = 0.0;

    public PriceManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadPrices() {
        prices.clear();
        
        plugin.reloadConfig();
        globalSellMultiplier = plugin.getConfig().getDouble("multipliers.sell", 1.0);
        globalBuyMultiplier = plugin.getConfig().getDouble("multipliers.buy", 1.0);
        taxRate = plugin.getConfig().getDouble("settings.tax_rate", 0.0);

        File file = new File(plugin.getDataFolder(), "prices.json");
        if (!file.exists()) {
            plugin.getLogger().warning("prices.json not found! Please place the file in the plugin folder.");
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            for (String key : root.keySet()) {
                JsonObject itemData = root.getAsJsonObject(key);
                double unitBuy = itemData.has("unit_buy") ? itemData.get("unit_buy").getAsDouble() : 0.0;
                double unitSell = itemData.has("unit_sell") ? itemData.get("unit_sell").getAsDouble() : 0.0;
                double stackBuy = itemData.has("stack_buy") ? itemData.get("stack_buy").getAsDouble() : 0.0;
                double stackSell = itemData.has("stack_sell") ? itemData.get("stack_sell").getAsDouble() : 0.0;
                prices.put(key.toLowerCase(), new PriceData(unitBuy, unitSell, stackBuy, stackSell));
            }
            plugin.getLogger().info("Loaded " + prices.size() + " prices from prices.json");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load prices.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PriceData getPrice(String itemName) {
        return prices.get(itemName.toLowerCase());
    }

    public double getGlobalSellMultiplier() {
        return globalSellMultiplier * (1.0 + taxRate);
    }

    public double getGlobalBuyMultiplier() {
        return globalBuyMultiplier;
    }

    public static class PriceData {
        public final double unitBuy;
        public final double unitSell;
        public final double stackBuy;
        public final double stackSell;

        public PriceData(double unitBuy, double unitSell, double stackBuy, double stackSell) {
            this.unitBuy = unitBuy;
            this.unitSell = unitSell;
            this.stackBuy = stackBuy;
            this.stackSell = stackSell;
        }
    }
}
