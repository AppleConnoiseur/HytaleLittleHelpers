package chjees.littlehelpers.npc.actions;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.actions.builders.BuilderFairyDisband;
import chjees.littlehelpers.npc.components.FairyComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

/**
 * <p>Removes the {@link FairyComponent} from the fairy and reverts back to original appearance.</p>
 */
public class FairyDisband extends ActionBase {
    private final String originalAppearance;

    public FairyDisband(@NonNullDecl BuilderFairyDisband builder, BuilderSupport builderSupport) {
        super(builder);
        this.originalAppearance = builder.getOriginalAppearance(builderSupport);
    }

    // Returns false if this Action is blocking execution.
    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store);
    }

    // Returns false if this Action will block the next Action.
    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if (!super.execute(ref, role, sensorInfo, dt, store))
            return false;

        //Ensure it's a fairy with a component.
        var fairyCompType = LittleHelpersPlugin.Instance().getFairyComponent();
        FairyComponent fairyComp = store.getComponent(ref, fairyCompType);
        if(fairyComp == null)
            return  false;

        //Start disbanding
        store.removeComponent(ref, fairyCompType);
        NPCEntity.setAppearance(ref, originalAppearance, store);
        return true;
    }
}