package com.zx_rayer.newtoncore.modlib;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

public class ModCreativeTab {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRAVITY_CORE = CREATIVE_MODE_TABS.register("gravity_core", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.newtoncore")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.GRAVITY_CORE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.GRAVITY_CORE.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public static void listener(IEventBus ModEventbus){
        CREATIVE_MODE_TABS.register(ModEventbus);
    }

}
