package com.zx_rayer.newtoncore.modlib;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

public class ModItems {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> GRAVITY_CORE = ITEMS.registerSimpleItem("gravity_core", new Item.Properties());

    public static void listener(IEventBus ModEventbus){
        ITEMS.register(ModEventbus);
    }

}
