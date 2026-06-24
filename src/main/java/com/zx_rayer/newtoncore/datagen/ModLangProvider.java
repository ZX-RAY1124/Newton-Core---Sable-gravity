package com.zx_rayer.newtoncore.datagen;

import com.zx_rayer.newtoncore.modlib.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import static com.zx_rayer.newtoncore.NewtonCore.MODID;

public class ModLangProvider extends  LanguageProvider{
    public final langselection section;

    public ModLangProvider(PackOutput output, langselection selection) {
        super(output, MODID, selection.getCode());
        this.section = selection;
    }

    @Override
    protected void addTranslations(){
        switch (this.section){
            case EN_US -> {
                add(ModItems.GRAVITY_CORE.get(), "Gravity Core");
                add("itemGroup.newtoncore", "Newton Core");

            }
            case ZH_CN -> {
                add(ModItems.GRAVITY_CORE.get(), "引力核心");
                add("itemGroup.newtoncore", "牛顿核心");
            }
        }
    }



}
