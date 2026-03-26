package chjees.littlehelpers.npc.actions;

import chjees.littlehelpers.npc.actions.builders.BuilderFairyHarvest;
import chjees.littlehelpers.utility.NPCUtility;
import com.hypixel.hytale.builtin.adventure.farming.FarmingUtil;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.IPositionProvider;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.Objects;
import org.joml.Vector3i;

/**
 * <p>Harvests the block provided by the {@link PositionProvider}.</p>
 * <p><b>Note:</b> This is an instant action.</p>
 */
public class FairyHarvest extends ActionBase {

    public FairyHarvest(@NonNullDecl BuilderFairyHarvest builderActionBase, BuilderSupport builderSupport) {
        super(builderActionBase);
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        if(!super.canExecute(ref, role, sensorInfo, dt, store))
            return false;
        if(sensorInfo == null)
            return false;
        if(!sensorInfo.hasPosition())
            return false;

        //Ensure we have a valid block targeted.
        World world = store.getExternalData().getWorld();
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();

        return NPCUtility.checkHarvestableBlock(chunkStore, Objects.requireNonNull(sensorInfo.getPositionProvider()));
    }

    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.canExecute(ref, role, sensorInfo, dt, store))
            return false;
        if(sensorInfo == null)
            return false;
        if(!sensorInfo.hasPosition())
            return false;

        //Ensure we have a valid block targeted.
        World world = store.getExternalData().getWorld();
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();

        IPositionProvider position = sensorInfo.getPositionProvider();
        assert position != null;
        Vector3i blockPosition = new Vector3i((int) position.getX(), (int) position.getY(), (int) position.getZ());

        if(!NPCUtility.checkHarvestableBlock(chunkStore, Objects.requireNonNull(sensorInfo.getPositionProvider())))
            return false;

        //Harvest the block.
        //From: com.hypixel.hytale.builtin.adventure.farming.interactions.HarvestCropInteraction
        //Get the chunk the block is in.
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPosition.x, blockPosition.z);
        Ref<ChunkStore> chunkReference = chunkStore.getExternalData().getChunkReference(chunkIndex);
        if(chunkReference == null)
            return false;

        BlockChunk blockChunkComponent = chunkStore.getComponent(chunkReference, BlockChunk.getComponentType());
        if(blockChunkComponent == null)
            return false;

        @SuppressWarnings("deprecation") BlockSection blockSection = blockChunkComponent.getSectionAtBlockY(blockPosition.y);
        WorldChunk worldChunk = chunkStore.getComponent(chunkReference, WorldChunk.getComponentType());
        if(worldChunk == null)
            return false;

        //The data we care about.
        BlockType blockType = worldChunk.getBlockType(blockPosition);
        if(blockType == null)
            return false;

        int rotationIndex = blockSection.getRotationIndex(blockPosition.x, blockPosition.y, blockPosition.z);

        return FarmingUtil.harvest(world, store, ref, blockType, rotationIndex, blockPosition);
    }
}