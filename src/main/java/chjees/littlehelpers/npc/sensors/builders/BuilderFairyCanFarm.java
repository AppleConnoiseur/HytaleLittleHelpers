package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyCanFarm;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.NumberArrayHolder;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderFairyCanFarm extends BuilderSensorBase {
    private final NumberArrayHolder scanRange = new NumberArrayHolder();

    public BuilderFairyCanFarm() {
    }

    @Override
    public String getShortDescription() {
        return "Determines whether the fairy can farm.";
    }

    @Override
    public String getLongDescription() {
        return "Long description for FairyCanFarm.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
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

    public NumberArrayHolder getScanRange() {
        return scanRange;
    }

    /* TODO: Add getters for private data variables for FairyCanFarm to access.*/
}