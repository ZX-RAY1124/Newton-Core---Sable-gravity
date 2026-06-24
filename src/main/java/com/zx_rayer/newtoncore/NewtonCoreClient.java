package com.zx_rayer.newtoncore;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// 此类不会在专用服务器上加载。从这里访问客户端代码是安全的。
@Mod(value = NewtonCore.MODID, dist = Dist.CLIENT)
// 你可以使用 EventBusSubscriber 来自动注册类中所有带有 @SubscribeEvent 注解的静态方法
@EventBusSubscriber(modid = NewtonCore.MODID, value = Dist.CLIENT)
public class NewtonCoreClient {
    public NewtonCoreClient(ModContainer container) {
        // 允许 NeoForge 为此 mod 的配置创建一个配置界面。
        // 配置界面的访问路径：进入 Mods 界面 > 点击你的 mod > 点击 config。
        // 不要忘记在 en_us.json 文件中为你的配置选项添加翻译。
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // 一些客户端设置代码
        NewtonCore.LOGGER.info("HELLO FROM CLIENT SETUP");
        NewtonCore.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}