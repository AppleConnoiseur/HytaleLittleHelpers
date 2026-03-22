package chjees.littlehelpers.entity.systems;

import chjees.littlehelpers.npc.components.FairyComponent;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.thread.TickingThread;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

/**
 *  <p>Ticks all entities with the {@link FairyComponent}'s.</p>
 *  <p>It takes about 60 minutes to fully drain <b>Food</b> and <b>Essence</b> needs.</p>
 *  <p>It takes around 30 to 60 minutes to drain the happiness need. Each lacking <b>Food</b> and <b>Essence</b> need additively add to the drain by 100%.</p>
 */
public class FairyNeedsSystem extends EntityTickingSystem<EntityStore> {
    private final ComponentType<EntityStore, FairyComponent> fairyComponentType;

    public FairyNeedsSystem(ComponentType<EntityStore, FairyComponent> componentType)
    {
        this.fairyComponentType = componentType;
    }

    @Override
    public void tick(
            float dt,
            int index,
            @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        FairyComponent component = archetypeChunk.getComponent(index, fairyComponentType);
        assert component != null;

        //=== Calculation Breakup ===
        //60 minutes to fully drain needs.
        //1 minute is 60 seconds
        //A world ticks at 30 times per second.
        //Multiply delta time that is affected by time dilation.
        final double minutes = 60d;
        final double secondsPerMinutes = 60d;
        final double needTick =  1d / (minutes * secondsPerMinutes * (double)TickingThread.TPS) * dt;

        //Tick down all needs.
        component.setFoodNeed(component.getFoodNeed() - needTick);
        component.setEssenceNeed(component.getEssenceNeed() - needTick);

        //Only tick down happiness if either of the needs are 0. Multiplied per missing need.
        double happinessDrainModifier = 0d;

        if(component.getFoodNeed() <= 0d)
            happinessDrainModifier += 1d;

        if(component.getEssenceNeed() <= 0d)
            happinessDrainModifier += 1d;

        if(happinessDrainModifier > 0d)
            component.setHappinessNeed(component.getHappinessNeed() - (happinessDrainModifier * needTick));
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return this.fairyComponentType;
    }

    @Override
    public boolean isParallel(int archetypeChunkSize, int taskCount) {
        return false;
    }

    @SuppressWarnings("unused")
    public ComponentType<EntityStore, FairyComponent> getFairyComponentType() {
        return fairyComponentType;
    }
}
