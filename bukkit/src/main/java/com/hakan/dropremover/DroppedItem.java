package com.hakan.dropremover;

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
        DropHandler.getDroppedItems().remove(this);
    }

    public void updateItem() {
        if (DropHandler.HOLOGRAM_ENABLED) {
            String amount = this.item.getItemStack().getAmount() + "";
            String type = DropHandler.getNameByMaterial(this.item.getItemStack().getType());
            String seconds = String.valueOf((int) Math.round((this.removeAt - System.currentTimeMillis()) / 1000.0));

            String customName = DropHandler.HOLOGRAM_TEXT
                    .replace("%material%", type)
                    .replace("%amount%", amount)
                    .replace("%time%", seconds);

            this.item.setCustomNameVisible(true);
            this.item.setCustomName(customName);
        }
    }
}