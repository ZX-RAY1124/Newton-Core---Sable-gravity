package com.zx_rayer.newtoncore.modlib;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

public class ModItems {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<BlockItem> GRAVITY_CORE = ITEMS.registerSimpleBlockItem("gravity_core", ModBlocks.GRAVITY_CORE_BLOCK);

    public static void listener(IEventBus ModEventbus){
        ITEMS.register(ModEventbus);
    }

}
