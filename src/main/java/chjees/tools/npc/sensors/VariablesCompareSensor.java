package chjees.tools.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.tools.npc.components.VariablesComponent;
import chjees.tools.npc.sensors.builders.BuilderVariablesCompareSensor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 *  Performs simple mathematical comparisons with variables from the {@link VariablesComponent}.
 */
public class VariablesCompareSensor extends SensorBase {
    /// Operation to perform on the variable.
    private final Operation operation;
    /// Variable name to do the operation with.
    private final String variableName;
    /// Variable value to do the operation with.
    private final double variableValue;

    public VariablesCompareSensor(@Nonnull BuilderVariablesCompareSensor builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);
        operation = builder.getOperation().get(builderSupport.getExecutionContext());
        variableName = builder.getVariableName().get(builderSupport.getExecutionContext());
        variableValue = builder.getValue().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
            return  false;

        VariablesComponent varComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getVariablesComponent());
        //Fail if we have no component to work with.
        if(varComp == null)
        {
            HytaleLogger.getLogger().at(Level.WARNING).atMostEvery(1, TimeUnit.MINUTES).log("VariablesCompareSensor is trying to do a operation with a `null` VariablesComponent: %s", getBreadCrumbs());
            return false;
        }

        if(!varComp.getVariables().containsKey(this.variableName))
            return false;

        switch (this.operation)
        {
            case Equals -> {
                return varComp.getVariables().get(this.variableName) == this.variableValue;
            }
            case Greater -> {
                return varComp.getVariables().get(this.variableName) > this.variableValue;
            }
            case GreaterOrEquals -> {
                return varComp.getVariables().get(this.variableName) >= this.variableValue;
            }
            case Lesser -> {
                return varComp.getVariables().get(this.variableName) < this.variableValue;
            }
            case LesserOrEquals -> {
                return varComp.getVariables().get(this.variableName) <= this.variableValue;
            }
            case IsSet -> {
                return varComp.getVariables().containsKey(this.variableName);
            }
            default -> throw new IllegalStateException("VariablesCompare: Unexpected operation: " + this.operation);
        }
    }

    @Override
    public InfoProvider getSensorInfo() {
        return null;
    }

    public enum Operation implements Supplier<String> {
        Equals("Checks whether the variable is equal to the value: x == y"),
        Greater("Checks whether the variable is greater than the value: x > y"),
        GreaterOrEquals("Checks whether the variable is greater than or equals the value: x >= y"),
        Lesser("Checks whether the variable is lesser than the value: x < y"),
        LesserOrEquals("Checks whether the variable is lesser than or equals the value: x <= y"),
        IsSet("Checks whether the variable is set at all: Matches if x exists.");

        private final String description;

        Operation(String description) {
            this.description = description;
        }

        @Override
        public String get() {
            return this.description;
        }
    }
}