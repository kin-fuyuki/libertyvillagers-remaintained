package com.gitsh01.libertyvillagers.mixin;

import com.gitsh01.libertyvillagers.acessors.getFreeTicketsAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ChunkErrorHandler;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.poi.PointOfInterestSet;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.storage.ChunkPosKeyedStorage;
import net.minecraft.world.storage.SerializingRegionBasedStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(PointOfInterestStorage.class)
public class PointOfInterestStorageMixin extends SerializingRegionBasedStorage<PointOfInterestSet, PointOfInterestSet.Serialized> implements getFreeTicketsAccessor {
    public PointOfInterestStorageMixin(ChunkPosKeyedStorage storageAccess, Codec<PointOfInterestSet.Serialized> codec, Function<PointOfInterestSet, PointOfInterestSet.Serialized> serializer, BiFunction<PointOfInterestSet.Serialized, Runnable, PointOfInterestSet> deserializer, Function<Runnable, PointOfInterestSet> factory, DynamicRegistryManager registryManager, ChunkErrorHandler errorHandler, HeightLimitView world) {
        super(storageAccess, codec, serializer, deserializer, factory, registryManager, errorHandler, world);
    }

    @Unique
    @Override
    public int libertyvillagers$getFreeTickets(BlockPos pos) {
        return this.get(ChunkSectionPos.toLong(pos)).map(poiSet -> poiSet.getFreeTickets(pos)).orElseThrow(() -> Util.getFatalOrPause(new IllegalStateException("POI never registered at " + String.valueOf(pos))));
    }
}
