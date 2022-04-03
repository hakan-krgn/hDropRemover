package com.hakan.dropremover.listeners;

import com.hakan.core.listener.HListenerAdapter;
import com.hakan.dropremover.DropRemoverHandler;
import com.hakan.dropremover.item.DroppedItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class DropItemListeners extends HListenerAdapter {

    public DropItemListeners(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        DroppedItem droppedItem = new DroppedItem(event.getItemDrop(), System.currentTimeMillis() + DropRemoverHandler.REMOVE_TIME * 1000L);
        droppedItem.updateItem();
        DropRemoverHandler.getDroppedItems().add(droppedItem);
    }
}