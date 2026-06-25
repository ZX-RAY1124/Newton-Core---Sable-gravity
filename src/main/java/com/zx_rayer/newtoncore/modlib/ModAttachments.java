package com.zx_rayer.newtoncore.modlib;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.zx_rayer.newtoncore.NewtonCore.MODID;

/**
 * 集中管理所有 AttachmentType。
 * AttachmentType 用于将自定义数据附加到 Chunk、Entity、ItemStack 等对象上。
 */
public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    /**
     * 向量场 AttachmentType —— 每个 Chunk 存储一个完整的 16×384×16 向量场。
     * <p>
     * 使用方式：
     * <pre>{@code
     *   VectorFieldData data = chunk.getData(ModAttachments.VECTOR_FIELD.get());
     *   data.setVector(localX, localY, localZ, vx, vy, vz);
     *   chunk.setData(ModAttachments.VECTOR_FIELD.get(), data);
     * }</pre>
     */
    public static final Supplier<AttachmentType<VectorFieldData>> VECTOR_FIELD =
            ATTACHMENT_TYPES.register(
                    "vector_field",
                    () -> AttachmentType.builder(VectorFieldData::new)
                            .serialize(VectorFieldData.CODEC)
                            .build()
            );
}