package chjees.littlehelpers.npc.filters;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.filters.builders.BuilderFairyRecruiter;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.EntityFilterBase;
import com.hypixel.hytale.server.npc.role.Role;

import javax.annotation.Nonnull;

/**
 * Filter is true if the entity being filtered is the one which recruited the fairy.
 */
public class FairyRecruiter extends EntityFilterBase {
    public static final int COST = 300;

    public FairyRecruiter(@Nonnull BuilderFairyRecruiter builder, @Nonnull BuilderSupport builderSupport) {

    }

    @Override
    public boolean matchesEntity(@Nonnull Ref<EntityStore> ref, @Nonnull Ref<EntityStore> targetRef, @Nonnull Role role, @Nonnull Store<EntityStore> store) {
        //We require a component to work with. Abort if none is found.
        FairyComponent fairyComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent());
        if (fairyComp == null)
            return false;

        UUIDComponent otherUUID = store.getComponent(targetRef, UUIDComponent.getComponentType());
        assert otherUUID != null;

        return fairyComp.getPlayerRecruiter().equals(otherUUID.getUuid());
    }

    @Override
    public int cost() {
        return COST;
    }
}