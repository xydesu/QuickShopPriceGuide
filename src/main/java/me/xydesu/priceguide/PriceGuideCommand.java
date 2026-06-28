package me.xydesu.priceguide;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PriceGuideCommand implements CommandExecutor {

    private final PriceGuidePlugin plugin;
    private final PriceManager priceManager;

    public PriceGuideCommand(PriceGuidePlugin plugin, PriceManager priceManager) {
        this.plugin = plugin;
        this.priceManager = priceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("priceguide.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission!");
                return true;
            }
            priceManager.loadPrices();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.reloaded", "&8[&b市價指南&8] &a價格表已成功重新載入！")));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players.");
            return true;
        }

        Player player = (Player) sender;
        String itemName = "";

        if (args.length == 0) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.usage", "&8[&b市價指南&8] &c用法: &f/priceguide <reload|物品名稱>")));
                return true;
            }
            itemName = item.getType().name().toLowerCase();
        } else {
            itemName = String.join("_", args).toLowerCase();
        }

        PriceManager.PriceData priceData = priceManager.getPrice(itemName);
        if (priceData != null) {
            double sellPrice = priceData.unitSell * priceManager.getGlobalSellMultiplier();
            double buyPrice = priceData.unitBuy * priceManager.getGlobalBuyMultiplier();
            
            String msgUnit = plugin.getConfig().getString("messages.recommendation_unit", "&8[&b市價指南&8] &7單個推薦 &8» &f出售: &a$%sell% &8| &f收購: &c$%buy%");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgUnit
                    .replace("%sell%", String.format("%.2f", sellPrice))
                    .replace("%buy%", String.format("%.2f", buyPrice))));

            if (priceData.stackSell > 0 || priceData.stackBuy > 0) {
                double stackSellPrice = priceData.stackSell * priceManager.getGlobalSellMultiplier();
                double stackBuyPrice = priceData.stackBuy * priceManager.getGlobalBuyMultiplier();
                String msgStack = plugin.getConfig().getString("messages.recommendation_stack", "&8[&b市價指南&8] &7整組推薦 &8» &f出售: &a$%sell% &8| &f收購: &c$%buy%");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgStack
                        .replace("%sell%", String.format("%.2f", stackSellPrice))
                        .replace("%buy%", String.format("%.2f", stackBuyPrice))));
            }
        } else {
            String noDataMsg = plugin.getConfig().getString("messages.no_data", "&8[&b市價指南&8] &c目前尚未有此物品的推薦價格資料。");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noDataMsg));
        }

        return true;
    }
}
