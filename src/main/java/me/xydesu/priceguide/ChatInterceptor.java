package me.xydesu.priceguide;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatInterceptor implements Listener {

    private final PriceGuidePlugin plugin;

    public ChatInterceptor(PriceGuidePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().trim().equalsIgnoreCase("/auto") || event.getMessage().trim().equalsIgnoreCase("/rec")) {
            Double pendingPrice = plugin.getPendingPrices().get(event.getPlayer().getUniqueId());
            if (pendingPrice != null) {
                event.setCancelled(true);
                // 讓玩家自動說出價格，這樣 QuickShop 就能正確攔截到數字
                org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
                    event.getPlayer().chat(String.valueOf(pendingPrice));
                });
                plugin.getPendingPrices().remove(event.getPlayer().getUniqueId());
            }
        }
    }
}
