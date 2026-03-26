package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.npc.sensors.builders.BuilderFairyFindClosestHarvestable;
import chjees.littlehelpers.utility.NPCUtility;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;
import org.joml.Vector3i;

/**
 * <p>Similar to the {@link FairyCanFarm} Sensor but this is dedicated to finding nearby harvestable {@link BlockType}'s in a short radius while providing position data.</p>
 * <p>It will provide the same position data and not look for a new harvestable block as long as the one it's locked on is harvestable.</p>
 * <h3>Logic</h3>
 * <ul>
 *     <li>Keep position as long as its harvestable. It's a match.</li>
 *     <li>If the current position is not harvestable, look for a new block.</li>
 *     <li>If no nearby blocks are harvestable this sensor no longer matches.</li>
 * </ul>
 */
public class FairyFindClosestHarvestable extends SensorBase {
    //Builder variables
    private final int scanRange;
    private final PositionProvider positionProvider = new PositionProvider();

    public FairyFindClosestHarvestable(@Nonnull BuilderFairyFindClosestHarvestable builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);

        scanRange = builder.getScanRange().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        //Ensure we will only try to match as long as our parent class matches.
        if (!super.matches(ref, role, dt, store))
        {
            positionProvider.clear();
            return false;
        }

        //Get relevant chunks.
        //Modified from:
        // com/hypixel/hytale/server/core/modules/interaction/interaction/config/client/ExplodeInteraction.java : processTargetBlocks
        // com/hypixel/hytale/server/core/modules/interaction/BlockHarvestUtils.java : performBlockDamage
        World world = store.getExternalData().getWorld();
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();

        //Check if the current block we have set is a valid harvestable block.
        if(positionProvider.hasPosition())
        {
            if(NPCUtility.checkHarvestableBlock(chunkStore, positionProvider))
            {
                return true;
            } else{
                positionProvider.clear();
            }
        }

        //Relevant data to work with.
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        assert transform != null;

        Vector3i originPosition = Vector3dUtil.toVector3i(transform.getPosition());

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
                //Check from bottom up.
                for (int loopY = bottom; loopY >= top && !foundValidBlock; loopY--)
                {
                    foundValidBlock = NPCUtility.checkHarvestableBlock(chunkStore, loopX, loopY, loopZ);
                    if(foundValidBlock)
                    {
                        positionProvider.setTarget(new Vector3d(loopX, loopY, loopZ));
                    }
                }
            }
        }

        if(!foundValidBlock)
        {
            positionProvider.clear();
        }
        return foundValidBlock;
    }

    @Override
    public InfoProvider getSensorInfo() {
        return positionProvider;
    }
}