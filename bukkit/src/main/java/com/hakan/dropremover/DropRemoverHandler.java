package com.hakan.dropremover;

import com.hakan.core.HCore;
import com.hakan.core.listener.HListenerAdapter;
import com.hakan.core.particle.HParticle;
import com.hakan.dropremover.commands.DropCommand;
import com.hakan.dropremover.configuration.DropRemoverConfiguration;
import com.hakan.dropremover.item.DroppedItem;
import com.hakan.dropremover.listeners.DropItemListeners;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DropRemoverHandler {

    public static int REMOVE_TIME;
    public static String EFFECT;
    public static String HOLOGRAM_TEXT;
    public static boolean EFFECT_ENABLED;
    public static boolean HOLOGRAM_ENABLED;

    private static List<DroppedItem> droppedItems;

    public static void initialize(DropRemoverPlugin plugin) {
        //CONFIGURATION
        DropRemoverConfiguration.initialize(plugin);
        DropRemoverHandler.loadValues();


        //CACHE
        DropRemoverHandler.droppedItems = new ArrayList<>();


        //TASKS
        HCore.syncScheduler().every(20)
                .run(() -> DropRemoverHandler.getDroppedItemsSafe().forEach(droppedItem -> {
                    if (droppedItem.isRemoved()) {
                        droppedItem.remove();
                        return;
                    }

                    droppedItem.updateItem();
                    if (droppedItem.canRemove()) {
                        Location location = droppedItem.getItem().getLocation();
                        HCore.playParticle(location.getWorld().getPlayers(), location,
                                new HParticle(DropRemoverHandler.EFFECT, 20, new Vector(0.2, 0.2, 0.2)));
                        droppedItem.remove();
                    }
                }));


        //BUKKIT
        HListenerAdapter.register(new DropItemListeners(plugin));
        HCore.registerCommands(new DropCommand("hdropremover", "dropremover", "hdrops", "drops"));
    }

    public static List<DroppedItem> getDroppedItemsSafe() {
        return new ArrayList<>(droppedItems);
    }

    public static List<DroppedItem> getDroppedItems() {
        return droppedItems;
    }

    public static void loadValues() {
        REMOVE_TIME = DropRemoverConfiguration.CONFIG.get("settings.remove-time");
        EFFECT = DropRemoverConfiguration.CONFIG.get("settings.effect.type");
        EFFECT_ENABLED = DropRemoverConfiguration.CONFIG.get("settings.effect.enabled");
        HOLOGRAM_TEXT = ChatColor.translateAlternateColorCodes('&', DropRemoverConfiguration.CONFIG.get("settings.hologram.text"));
        HOLOGRAM_ENABLED = DropRemoverConfiguration.CONFIG.get("settings.hologram.enabled");
    }
}