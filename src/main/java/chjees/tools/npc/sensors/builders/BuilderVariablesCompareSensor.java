package chjees.tools.npc.sensors.builders;

import chjees.tools.npc.sensors.VariablesCompareSensor;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.DoubleHolder;
import com.hypixel.hytale.server.npc.asset.builder.holder.EnumHolder;
import com.hypixel.hytale.server.npc.asset.builder.holder.StringHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.DoubleRangeValidator;
import com.hypixel.hytale.server.npc.asset.builder.validators.StringNotEmptyValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

/**
 * <p>Builder for {@link VariablesCompareSensor}.</p>
 * <p>Performs simple mathematical comparisons with variables from the {@link chjees.tools.npc.components.VariablesComponent}.</p>
 */
public class BuilderVariablesCompareSensor extends BuilderSensorBase {
    private final EnumHolder<VariablesCompareSensor.Operation> operation = new EnumHolder<>();
    private final DoubleHolder value = new DoubleHolder();
    private final StringHolder variableName = new StringHolder();

    public BuilderVariablesCompareSensor() {
    }

    @Override
    public String getShortDescription() {
        return "Compares a named variable with a value.";
    }

    @Override
    public String getLongDescription() {
        return "Compares a named variable with a value. Pick between equals, greater than, greater than or equal, less than, less than or equal.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.requireEnum(
                data,
                "Operation",
                this.getOperation(),
                VariablesCompareSensor.Operation.class,
                BuilderDescriptorState.Stable,
                "Mathematical operation to perform.",
                "Mathematical operation to perform. Compares two values whether they are true.");
        this.requireString(
                data,
                "Variable",
                this.getVariableName(),
                StringNotEmptyValidator.get(),
                BuilderDescriptorState.Stable,
                "Variable name to perform the operation on.",
                null
        );
        this.requireDouble(
                data,
                "Value",
                this.getValue(),
                DoubleRangeValidator.between(Double.MIN_VALUE,Double.MAX_VALUE),
                BuilderDescriptorState.Stable,
                "Value to do the operation with.",
                null
        );
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new VariablesCompareSensor(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public EnumHolder<VariablesCompareSensor.Operation> getOperation() {
        return operation;
    }

    public DoubleHolder getValue() {
        return value;
    }

    public StringHolder getVariableName() {
        return variableName;
    }
}