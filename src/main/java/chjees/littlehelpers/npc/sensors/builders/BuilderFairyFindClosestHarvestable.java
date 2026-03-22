package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyCanFarm;
import chjees.littlehelpers.npc.sensors.FairyFindClosestHarvestable;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.Feature;
import com.hypixel.hytale.server.npc.asset.builder.holder.IntHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntSingleValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

/**
 * <p>Builder class for {@link FairyFindClosestHarvestable}.</p>
 * <p>Similar to the {@link FairyCanFarm} Sensor but this is dedicated to finding nearby harvestable {@link BlockType}'s in a short radius while providing position data.</p>
 * <p>It will provide the same position data and not look for a new harvestable block as long as the one it's locked on is harvestable.</p>
 * <h3>Logic</h3>
 * <ul>
 *     <li>Keep position as long as its harvestable. It's a match.</li>
 *     <li>If the current position is not harvestable, look for a new block.</li>
 *     <li>If no nearby blocks are harvestable this sensor no longer matches.</li>
 * </ul>
 */
public class BuilderFairyFindClosestHarvestable extends BuilderSensorBase {
    private final IntHolder scanRange = new IntHolder();

    public BuilderFairyFindClosestHarvestable() {
    }

    @Override
    public String getShortDescription() {
        return "Finds the closest harvestable block.";
    }

    @Override
    public String getLongDescription() {
        return "Finds the closest harvestable block. Do not make the radius too big since this will be checked often.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.getInt(
                data,
                "Range",
                this.scanRange,
                3,
                IntSingleValidator.greater0(),
                BuilderDescriptorState.Stable,
                "Maximum range of blocks to scan.",
                null);
        this.provideFeature(Feature.Position);
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new FairyFindClosestHarvestable(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public IntHolder getScanRange() {
        return scanRange;
    }
}