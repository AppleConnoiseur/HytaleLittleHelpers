package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyNeedsSensor;
import com.hypixel.hytale.builtin.hytalegenerator.delimiters.RangeDouble;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

/**
 * <p>Multi-use sensor which matches if all of these criteria are true:</p>
 * <ul>
 *     <li><b>foodNeedRange</b> is within its set range.</li>
 *     <li><b>essenceNeedRange</b> is within its set range.</li>
 *     <li><b>happinessNeedRange</b> is within its set range.</li>
 * </ul>
 */
@SuppressWarnings("FieldMayBeFinal")
public class FairyNeedsSensor extends SensorBase {
    private RangeDouble foodNeedRange;
    private RangeDouble essenceNeedRange;
    private RangeDouble happinessNeedRange;

    public  FairyNeedsSensor(@Nonnull BuilderFairyNeedsSensor builder, @Nonnull BuilderSupport builderSupport)
    {
        super(builder);

        double[] foodArray = builder.getFoodNeedRange(builderSupport);
        foodNeedRange = new RangeDouble(foodArray[0], foodArray[1]);

        double[] essenceArray = builder.getEssenceNeedRange(builderSupport);
        essenceNeedRange = new RangeDouble(essenceArray[0], essenceArray[1]);

        double[] happinessArray = builder.getHappinessNeedRange(builderSupport);
        happinessNeedRange = new RangeDouble(happinessArray[0], happinessArray[1]);
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
            return  false;

        //We require a component to work with. Abort if none is found.
        FairyComponent fairyComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent());
        if(fairyComp == null)
            return false;

        //Are any of our needs not satiated?
        return foodNeedRange.contains(fairyComp.getFoodNeed()) &&
                essenceNeedRange.contains(fairyComp.getEssenceNeed()) &&
                happinessNeedRange.contains(fairyComp.getHappinessNeed());
    }

    @Override
    public InfoProvider getSensorInfo() { return null; }
}
