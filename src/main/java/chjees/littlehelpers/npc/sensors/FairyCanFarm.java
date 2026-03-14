package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.npc.sensors.builders.BuilderFairyCanFarm;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class FairyCanFarm extends SensorBase {
    public FairyCanFarm(@Nonnull BuilderFairyCanFarm builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        return super.matches(ref, role, dt, store);
    }

    @Override
    public InfoProvider getSensorInfo() {
        return null;
    }
}