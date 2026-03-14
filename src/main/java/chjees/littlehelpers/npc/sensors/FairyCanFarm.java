package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyCanFarm;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.block.BlockCubeUtil;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import javax.annotation.Nonnull;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class FairyCanFarm extends SensorBase {
    private final int scanRange;
    public FairyCanFarm(@Nonnull BuilderFairyCanFarm builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);

        scanRange = builder.getScanRange().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        super.matches(ref, role, dt, store);

        //Relevant data to work with.
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        assert transform != null;
        Vector3i originPosition = transform.getPosition().toVector3i();

        //Get relevant chunks.
        //Modified from:
        // com/hypixel/hytale/server/core/modules/interaction/interaction/config/client/ExplodeInteraction.java : processTargetBlocks
        // com/hypixel/hytale/server/core/modules/interaction/BlockHarvestUtils.java : performBlockDamage
        World world = store.getExternalData().getWorld();
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();


        //Block type checking. (Invert search result to break out early)
        boolean foundBlock = !BlockCubeUtil.forEachBlock(originPosition.x, originPosition.y, originPosition.z, scanRange, scanRange, scanRange, null, (x, y, z, _) -> {
            //Get the chunk the block is in.
            long chunkIndex = ChunkUtil.indexChunkFromBlock(x, z);
            Ref<ChunkStore> chunkReference = chunkStore.getExternalData().getChunkReference(chunkIndex);
            assert chunkReference != null;
            WorldChunk worldChunk = chunkStore.getComponent(chunkReference, WorldChunk.getComponentType());
            assert worldChunk != null;

            //The data we care about.
            BlockType blockType = worldChunk.getBlockType(x, y, z);

            //Break out early if successful.
            return blockType != null && LittleHelpersPlugin.Instance().getFarmableBlocks().contains(blockType.getId());
        });

        return foundBlock;
    }

    @Override
    public InfoProvider getSensorInfo() {
        return null;
    }
}