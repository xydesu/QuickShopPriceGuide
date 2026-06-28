package me.xydesu.priceguide;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShopCreateListener implements Listener {

    private final PriceGuidePlugin plugin;
    private final PriceManager priceManager;

    public ShopCreateListener(PriceGuidePlugin plugin, PriceManager priceManager) {
        this.plugin = plugin;
        this.priceManager = priceManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        Material type = block.getType();
        if (type != Material.CHEST && type != Material.TRAPPED_CHEST && type != Material.BARREL && type != Material.SHULKER_BOX && !type.name().endsWith("_SHULKER_BOX")) {
            return;
        }



        String itemName = item.getType().name().toLowerCase();
        PriceManager.PriceData priceData = priceManager.getPrice(itemName);

        if (priceData != null) {
            double sellPrice = priceData.unitSell * priceManager.getGlobalSellMultiplier();
            double buyPrice = priceData.unitBuy * priceManager.getGlobalBuyMultiplier();
            
            String msgUnit = plugin.getConfig().getString("messages.recommendation_unit", "&8[&b市價指南&8] &7單個推薦 &8» &f出售: &a$%sell% &8| &f收購: &c$%buy%");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgUnit
                    .replace("%sell%", String.format("%.2f", sellPrice))
                    .replace("%buy%", String.format("%.2f", buyPrice))));

            if (item.getMaxStackSize() > 1 && (priceData.stackSell > 0 || priceData.stackBuy > 0)) {
                double stackSellPrice = priceData.stackSell * priceManager.getGlobalSellMultiplier();
                double stackBuyPrice = priceData.stackBuy * priceManager.getGlobalBuyMultiplier();
                String msgStack = plugin.getConfig().getString("messages.recommendation_stack", "&8[&b市價指南&8] &7整組推薦 &8» &f出售: &a$%sell% &8| &f收購: &c$%buy%");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgStack
                        .replace("%sell%", String.format("%.2f", stackSellPrice))
                        .replace("%buy%", String.format("%.2f", stackBuyPrice))));
            }

            plugin.getPendingPrices().put(player.getUniqueId(), sellPrice);
            org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getPendingPrices().remove(player.getUniqueId()), 20L * 30L);

            String autoHint = plugin.getConfig().getString("messages.auto_hint");
            if (autoHint != null && !autoHint.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', autoHint));
            }
        } else {
            String noDataMsg = plugin.getConfig().getString("messages.no_data");
            if (noDataMsg != null && !noDataMsg.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noDataMsg));
            }
        }
    }
}
