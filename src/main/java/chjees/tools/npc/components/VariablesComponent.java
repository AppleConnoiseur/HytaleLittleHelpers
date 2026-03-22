package chjees.tools.npc.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import java.util.Map;

/**
 * <p>Storage of variables for mathematical operations.</p>
 */
public class VariablesComponent implements Component<EntityStore> {
    public final static MapCodec<Double, Map<String,Double>> VARIABLES_CODEC = new MapCodec<>(Codec.DOUBLE, Object2ObjectOpenHashMap::new);
    private Map<String, Double> variables = new Object2ObjectOpenHashMap<>();

    public VariablesComponent() { }

    public VariablesComponent(VariablesComponent other) {
        this.variables.putAll(other.getVariables());
    }

    public static final BuilderCodec<VariablesComponent> CODEC =
            BuilderCodec.builder(VariablesComponent.class, VariablesComponent::new)
                    .append(new KeyedCodec<>("Variables", VARIABLES_CODEC),
                            (variablesComponent,
                             stringIntegerMap) -> variablesComponent.variables = stringIntegerMap,
                            variablesComponent -> variablesComponent.variables)
                    .add()
                    .build();

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new VariablesComponent(this);
    }

    public Map<String, Double> getVariables() {
        return variables;
    }
}