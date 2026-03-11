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

public class FairyHome extends SensorBase {
    private final int homeRadiusSq;
    private final boolean usePosition;
    private final boolean checkOutside;
    protected final PositionProvider positionProvider = new PositionProvider();

    public FairyHome(@Nonnull BuilderFairyHome builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);
        int homeRadius = builder.getHomeRadius();
        homeRadiusSq = homeRadius * homeRadius;
        usePosition = builder.getUsePosition();
        checkOutside = builder.getOutside();
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
        double distanceToHome = position.distanceSquaredTo(fairyComp.getHomeCoordinatesAsPoint());

        if(checkOutside)
        {
            //Checks if we are outside the home zone.
            if(distanceToHome > homeRadiusSq)
            {
                positionProvider.setTarget(fairyComp.getHomeCoordinatesAsPoint());
                return true;
            }
        }else{
            //Check if we are inside the home radius.
            if(distanceToHome <= homeRadiusSq)
            {
                positionProvider.setTarget(fairyComp.getHomeCoordinatesAsPoint());
                return true;
            }
        }

        positionProvider.clear();
        return false;
    }

    @Override
    public InfoProvider getSensorInfo() {
        if(usePosition)
            return positionProvider;

        return null;
    }
}