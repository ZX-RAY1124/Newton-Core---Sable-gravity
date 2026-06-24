package com.zx_rayer.newtoncore.datagen;

import com.zx_rayer.newtoncore.modlib.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels(){
        simpleBlockWithItem(ModBlocks.GRAVITY_CORE_BLOCK.get(), cubeAll(ModBlocks.GRAVITY_CORE_BLOCK.get()));
    }
}
