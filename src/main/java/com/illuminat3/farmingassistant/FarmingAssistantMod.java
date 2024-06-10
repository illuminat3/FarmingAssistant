package com.illuminat3.farmingassistant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class FarmingAssistantMod implements ModInitializer {

    private static final Map<Block, Item> CROP_TO_SEED_MAP = new HashMap<>();

    static {
        CROP_TO_SEED_MAP.put(Blocks.WHEAT, Items.WHEAT_SEEDS);
        CROP_TO_SEED_MAP.put(Blocks.CARROTS, Items.CARROT);
        CROP_TO_SEED_MAP.put(Blocks.POTATOES, Items.POTATO);
        CROP_TO_SEED_MAP.put(Blocks.BEETROOTS, Items.BEETROOT_SEEDS);
        CROP_TO_SEED_MAP.put(Blocks.COCOA, Items.COCOA_BEANS);
        CROP_TO_SEED_MAP.put(Blocks.NETHER_WART, Items.NETHER_WART);
        CROP_TO_SEED_MAP.put(Blocks.CRIMSON_FUNGUS, Items.CRIMSON_FUNGUS);
        CROP_TO_SEED_MAP.put(Blocks.WARPED_FUNGUS, Items.WARPED_FUNGUS);
        CROP_TO_SEED_MAP.put(Blocks.BROWN_MUSHROOM, Items.BROWN_MUSHROOM);
        CROP_TO_SEED_MAP.put(Blocks.RED_MUSHROOM, Items.RED_MUSHROOM);
    }

    @Override
    public void onInitialize() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient && CROP_TO_SEED_MAP.containsKey(state.getBlock())) {
                ItemStack seedStack = findSeedInInventory(player, state.getBlock());
                if (seedStack != null) {
                    world.setBlockState(pos, state.getBlock().getDefaultState());
                    seedStack.decrement(1);
                }
            }
        });
    }

    private ItemStack findSeedInInventory(PlayerEntity player, Block block) {
        Item seedItem = CROP_TO_SEED_MAP.get(block);
        if (seedItem != null) {
            return findItemInInventory(player, seedItem);
        }
        return null;
    }

    private ItemStack findItemInInventory(PlayerEntity player, Item item) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return stack;
            }
        }
        return null;
    }
}
