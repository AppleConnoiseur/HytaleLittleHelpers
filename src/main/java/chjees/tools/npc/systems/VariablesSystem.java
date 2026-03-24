package chjees.tools.npc.systems;

import chjees.tools.npc.components.VariablesComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

/**
 * Ensures all NPCs have the {@link VariablesComponent} component upon spawning.
 */
public class VariablesSystem extends RefSystem<EntityStore> {
    private final ComponentType<EntityStore, VariablesComponent> variablesComponentType;

    public VariablesSystem(ComponentType<EntityStore, VariablesComponent> componentType) {
        this.variablesComponentType = componentType;
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }

    @Override
    public void onEntityAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl AddReason addReason, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        @SuppressWarnings("DataFlowIssue") NPCEntity npcComp = store.getComponent(ref, NPCEntity.getComponentType());
        if(addReason == AddReason.SPAWN && npcComp != null)
        {
            commandBuffer.addComponent(ref, this.variablesComponentType);
        }
    }

    @Override
    public void onEntityRemove(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
    }
}
