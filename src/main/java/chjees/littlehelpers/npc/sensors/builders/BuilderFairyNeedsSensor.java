package chjees.littlehelpers.npc.sensors.builders;

import chjees.littlehelpers.npc.sensors.FairyNeedsSensor;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.NumberArrayHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.DoubleSequenceValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

import javax.annotation.Nonnull;

/**
 * <p>Multi-use sensor for class {@link FairyNeedsSensor} which matches if all of these criteria are true:</p>
 * <ul>
 *     <li><b>foodNeedRange</b> is within its set range.</li>
 *     <li><b>essenceNeedRange</b> is within its set range.</li>
 *     <li><b>happinessNeedRange</b> is within its set range.</li>
 * </ul>
 * <p>In order to detect if a `need` is beneath a threshold only set that range to be lower.</p>
 * <pre><code>
 *     {
 *         "Type":"LHFairyNeedsSatiated",
 *         "FoodNeed": [0.0, 0.5]
 *     }
 * </code></pre>
 */
public class BuilderFairyNeedsSensor extends BuilderSensorBase {
    private final NumberArrayHolder foodNeedRange = new NumberArrayHolder();
    private final NumberArrayHolder essenceNeedRange = new NumberArrayHolder();
    private final NumberArrayHolder happinessNeedRange = new NumberArrayHolder();

    public  BuilderFairyNeedsSensor(){
    }

    @Override
    public String getShortDescription() {
        return "Matches the need(s) of a entity with the FairyComponent.";
    }

    @Override
    public String getLongDescription() {
        return "Matches the need(s) of a entity with the FairyComponent. Can match one or more needs.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.getDoubleRange(
            data,
            "FoodNeed",
                this.foodNeedRange,
            new double[]{0.0d, 1.0d},
            DoubleSequenceValidator.betweenWeaklyMonotonic(0.0d, 1.0d),
            BuilderDescriptorState.Stable,
            "The acceptable range to check on the need.",
            "The acceptable range to check on the need."

        );
        this.getDoubleRange(
                data,
                "EssenceNeed",
                this.essenceNeedRange,
                new double[]{0.0d, 1.0d},
                DoubleSequenceValidator.betweenWeaklyMonotonic(0.0d, 1.0d),
                BuilderDescriptorState.Stable,
                "The acceptable range to check on the need.",
                "The acceptable range to check on the need."

        );
        this.getDoubleRange(
                data,
                "HappinessNeed",
                this.happinessNeedRange,
                new double[]{0.0d, 1.0d},
                DoubleSequenceValidator.betweenWeaklyMonotonic(0.0d, 1.0d),
                BuilderDescriptorState.Stable,
                "The acceptable range to check on the need.",
                "The acceptable range to check on the need."

        );
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new FairyNeedsSensor(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public double[] getFoodNeedRange(@Nonnull BuilderSupport support) {
        return foodNeedRange.get(support.getExecutionContext());
    }

    public double[] getEssenceNeedRange(@Nonnull BuilderSupport support) {
        return essenceNeedRange.get(support.getExecutionContext());
    }

    public double[] getHappinessNeedRange(@Nonnull BuilderSupport support) {
        return happinessNeedRange.get(support.getExecutionContext());
    }
}
