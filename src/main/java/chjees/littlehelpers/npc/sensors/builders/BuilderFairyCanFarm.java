package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyCanFarm;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.IntHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntSingleValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderFairyCanFarm extends BuilderSensorBase {
    private final IntHolder scanRange = new IntHolder();

    public BuilderFairyCanFarm() {
    }

    @Override
    public String getShortDescription() {
        return "Determines whether the fairy can farm.";
    }

    @Override
    public String getLongDescription() {
        return "Determines whether the fairy can farm. Combine with a timer or alarm to not check too often.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.getInt(
                data,
                "Range",
                this.scanRange,
                10,
                IntSingleValidator.greater0(),
                BuilderDescriptorState.Stable,
                "Maximum range of blocks to scan.",
                null);
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new FairyCanFarm(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public IntHolder getScanRange() {
        return scanRange;
    }

    /* TODO: Add getters for private data variables for FairyCanFarm to access.*/
}