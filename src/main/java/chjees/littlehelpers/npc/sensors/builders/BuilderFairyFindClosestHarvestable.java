package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyFindClosestHarvestable;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.Feature;
import com.hypixel.hytale.server.npc.asset.builder.holder.IntHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntSingleValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderFairyFindClosestHarvestable extends BuilderSensorBase {
    private final IntHolder scanRange = new IntHolder();

    public BuilderFairyFindClosestHarvestable() {
    }

    @Override
    public String getShortDescription() {
        return "Short description for FairyFindClosestHarvestable.";
    }

    @Override
    public String getLongDescription() {
        return "Long description for FairyFindClosestHarvestable.";
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