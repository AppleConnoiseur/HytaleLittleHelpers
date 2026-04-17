package chjees.tools.npc.sensors;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.role.Role;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3i;

public interface MatchableBlockInventory {
    boolean matchInventory(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, @NonNullDecl Store<EntityStore> store,
                           Vector3i blockPosition, ItemContainerBlock blockContainer, Ref<ChunkStore> chunkReference);
}
