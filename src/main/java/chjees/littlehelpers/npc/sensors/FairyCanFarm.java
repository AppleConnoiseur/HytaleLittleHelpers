package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.npc.sensors.builders.BuilderFairyCanFarm;
import com.hypixel.hytale.common.util.FormatUtil;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
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
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FairyCanFarm extends SensorBase {
    private final int scanRange;
    private static final Pattern BlockIdPattern = Pattern.compile("\\*(.+)_State_Definitions_StageFinal");
    //private static final Pattern BlockIdPattern = Pattern.compile("\\*(.+)_State_Definitions_Stage_StageFinal");
    //private static final Pattern BlockIdPattern = Pattern.compile("\\*(.+)_State_Definitions_StageFinal");
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

        long start = System.nanoTime();
        int iterations = 0;

        boolean foundValidBlock = false; //In case we find a valid block, break out of all loops.

        //Boring old three deep nested loop.
        int halfRadius = scanRange / 2;
        int west = originPosition.x - halfRadius;
        int east = originPosition.x + halfRadius;
        int north = originPosition.z - halfRadius;
        int south = originPosition.z + halfRadius;
        int top = originPosition.y - halfRadius;
        int bottom = originPosition.y + halfRadius;
        if(bottom < 0)
            bottom = 0;


        for (int loopX = west; loopX <= east && !foundValidBlock; loopX++)
        {
            for (int loopZ = north; loopZ <= south && !foundValidBlock; loopZ++)
            {
                for (int loopY = top; loopY <= bottom && !foundValidBlock; loopY++)
                {
                    iterations++;
                    foundValidBlock = checkBlock(chunkStore, loopX, loopY, loopZ);
                }
            }
        }

        HytaleLogger.forEnclosingClass().at(Level.INFO).log("foundValidBlock: %s; iterations: %s; Scanning blocks with custom algorithm took: %s", Boolean.toString(foundValidBlock), Integer.toString(iterations), FormatUtil.nanosToString(System.nanoTime() - start));
        return foundValidBlock;
    }

    private boolean checkBlock(Store<ChunkStore> chunkStore, int x, int y, int z)
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

        BlockType blockType = BlockType.getAssetMap().getAsset(blockRawID);
        if(blockType != null)
        {
            //Break out early for types like Empty.
            String blockId = blockType.getId();
            if(blockId.equals("Empty"))
                return false;

            Matcher blockIdMatcher = BlockIdPattern.matcher(blockId);
            boolean matches = blockIdMatcher.matches();
            if(matches)
            {
                HytaleLogger.forEnclosingClass().at(Level.INFO).log("Found a match: %s", blockType.getId());
            }
            return matches;
        }
        return  false;
    }

    @Override
    public InfoProvider getSensorInfo() {
        return null;
    }
}