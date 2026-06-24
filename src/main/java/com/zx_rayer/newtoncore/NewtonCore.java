package com.zx_rayer.newtoncore;

import com.zx_rayer.newtoncore.modlib.ModBlocks;
import com.zx_rayer.newtoncore.modlib.ModCreativeTab;
import com.zx_rayer.newtoncore.modlib.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// 此处的值应与 META-INF/neoforge.mods.toml 文件中的条目匹配
@Mod(NewtonCore.MODID)
public class NewtonCore {
    // 在一个公共位置定义 mod id，方便各处引用
    public static final String MODID = "newtoncore";
    // 直接引用一个 slf4j 日志记录器
    public static final Logger LOGGER = LogUtils.getLogger();
    // 创建一个 Deferred Register 来持有方块，所有方块都将注册在 "newtoncore" 命名空间下
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // 创建一个 Deferred Register 来持有物品，所有物品都将注册在 "newtoncore" 命名空间下
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // 创建一个 Deferred Register 来持有创造模式标签页，所有标签页都将注册在 "newtoncore" 命名空间下
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 创建一个 id 为 "newtoncore:example_block" 的新方块，由命名空间和路径组合而成
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // 创建一个 id 为 "newtoncore:example_block" 的新 BlockItem，由命名空间和路径组合而成
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // 创建一个 id 为 "newtoncore:example_id" 的新食物物品，营养值为 1，饱和度为 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // 创建一个 id 为 "newtoncore:example_tab" 的创造模式标签页，用于存放示例物品，位置在战斗标签页之后
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.newtoncore")) // 创造模式标签页标题的语言键
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // 将示例物品添加到标签页中。对于你自己的标签页，建议使用此方法而非事件
            }).build());

    // mod 类的构造函数是模组加载时首先运行的代码。
    // FML 会识别某些参数类型（如 IEventBus 或 ModContainer）并自动传入。
    public NewtonCore(IEventBus modEventBus, ModContainer modContainer) {
        // 注册 commonSetup 方法以进行模组加载
        modEventBus.addListener(this::commonSetup);

        // 将 Deferred Register 注册到 mod 事件总线，以便方块被注册
        BLOCKS.register(modEventBus);
        // 将 Deferred Register 注册到 mod 事件总线，以便物品被注册
        ITEMS.register(modEventBus);


        /*

        这里是所有外部文件接入点
         */
        ModItems.listener(modEventBus);
        // 将 Deferred Register 注册到 mod 事件总线，以便标签页被注册
        CREATIVE_MODE_TABS.register(modEventBus);

        // 注册我们自己以监听服务器和其他我们感兴趣的游戏事件。
        // 注意：这仅在且必须在我们希望 *此* 类（NewtonCore）直接响应事件时才需要。
        // 如果此类中没有 @SubscribeEvent 注解的函数（如下方的 onServerStarting()），则不要添加此行。
        NeoForge.EVENT_BUS.register(this);

        // 将物品注册到创造模式标签页
        modEventBus.addListener(this::addCreative);

        // 注册我们 mod 的 ModConfigSpec，以便 FML 为我们创建和加载配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // 一些通用设置代码
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // 将示例方块物品添加到建筑方块标签页
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // 你可以使用 SubscribeEvent 注解，让事件总线自动发现要调用的方法
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 服务器启动时执行一些操作
        LOGGER.info("HELLO from server starting");
    }
}