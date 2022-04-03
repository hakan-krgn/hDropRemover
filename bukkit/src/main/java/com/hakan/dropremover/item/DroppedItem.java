package com.hakan.dropremover.item;

import com.hakan.dropremover.DropRemoverHandler;
import org.bukkit.entity.Item;

public class DroppedItem {

    private final Item item;
    private final long removeAt;

    public DroppedItem(Item item, long removeAt) {
        this.item = item;
        this.removeAt = removeAt;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean isRemoved() {
        return this.item.isDead();
    }

    public boolean canRemove() {
        return this.removeAt <= System.currentTimeMillis();
    }

    public void remove() {
        this.item.remove();
        DropRemoverHandler.getDroppedItems().remove(this);
    }

    public void updateItem() {
        if (DropRemoverHandler.HOLOGRAM_ENABLED) {
            String type = this.item.getItemStack().getType().name();
            String amount = this.item.getItemStack().getAmount() + "";
            String seconds = String.valueOf((int) Math.round((this.removeAt - System.currentTimeMillis()) / 1000.0));

            String customName = DropRemoverHandler.HOLOGRAM_TEXT
                    .replace("%material%", type)
                    .replace("%amount%", amount)
                    .replace("%time%", seconds);

            this.item.setCustomNameVisible(true);
            this.item.setCustomName(customName);
        }
    }
}