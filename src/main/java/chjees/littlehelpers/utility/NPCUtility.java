package chjees.littlehelpers.utility;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.role.Role;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class NPCUtility {
    public static Player GetPlayerFromRole(@NonNullDecl Role role, @NonNullDecl Store<EntityStore> store) {
        Ref<EntityStore> playerReference = role.getStateSupport().getInteractionIterationTarget();
        if (playerReference == null) {
            return null;
        } else {
            PlayerRef playerRefComponent = store.getComponent(playerReference, PlayerRef.getComponentType());
            if (playerRefComponent == null) {
                return null;
            } else {
                return store.getComponent(playerReference, Player.getComponentType());
            }
        }
    }
}
