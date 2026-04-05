package chjees.littlehelpers.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyNeedsSensor;
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
    private final double[] foodArray;
    private final double[] essenceArray;
    private final double[] happinessArray;


    public  FairyNeedsSensor(@Nonnull BuilderFairyNeedsSensor builder, @Nonnull BuilderSupport builderSupport)
    {
        super(builder);

        foodArray = builder.getFoodNeedRange(builderSupport);

        essenceArray = builder.getEssenceNeedRange(builderSupport);

        happinessArray = builder.getHappinessNeedRange(builderSupport);
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
        return inRange(foodArray, fairyComp.getFoodNeed()) &&
                inRange(essenceArray, fairyComp.getEssenceNeed()) &&
                inRange(happinessArray, fairyComp.getHappinessNeed());
    }

    /**
     * <p>Simple implementation that checks if the <b>test</b> value is within the <b>range</b>.</p>
     * <p><b>range[0]</b> -&gt; <b>test</b> -&gt; <b>range[1]</b></p>
     * @param range Two length double array. <b>double[2]</b>
     * @param test Value to test with.
     * @return True if <b>test</b> is between <b>range[0]</b> and <b>range[1]</b>.
     */
    private boolean inRange(double[] range, double test)
    {
        return  test >= range[0] && test <= range[1];
    }

    @Override
    public InfoProvider getSensorInfo() { return null; }
}
