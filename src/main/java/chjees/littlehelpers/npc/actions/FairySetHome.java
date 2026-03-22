package chjees.littlehelpers.npc.actions;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.actions.builders.BuilderFairySetHome;
import chjees.littlehelpers.npc.components.FairyComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

/**
 * <p>Sets the new home of the fairy.</p>
 */
public class FairySetHome extends ActionBase {
    private final boolean unsetHome;

    public FairySetHome(@NonNullDecl BuilderFairySetHome builderActionBase, BuilderSupport builderSupport) {
        super(builderActionBase);
        unsetHome = builderActionBase.isUnsetHome();
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store);
    }

    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.execute(ref, role, sensorInfo, dt, store))
            return  false;

        //Set the new home of the fairy.
        FairyComponent fairyComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent());
        assert fairyComp != null;

        @SuppressWarnings("DataFlowIssue") NPCEntity npcComponent = store.getComponent(ref, NPCEntity.getComponentType());
        assert npcComponent != null;

        if(unsetHome)
        {
            fairyComp.setHomeCoordinatesSet(false);
            fairyComp.setHomeCoordinates(0, 0,0);
            return true;
        }

        TransformComponent transformComp = store.getComponent(ref, TransformComponent.getComponentType());
        assert transformComp != null;

        Vector3d point = transformComp.getPosition();
        fairyComp.setHomeCoordinates((int)point.x, (int)point.y, (int)point.z);
        fairyComp.setHomeCoordinatesSet(true);
        npcComponent.setLeashPoint(point);
        return true;
    }
}