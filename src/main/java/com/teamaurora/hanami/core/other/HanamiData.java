package com.teamaurora.hanami.core.other;

import com.teamabnormals.abnormals_core.core.utils.DataUtils;
import com.teamaurora.hanami.common.data.SakuraBlossomDispenseBehavior;
import com.teamaurora.hanami.core.registry.HanamiBlocks;
import com.teamaurora.hanami.core.registry.HanamiItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class HanamiData {
    public static void registerDispenserBehaviors() {
        DispenserBlock.registerDispenseBehavior(HanamiItems.SAKURA_FLOWER.get(), new SakuraBlossomDispenseBehavior());
    }

    @SuppressWarnings("deprecation")
    public static void registerCompostables() {
        DataUtils.registerCompostable(0.3F, HanamiBlocks.SAKURA_LEAVES.get());
        DataUtils.registerCompostable(0.3F, HanamiBlocks.SAKURA_SAPLING.get());
        DataUtils.registerCompostable(0.3F, HanamiBlocks.SAKURA_LEAF_CARPET.get());

        DataUtils.registerCompostable(0.65F, HanamiItems.SAKURA_CHERRIES.get());
        DataUtils.registerCompostable(1F, HanamiItems.SAKURA_CHERRY_PIE.get());
        DataUtils.registerCompostable(1F, HanamiBlocks.BLACK_FOREST_GATEAU.get());

        DataUtils.registerCompostable(0.65F, HanamiItems.SAKURA_FLOWER.get());

        DataUtils.registerCompostable(0.65F, HanamiBlocks.AZALEA_BUSH.get());
    }

    public static void registerFlammables() {
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_LEAVES.get(), 30, 60);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_LOG.get(), 5, 5);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_WOOD.get(), 5, 5);
        DataUtils.registerFlammable(HanamiBlocks.STRIPPED_SAKURA_LOG.get(), 5, 5);
        DataUtils.registerFlammable(HanamiBlocks.STRIPPED_SAKURA_WOOD.get(), 5, 5);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_PLANKS.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_SLAB.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_STAIRS.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_FENCE.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_FENCE_GATE.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.VERTICAL_SAKURA_PLANKS.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_LEAF_CARPET.get(), 30, 60);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_VERTICAL_SLAB.get(), 5, 20);
        DataUtils.registerFlammable(HanamiBlocks.SAKURA_BOOKSHELF.get(), 5, 20);
    }
}
