package me.xydesu.priceguide;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatInterceptor implements Listener {

    private final PriceGuidePlugin plugin;

    public ChatInterceptor(PriceGuidePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().trim().equalsIgnoreCase("auto") || event.getMessage().trim().equalsIgnoreCase("rec")) {
            Double pendingPrice = plugin.getPendingPrices().get(event.getPlayer().getUniqueId());
            if (pendingPrice != null) {
                event.setMessage(String.valueOf(pendingPrice));
                plugin.getPendingPrices().remove(event.getPlayer().getUniqueId());
            }
        }
    }
}
