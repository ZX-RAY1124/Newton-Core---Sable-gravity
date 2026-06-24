package com.zx_rayer.newtoncore.datagen;

import com.zx_rayer.newtoncore.modlib.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

public class ModItemModelProvider extends ItemModelProvider{

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {  //方块模型
        super(output, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels(){
        basicItem(ModItems.GRAVITY_CORE.get());
    }

}
