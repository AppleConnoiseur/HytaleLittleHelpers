package chjees.tools.npc.actions;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.tools.npc.actions.builders.BuilderVariablesOperation;
import chjees.tools.npc.components.VariablesComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * <p>Performs mathematical operations on the {@link VariablesComponent}.</p>
 */
public class VariablesOperation extends ActionBase {
    /// Operation to perform on the variable.
    private final Operation operation;
    /// Variable name to do the operation with.
    private final String variableName;
    /// Variable value to do the operation with.
    private final double variableValue;

    public VariablesOperation(@NonNullDecl BuilderVariablesOperation builderVariablesOperation, BuilderSupport builderSupport) {
        super(builderVariablesOperation);
        operation = builderVariablesOperation.getOperation().get(builderSupport.getExecutionContext());
        variableName = builderVariablesOperation.getVariableName().get(builderSupport.getExecutionContext());
        variableValue = builderVariablesOperation.getValue().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store) && this.operation != null;
    }

    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.execute(ref, role, sensorInfo, dt, store))
            return  false;

        VariablesComponent varComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getVariablesComponent());
        //Fail if we have no component to work with.
        if(varComp == null)
        {
            HytaleLogger.getLogger().at(Level.WARNING).atMostEvery(1, TimeUnit.MINUTES).log("VariablesOperation is trying to do a operation with a `null` VariablesComponent: %s", getBreadCrumbs());
            return false;
        }

        //Temp fix
        //varComp.fix();

        switch (this.operation)
        {
            case Set -> varComp.getVariables().put(this.variableName, variableValue);
            case Addition -> varComp.getVariables().put(this.variableName, varComp.getVariables().get(this.variableName) + variableValue);
            case Subtract -> varComp.getVariables().put(this.variableName, varComp.getVariables().get(this.variableName) - variableValue);
            case Multiply -> varComp.getVariables().put(this.variableName, varComp.getVariables().get(this.variableName) * variableValue);
            case Division -> varComp.getVariables().put(this.variableName, varComp.getVariables().get(this.variableName) / variableValue);
        }

        return true;
    }

    public enum Operation implements Supplier<String> {
        Set("Sets the value directly operation: x = y"),
        Addition("Performs a addition operation: x + y"),
        Subtract("Performs a subtraction operation: x - y"),
        Multiply("Performs a multiplication operation: x * y"),
        Division("Performs a division operation: x / y");

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