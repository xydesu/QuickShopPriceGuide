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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.reloaded", "&a[系統] 價格表已重新載入！")));
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.usage", "&c用法: /priceguide <reload|物品名稱>")));
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
            
            String msgUnit = plugin.getConfig().getString("messages.recommendation_unit", "&a[系統] 單個推薦 - 出售: $%sell% | 收購: $%buy%");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgUnit
                    .replace("%sell%", String.format("%.2f", sellPrice))
                    .replace("%buy%", String.format("%.2f", buyPrice))));

            if (priceData.stackSell > 0 || priceData.stackBuy > 0) {
                double stackSellPrice = priceData.stackSell * priceManager.getGlobalSellMultiplier();
                double stackBuyPrice = priceData.stackBuy * priceManager.getGlobalBuyMultiplier();
                String msgStack = plugin.getConfig().getString("messages.recommendation_stack", "&a[系統] 整組推薦 - 出售: $%sell% | 收購: $%buy%");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msgStack
                        .replace("%sell%", String.format("%.2f", stackSellPrice))
                        .replace("%buy%", String.format("%.2f", stackBuyPrice))));
            }
        } else {
            String noDataMsg = plugin.getConfig().getString("messages.no_data", "&c[系統] 找不到此物品的推薦價格。");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noDataMsg));
        }

        return true;
    }
}
