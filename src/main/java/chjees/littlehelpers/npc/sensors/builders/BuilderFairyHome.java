package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyHome;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.Feature;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntRangeValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderFairyHome extends BuilderSensorBase {
    private int homeRadius;
    private boolean usePosition;

    public BuilderFairyHome() {
    }

    @Override
    public String getShortDescription() {
        return "Senses if the fairy is close to home.";
    }

    @Override
    public String getLongDescription() {
        return "Senses if the fairy is close to home.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.getInt(data,
                "HomeRadius",
                this::setHomeRadius,
                30,
                IntRangeValidator.between(0, Integer.MAX_VALUE),
                BuilderDescriptorState.Stable,
                "The radius in which the fairy will stay at home.",
                "The radius in which the fairy will stay at home. If set above zero it provide target positioning data.");
        boolean tempUsePosition = this.getBoolean(data,
                "UsePosition",
                this::setUsePosition,
                false,
                BuilderDescriptorState.Stable,
                "Whether to provide targeting feature to the instruction.",
                "Whether to provide targeting feature to the instruction. Useful for making the fairy go home.");
        if(tempUsePosition)
            this.provideFeature(Feature.Position);

        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new FairyHome(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public int getHomeRadius() {
        return homeRadius;
    }

    public void setHomeRadius(int homeRadius) {
        this.homeRadius = homeRadius;
    }

    public boolean getUsePosition() {
        return usePosition;
    }

    private void setUsePosition(boolean usePosition) {
        this.usePosition = usePosition;
    }
}