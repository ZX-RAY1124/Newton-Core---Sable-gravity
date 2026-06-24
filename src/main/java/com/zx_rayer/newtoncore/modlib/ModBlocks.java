package com.zx_rayer.newtoncore.modlib;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;


public class ModBlocks {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> GRAVITY_CORE_BLOCK = BLOCKS.registerSimpleBlock("gravity_core_blocks");

    public static void listener(IEventBus ModEventbus) {
        BLOCKS.register(ModEventbus);
    }
}