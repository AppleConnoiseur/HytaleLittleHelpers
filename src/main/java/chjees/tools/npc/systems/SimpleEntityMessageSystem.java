package chjees.tools.npc.systems;

import chjees.tools.npc.components.SimpleEntityMessageComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SimpleEntityMessageSystem extends RefSystem<EntityStore> {
    @SuppressWarnings("FieldMayBeFinal")
    private final ComponentType<EntityStore, SimpleEntityMessageComponent> simpleMessageComponentType;

    public SimpleEntityMessageSystem(ComponentType<EntityStore, SimpleEntityMessageComponent> componentType)
    {
        this.simpleMessageComponentType = componentType;
    }

    @Override
    public void onEntityAdded(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl AddReason addReason, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        @SuppressWarnings("DataFlowIssue") NPCEntity npcComp = store.getComponent(ref, NPCEntity.getComponentType());
        if(addReason == AddReason.SPAWN && npcComp != null)
        {
            commandBuffer.addComponent(ref, this.simpleMessageComponentType);
        }
    }

    @Override
    public void onEntityRemove(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }
}
