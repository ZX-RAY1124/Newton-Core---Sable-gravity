package com.zx_rayer.newtoncore;

import com.zx_rayer.newtoncore.datagen.ModBlockStateProvider;
import com.zx_rayer.newtoncore.datagen.ModItemModelProvider;
import com.zx_rayer.newtoncore.datagen.ModLangProvider;
import com.zx_rayer.newtoncore.datagen.langselection;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

@EventBusSubscriber(modid = MODID)
public class ModDataGeneration {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        //generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLangProvider(packOutput, langselection.EN_US));
        generator.addProvider(event.includeClient(), new ModLangProvider(packOutput, langselection.ZH_CN));

    }

}
