package chjees.tools.npc.actions.builders;

import chjees.tools.npc.actions.VariablesOperation;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.DoubleHolder;
import com.hypixel.hytale.server.npc.asset.builder.holder.EnumHolder;
import com.hypixel.hytale.server.npc.asset.builder.holder.StringHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.StringNotEmptyValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

/**
 * <p>Builder for {@link VariablesOperation}.</p>
 * <p>Performs mathematical operations on the {@link chjees.tools.npc.components.VariablesComponent}.</p>
 */
public class BuilderVariablesOperation extends BuilderActionBase {
    private final EnumHolder<VariablesOperation.Operation> operation = new EnumHolder<>();
    private final DoubleHolder value = new DoubleHolder();
    private final StringHolder variableName = new StringHolder();

    @NullableDecl
    @Override
    public String getShortDescription() {
        return "Performs a mathematical operation on a named variable.";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "Performs a mathematical operation on a named variable. Pick between common mathematical operations such as setting, addition, subtraction, multiplication and division.";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new VariablesOperation(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderVariablesOperation readConfig(JsonElement data) {
        this.requireEnum(
                data,
                "Operation",
                this.operation,
                VariablesOperation.Operation.class,
                BuilderDescriptorState.Stable,
                "Mathematical operation to perform.",
                "Mathematical operation to perform. Manipulates a existing variable if it exists. If it doesn't exist operations other than `Set` defaults the value to `0.0`.");
        this.requireString(
                data,
                "Variable",
                this.variableName,
                StringNotEmptyValidator.get(),
                BuilderDescriptorState.Stable,
                "Variable name to perform the operation on.",
                null
        );
        this.requireDouble(
                data,
                "Value",
                this.value,
                null,
                BuilderDescriptorState.Stable,
                "Value to do the operation with.",
                null
        );
        return this;
    }

    public EnumHolder<VariablesOperation.Operation> getOperation() {
        return operation;
    }

    public DoubleHolder getValue() {
        return value;
    }

    public StringHolder getVariableName() {
        return variableName;
    }
}