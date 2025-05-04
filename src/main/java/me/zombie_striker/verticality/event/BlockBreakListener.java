package me.zombie_striker.verticality.event;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements EventListener<PlayerBlockBreakEvent> {

    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockBreakEvent event) {
        Block brokenBlock = event.getBlock();

        // Create an item drop corresponding to the broken block
        ItemStack itemStack = getItemStackFromBlock(brokenBlock);

        // Skip if the block shouldn't drop anything
        if (!itemStack.isAir()) {
            // Create the item entity
            ItemEntity itemEntity = new ItemEntity(itemStack);

            // Set the position to the center of the broken block
            Pos blockPos = event.getBlockPosition().asVec().asPosition();
            Pos centerPos = new Pos(
                    blockPos.x() + 0.5,
                    blockPos.y() + 0.5,
                    blockPos.z() + 0.5
            );
            itemEntity.setInstance(event.getInstance(), centerPos);

            // Add some random velocity for natural effect
            itemEntity.setVelocity(new Vec(Math.random() * 0.2 - 0.1,
                    Math.random() * 0.2 + 0.1,
                    Math.random() * 0.2 - 0.1
            ));
        }

        return Result.SUCCESS;
    }

    /**
     * Converts a block to its corresponding item stack.
     *
     * @param block The broken block
     * @return ItemStack representing the block, or AIR if it shouldn't drop
     */
    private ItemStack getItemStackFromBlock(Block block) {
        // Get the material from the block
        // In Minestom, some blocks don't drop themselves but drop other items
        // This is a simplified conversion - you might need a more complex mapping
        Material material = Material.fromKey(block.name());

        // Skip air or blocks that shouldn't drop (like glass, depending on your game rules)
        if (material == null || block.isAir()) {
            return ItemStack.AIR;
        }

        // Return 1 of the item
        return ItemStack.of(material, 1);
    }
}
