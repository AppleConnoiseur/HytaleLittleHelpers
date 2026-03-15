package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyCanFarm;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
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

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FairyCanFarm extends SensorBase {
    private final int scanRange;
    private static Pattern BlockIdPattern = Pattern.compile("\\*(.+)_State_Definitions_Stage_StageFinal");
    public FairyCanFarm(@Nonnull BuilderFairyCanFarm builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);

        scanRange = builder.getScanRange().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
            return  false;
        //Debug timing
        //long start = System.nanoTime();

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
            if(chunkReference == null)
                return  true;

            WorldChunk worldChunk = chunkStore.getComponent(chunkReference, WorldChunk.getComponentType());
            assert worldChunk != null;

            //The data we care about.
            int blockRawID = worldChunk.getBlock(x, y, z);
            BlockType blockType = worldChunk.getBlockType(x, y, z);
            if(blockType != null) //Break out early if we found a viable block. (Invert the result)
            {
                //Break out early for types like Empty.
                String blockId = blockType.getId();
                if(blockId.equals("Empty"))
                    return true;

                //Criteria
                //1. If a block id is prefixed with a star it's a block state.
                //2. Look for a block state definition where it's at the finished stage.
                // TODO: Look for a better way that does require tearing out my hair.
                Matcher blockIdMatcher = BlockIdPattern.matcher(blockId);
                boolean foundMatch = blockIdMatcher.matches();
                HytaleLogger.forEnclosingClass().at(Level.INFO).atMostEvery(10, TimeUnit.SECONDS).log("Attempting match (%s): %s ## %s", Boolean.toString(foundMatch), Integer.toString(blockRawID), blockId);
                if(foundMatch)
                {
                    HytaleLogger.forEnclosingClass().at(Level.INFO).atMostEvery(3, TimeUnit.SECONDS).log("Match found: %s", blockIdMatcher.group(1));
                }
                /*if(foundMatch && LittleHelpersPlugin.Instance().getFarmableBlocks().stream().anyMatch(s -> s.equals(blockIdMatcher.group(1))))
                {
                    //if(blockIdMatcher.group(0).equals(blockId))
                        return false;
                }*/

                //We got a normal block. Skip.
                return true;
            }

            //Continue if we did not find the appropriate block.
            return true;
        });

        //Debug timing
        //HytaleLogger.forEnclosingClass().at(Level.INFO).log("Scanning for farmable blocks! Took: %s", FormatUtil.nanosToString(System.nanoTime() - start));

        return foundBlock;
    }

    @Override
    public InfoProvider getSensorInfo() {
        return null;
    }
}