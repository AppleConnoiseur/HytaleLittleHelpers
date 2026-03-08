package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyIsRecruited;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class FairyIsRecruited extends SensorBase {
    public FairyIsRecruited(@Nonnull BuilderFairyIsRecruited builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
            return  false;

        return store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent()) != null;
    }

    @Override
    public InfoProvider getSensorInfo() {
        return null;
    }
}