package me.zombie_striker.verticality.event;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerInventoryItemCollectListener implements EventListener<PickupItemEvent> {

    @Override
    public @NotNull Class<PickupItemEvent> eventType() {
        return PickupItemEvent.class;
    }

    @Override
    public @NotNull Result run(PickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return Result.SUCCESS;
        }
        ItemEntity target = event.getItemEntity();

        ItemStack itemStack = target.getItemStack();
        PlayerInventory inventory = player.getInventory();

        // Try to add item to player inventory
        boolean added = inventory.addItemStack(itemStack);

        // If successfully added, remove the item entity
        if (added) {
            target.remove();
        }

        return Result.SUCCESS;
    }
}
