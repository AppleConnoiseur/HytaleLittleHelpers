package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyHome;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntRangeValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderFairyHome extends BuilderSensorBase {
    private int senseRadius;
    private int homeRadius;

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
        this.requireInt(data,
            "SensingRadius",
            this::setSenseRadius,
            IntRangeValidator.between(0, Integer.MAX_VALUE),
            BuilderDescriptorState.Stable,
            "The radius in which the fairy can sense its home.",
            "The radius in which the fairy can sense its home.");
        this.getInt(data,
                "HomeRadius",
                this::setHomeRadius,
                0,
                IntRangeValidator.between(0, Integer.MAX_VALUE),
                BuilderDescriptorState.Stable,
                "The radius in which the fairy will stay at home.",
                "The radius in which the fairy will stay at home. If set above zero it provide target positioning data");
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

    public int getSenseRadius() {
        return senseRadius;
    }

    public void setSenseRadius(int senseRadius) {
        this.senseRadius = senseRadius;
    }

    public int getHomeRadius() {
        return homeRadius;
    }

    public void setHomeRadius(int homeRadius) {
        this.homeRadius = homeRadius;
    }
}