package chjees.tools.npc.sensors.builders;

import chjees.tools.npc.sensors.SimpleEntityMessageSensor;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.Feature;import com.hypixel.hytale.server.npc.asset.builder.holder.BooleanHolder;
import com.hypixel.hytale.server.npc.asset.builder.holder.StringHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.StringNotEmptyValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

public class BuilderSimpleEntityMessageSensor extends BuilderSensorBase {
    private final StringHolder messageKey = new StringHolder();
    private final BooleanHolder useEntity = new BooleanHolder();
    private final BooleanHolder clearMessage = new BooleanHolder();

    public BuilderSimpleEntityMessageSensor() {
    }

    @Override
    public String getShortDescription() {
        return "Reacts to when a message is delivered and then removes it.";
    }

    @Override
    public String getLongDescription() {
        return "Reacts to when a message is delivered and then removes it. Optionally can react to the entity that sent the message.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.requireString(
                data,
                "Message",
                this.messageKey,
                StringNotEmptyValidator.get(),
                BuilderDescriptorState.Stable,
                "Message to react to.",
                null
        );
        boolean tempUseEntity = this.getBoolean(
                data,
                "UseEntity",
                this.useEntity,
                false,
                BuilderDescriptorState.Stable,
                "If set to true entity information will be sent for use in other sensors and actions.",
                null
        );
        this.getBoolean(
                data,
                "Clear",
                this.clearMessage,
                true,
                BuilderDescriptorState.Stable,
                "If set to true it will automatically clear the message on match.",
                null
        );
        if(tempUseEntity)
        {
            this.provideFeature(Feature.LiveEntity);
        }

        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new SimpleEntityMessageSensor(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public StringHolder getMessageKey() {
        return this.messageKey;
    }

    public BooleanHolder getUseEntity() {
        return useEntity;
    }

    public BooleanHolder getClearMessage() {
        return clearMessage;
    }
}