package chjees.littlehelpers.utility;

import chjees.littlehelpers.LittleHelpersPlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import  com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.npc.sensorinfo.IPositionProvider;

/**
 * Utility NPC functions for the Little Helpers mod.
 */
public class NPCUtility {
    /**
     * Checks the {@link ChunkStore} for a harvestable {@link BlockType} for fairies.
     * @param chunkStore Hytale {@link ChunkStore}.
     * @param positionProvider Position provider for the block.
     * @return <i>True</i> if the block at the x, y & z position is harvestable. <i>False</i> if it's not.
     */
    public static boolean checkHarvestableBlock(Store<ChunkStore> chunkStore, IPositionProvider positionProvider)
    {
        return checkHarvestableBlock(chunkStore, (int)positionProvider.getX(), (int)positionProvider.getY(), (int)positionProvider.getZ());
    }

    /**
     * Checks the {@link ChunkStore} for a harvestable {@link BlockType} for fairies.
     * @param chunkStore Hytale {@link ChunkStore}.
     * @param x X position of the block.
     * @param y Y position of the block.
     * @param z Z position of the block.
     * @return <i>True</i> if the block at the x, y & z position is harvestable. <i>False</i> if it's not.
     */
    public static boolean checkHarvestableBlock(Store<ChunkStore> chunkStore, int x, int y, int z)
    {
        //Get the chunk the block is in.
        long chunkIndex = ChunkUtil.indexChunkFromBlock(x, z);
        Ref<ChunkStore> chunkReference = chunkStore.getExternalData().getChunkReference(chunkIndex);
        if(chunkReference == null)
            return false;

        WorldChunk worldChunk = chunkStore.getComponent(chunkReference, WorldChunk.getComponentType());
        assert worldChunk != null;

        //The data we care about.
        int blockRawID = worldChunk.getBlock(x, y, z);

        //Skip Empty blocks.
        if(blockRawID == LittleHelpersPlugin.Instance().getBlockEmptyId())
            return  false;

        return  LittleHelpersPlugin.Instance().getHarvestableBlockIds().contains(blockRawID);
    }
}
