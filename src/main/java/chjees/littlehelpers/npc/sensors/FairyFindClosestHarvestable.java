package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.npc.sensors.builders.BuilderFairyFindClosestHarvestable;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.IntHolder;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

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
            return false;

        /* TODO: Add own matching logic here. */
        return true;
    }

    @Override
    public InfoProvider getSensorInfo() {
        return positionProvider;
    }
}