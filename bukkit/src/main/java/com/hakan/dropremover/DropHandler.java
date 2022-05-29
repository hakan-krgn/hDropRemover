package com.hakan.dropremover;

import com.hakan.core.HCore;
import com.hakan.core.particle.HParticle;
import com.hakan.dropremover.commands.DropCommand;
import com.hakan.dropremover.configuration.DropRemoverConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DropHandler {

    public static int REMOVE_TIME;
    public static String EFFECT;
    public static String HOLOGRAM_TEXT;
    public static boolean EFFECT_ENABLED;
    public static boolean HOLOGRAM_ENABLED;

    private static List<DroppedItem> droppedItems;
    private static Map<Material, String> itemNames;

    public static void initialize(DropRemoverPlugin plugin) {

        //CONFIGURATION
        DropRemoverConfiguration.initialize(plugin);
        DropHandler.loadValues();


        //CACHE
        DropHandler.droppedItems = new ArrayList<>();
        DropHandler.itemNames = new HashMap<>();
        DropHandler.loadItems();


        //TASKS
        HCore.syncScheduler().every(20)
                .run(() -> DropHandler.getDroppedItemsSafe().forEach(droppedItem -> {
                    if (droppedItem.isRemoved()) {
                        droppedItem.remove();
                        return;
                    }

                    droppedItem.updateItem();
                    if (droppedItem.canRemove()) {
                        Location location = droppedItem.getItem().getLocation();
                        HCore.playParticle(location.getWorld().getPlayers(), location,
                                new HParticle(DropHandler.EFFECT, 20, new Vector(0.2, 0.2, 0.2)));
                        droppedItem.remove();
                    }
                }));


        //BUKKIT
        HCore.registerCommands(new DropCommand());
        HCore.registerEvent(PlayerDropItemEvent.class)
                .consume(event -> {
                    DroppedItem droppedItem = new DroppedItem(event.getItemDrop(), System.currentTimeMillis() + DropHandler.REMOVE_TIME * 1000L);
                    droppedItem.updateItem();
                    DropHandler.getDroppedItems().add(droppedItem);
                });
    }

    public static List<DroppedItem> getDroppedItemsSafe() {
        return new ArrayList<>(droppedItems);
    }

    public static List<DroppedItem> getDroppedItems() {
        return droppedItems;
    }

    public static Map<Material, String> getItemNamesSafe() {
        return new HashMap<>(itemNames);
    }

    public static Map<Material, String> getItemNames() {
        return itemNames;
    }

    public static Optional<String> findNameByMaterial(Material material) {
        return Optional.ofNullable(DropHandler.itemNames.get(material));
    }

    public static String getNameByMaterial(Material material) {
        return findNameByMaterial(material).orElse(material.name());
    }

    public static void loadValues() {
        REMOVE_TIME = DropRemoverConfiguration.CONFIG.get("settings.remove-time");
        EFFECT = DropRemoverConfiguration.CONFIG.get("settings.effect.type");
        EFFECT_ENABLED = DropRemoverConfiguration.CONFIG.get("settings.effect.enabled");
        HOLOGRAM_TEXT = ChatColor.translateAlternateColorCodes('&', DropRemoverConfiguration.CONFIG.get("settings.hologram.text"));
        HOLOGRAM_ENABLED = DropRemoverConfiguration.CONFIG.get("settings.hologram.enabled");
    }

    public static void loadItems() {
        DropRemoverConfiguration configuration = DropRemoverConfiguration.getByPath("items.yml");
        for (Material material : Material.values()) {
            if (configuration.get(material.name()) == null) {
                configuration.set(material.name(), material.name());
            }
        }

        for (String type : configuration.get("", MemorySection.class).getKeys(false)) {
            try {
                Material material = Material.valueOf(type);
                String name = configuration.get(type);
                DropHandler.itemNames.put(material, name);
            } catch (Exception e) {
                configuration.set(type, null);
            }
        }
        configuration.save();
    }
}