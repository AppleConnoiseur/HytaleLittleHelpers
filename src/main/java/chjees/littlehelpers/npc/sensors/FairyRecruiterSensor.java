package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyRecruiterSensor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

@SuppressWarnings("FieldMayBeFinal")
public class FairyRecruiterSensor extends SensorBase {
    public FairyRecruiterSensor(@Nonnull BuilderFairyRecruiterSensor builder, @Nonnull BuilderSupport builderSupport)
    {
        super(builder);
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
            return  false;

        //We require a component to work with. Abort if none is found.
        FairyComponent fairyComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent());
        if(fairyComp == null)
            return false;

        Ref<EntityStore> playerReference = role.getStateSupport().getInteractionIterationTarget();
        if (playerReference == null) {
            return false;
        } else {
            PlayerRef playerRefComponent = store.getComponent(playerReference, PlayerRef.getComponentType());
            if (playerRefComponent == null) {
                return false;
            } else {
                UUIDComponent playerUUID = store.getComponent(playerReference, UUIDComponent.getComponentType());

                return playerUUID != null && fairyComp.getPlayerRecruiter() == playerUUID.getUuid();
            }
        }
    }

    @Override
    public InfoProvider getSensorInfo() { return null; }
}
