package com.zx_rayer.newtoncore.modlib;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodec;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;

/**
 * 存储单个 Chunk 内每个方块的向量场数据。
 * 数据结构：field[x][y][z] = float[] {vx, vy, vz}
 * 其中 x: 0-15, y: 0-383 (覆盖世界高度 -64 到 319), z: 0-15
 * <p>
 * 警告：每个 Chunk 存储 16*384*16 = 98,304 个向量 ≈ 1.18 MB
 * 大量 Chunk 加载时可能导致显著内存占用和存档膨胀。
 */
public class VectorFieldData {

    public static final int CHUNK_SIZE_X = 16;
    public static final int CHUNK_SIZE_Y = 384;
    public static final int CHUNK_SIZE_Z = 16;

    public static final int MIN_Y = -64;

    private final float[][][][] field;

    public VectorFieldData() {
        this.field = new float[CHUNK_SIZE_X][CHUNK_SIZE_Y][CHUNK_SIZE_Z][3];
    }

    public VectorFieldData(float[][][][] field) {
        this.field = field;
    }

    /**
     * 获取指定方块位置（世界坐标）的向量
     */
    public float[] getVector(int worldX, int worldY, int worldZ) {
        int localX = worldX & 15;
        int localY = worldY - MIN_Y;
        int localZ = worldZ & 15;

        if (localY < 0 || localY >= CHUNK_SIZE_Y) {
            return new float[]{0, 0, 0};
        }
        return field[localX][localY][localZ];
    }

    /**
     * 获取指定局部坐标的向量
     */
    public float[] getVectorLocal(int localX, int localY, int localZ) {
        if (localX < 0 || localX >= CHUNK_SIZE_X
                || localY < 0 || localY >= CHUNK_SIZE_Y
                || localZ < 0 || localZ >= CHUNK_SIZE_Z) {
            return new float[]{0, 0, 0};
        }
        return field[localX][localY][localZ];
    }

    /**
     * 设置指定局部坐标的向量
     */
    public void setVector(int localX, int localY, int localZ, float vx, float vy, float vz) {
        if (localX >= 0 && localX < CHUNK_SIZE_X
                && localY >= 0 && localY < CHUNK_SIZE_Y
                && localZ >= 0 && localZ < CHUNK_SIZE_Z) {
            field[localX][localY][localZ][0] = vx;
            field[localX][localY][localZ][1] = vy;
            field[localX][localY][localZ][2] = vz;
        }
    }

    /**
     * 获取内部原始数据（谨慎使用）
     */
    public float[][][][] getRawField() {
        return field;
    }

    /**
     * 创建一个副本
     */
    public VectorFieldData copy() {
        float[][][][] newField = new float[CHUNK_SIZE_X][CHUNK_SIZE_Y][CHUNK_SIZE_Z][3];
        for (int x = 0; x < CHUNK_SIZE_X; x++) {
            for (int y = 0; y < CHUNK_SIZE_Y; y++) {
                for (int z = 0; z < CHUNK_SIZE_Z; z++) {
                    System.arraycopy(field[x][y][z], 0, newField[x][y][z], 0, 3);
                }
            }
        }
        return new VectorFieldData(newField);
    }

    // ==================== 序列化 ====================

    /**
     * 将整个向量场扁平化为 float 数组，用于序列化。
     * 顺序：x=0..15, y=0..383, z=0..15, component=0..2
     */
    private float[] flatten() {
        float[] flat = new float[CHUNK_SIZE_X * CHUNK_SIZE_Y * CHUNK_SIZE_Z * 3];
        int index = 0;
        for (int x = 0; x < CHUNK_SIZE_X; x++) {
            for (int y = 0; y < CHUNK_SIZE_Y; y++) {
                for (int z = 0; z < CHUNK_SIZE_Z; z++) {
                    flat[index++] = field[x][y][z][0];
                    flat[index++] = field[x][y][z][1];
                    flat[index++] = field[x][y][z][2];
                }
            }
        }
        return flat;
    }

    private static VectorFieldData unflatten(float[] flat) {
        float[][][][] field = new float[CHUNK_SIZE_X][CHUNK_SIZE_Y][CHUNK_SIZE_Z][3];
        int index = 0;
        for (int x = 0; x < CHUNK_SIZE_X; x++) {
            for (int y = 0; y < CHUNK_SIZE_Y; y++) {
                for (int z = 0; z < CHUNK_SIZE_Z; z++) {
                    field[x][y][z][0] = flat[index++];
                    field[x][y][z][1] = flat[index++];
                    field[x][y][z][2] = flat[index++];
                }
            }
        }
        return new VectorFieldData(field);
    }

    // 将 float[] 转为 byte[] 用于 NBT 序列化（更紧凑）
    public byte[] toBytes() {
        float[] flat = flatten();
        byte[] bytes = new byte[flat.length * 4];
        for (int i = 0; i < flat.length; i++) {
            int bits = Float.floatToIntBits(flat[i]);
            bytes[i * 4] = (byte) (bits >> 24);
            bytes[i * 4 + 1] = (byte) (bits >> 16);
            bytes[i * 4 + 2] = (byte) (bits >> 8);
            bytes[i * 4 + 3] = (byte) (bits);
        }
        return bytes;
    }

    public static VectorFieldData fromBytes(byte[] bytes) {
        float[] flat = new float[bytes.length / 4];
        for (int i = 0; i < flat.length; i++) {
            int bits = ((bytes[i * 4] & 0xFF) << 24)
                    | ((bytes[i * 4 + 1] & 0xFF) << 16)
                    | ((bytes[i * 4 + 2] & 0xFF) << 8)
                    | (bytes[i * 4 + 3] & 0xFF);
            flat[i] = Float.intBitsToFloat(bits);
        }
        return unflatten(flat);
    }

    // Codec：用于 NBT 存档序列化
    public static final Codec<VectorFieldData> CODEC =
            Codec.BYTE_ARRAY.xmap(VectorFieldData::fromBytes, VectorFieldData::toBytes);

    // StreamCodec：用于网络同步（如有需要）
    public static final StreamCodec<ByteBuf, VectorFieldData> STREAM_CODEC =
            ByteBufCodec.BYTE_ARRAY.map(VectorFieldData::fromBytes, VectorFieldData::toBytes);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VectorFieldData that)) return false;
        return Arrays.deepEquals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(field);
    }
}