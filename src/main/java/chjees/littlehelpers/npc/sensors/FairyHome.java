package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyHome;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import javax.annotation.Nonnull;
import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

@SuppressWarnings("FieldMayBeFinal")
public class FairyHome extends SensorBase {
    private int senseRadius;
    private int homeRadius;
    private int homeRadiusSq;
    protected final PositionProvider positionProvider = new PositionProvider();

    public FairyHome(@Nonnull BuilderFairyHome builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);

        senseRadius = builder.getSenseRadius();
        homeRadius = builder.getHomeRadius();
        homeRadiusSq = homeRadius * homeRadius;
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
            return  false;

        //We require a components to work with. Abort if none is found.
        FairyComponent fairyComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent());
        if(fairyComp == null)
            return false;

        //Exit early if no co-ordinates are set.
        if(!fairyComp.isHomeCoordinatesSet())
        {
            positionProvider.clear();
            return false;
        }

        TransformComponent transformComp = store.getComponent(ref, TransformComponent.getComponentType());
        assert transformComp != null;

        //Check distance
        Vector3d position = transformComp.getPosition();

        //Provide position data if we care about home radius.
        double distance = position.distanceSquaredTo(fairyComp.getHomeCoordinatesAsPoint());
        if(homeRadius > 0 && distance > homeRadiusSq)
        {
            positionProvider.setTarget(fairyComp.getHomeCoordinatesAsPoint());
        }else
        {
            positionProvider.clear();
        }

        return distance <= senseRadius;
    }

    @Override
    public InfoProvider getSensorInfo() {
        if(homeRadius > 0)
            return positionProvider;

        return null;
    }
}