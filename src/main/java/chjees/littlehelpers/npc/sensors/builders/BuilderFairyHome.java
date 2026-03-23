package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyHome;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.Feature;
import com.hypixel.hytale.server.npc.asset.builder.holder.IntHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntRangeValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

/**
 * <p>Builder for {@link FairyHome} that checks if the fairy is in the home area.</p>
 * <p><b>Default</b> behavior checks if the fairy is inside the home zone with the cubic radius of 30 units.</p>
 * <p>If <b>Outside</b> is set to true it inverts the behavior and instead checks if the fairy is outside the home zone.</p>
 * <p>If <b>UsePosition</b> is set to true then this {@link Sensor} can be used to provide the home center position.</p>
 */
public class BuilderFairyHome extends BuilderSensorBase {
    private final IntHolder homeRadius = new IntHolder();
    private boolean usePosition;
    private boolean outside;

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
                this.homeRadius,
                30,
                IntRangeValidator.between(0, Integer.MAX_VALUE),
                BuilderDescriptorState.Stable,
                "The radius in which the fairy will stay at home.",
                "The radius in which the fairy will stay at home. If set above zero it provide target positioning data.");
        this.getBoolean(data,
                "Outside",
                this::setOutside,
                false,
                BuilderDescriptorState.Stable,
                "Whether to check if the fairy is outside the home zone.",
                "Whether to check if the fairy is outside the home zone.");
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

    public boolean getUsePosition() {
        return usePosition;
    }

    private void setUsePosition(boolean usePosition) {
        this.usePosition = usePosition;
    }

    public boolean getOutside() {
        return outside;
    }

    private void setOutside(boolean outside) {
        this.outside = outside;
    }

    public IntHolder getHomeRadius() {
        return homeRadius;
    }
}