package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyRecruiterSensor;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderFairyRecruiterSensor extends BuilderSensorBase {
    public  BuilderFairyRecruiterSensor(){
    }

    @Override
    public String getShortDescription() {
        return "Matches if the player UUID is the same as the recruiter in the FairyComponent.";
    }

    @Override
    public String getLongDescription() {
        return "Matches if the player UUID is the same as the recruiter in the FairyComponent.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new FairyRecruiterSensor(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }
}