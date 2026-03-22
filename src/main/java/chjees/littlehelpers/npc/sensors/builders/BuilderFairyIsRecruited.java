package chjees.littlehelpers.npc.sensors.builders;


import chjees.littlehelpers.npc.sensors.FairyIsRecruited;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

/**
 * Builder for {@link FairyIsRecruited} that checks if the fairy is recruited by someone.
 */
public class BuilderFairyIsRecruited extends BuilderSensorBase {

    public BuilderFairyIsRecruited() {
    }

    @Override
    public String getShortDescription() {
        return "Matches if the fairy is recruited. (Have a FairyComponent)";
    }

    @Override
    public String getLongDescription() {
        return "Matches if the fairy is recruited. (Have a FairyComponent)";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new FairyIsRecruited(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }
}