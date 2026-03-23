package chjees.littlehelpers.npc.actions;

import chjees.littlehelpers.npc.actions.builders.BuilderDumpInventory;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * <p>Dumps the inventory of the NPC on the ground.</p>
 */
public class DumpInventory extends ActionBase {
    public DumpInventory(@NonNullDecl BuilderDumpInventory builder, BuilderSupport builderSupport) {
        super(builder);
    }

    // Returns false if this Action is blocking execution.
    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store);
    }

    // Returns false if this Action will block the next Action.
    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.execute(ref, role, sensorInfo, dt, store))
            return false;
        //NPC stuff
        TransformComponent entityTransform = store.getComponent(ref, TransformComponent.getComponentType());
        assert entityTransform != null;
        HeadRotation headRotationComponent = store.getComponent(ref, HeadRotation.getComponentType());
        assert headRotationComponent != null;

        //Get inventories.
        InventoryComponent.Hotbar entityHotbar = store.getComponent(ref, InventoryComponent.Hotbar.getComponentType());
        InventoryComponent.Storage entityStorage = store.getComponent(ref, InventoryComponent.Storage.getComponentType());
        ArrayList<ItemStack> itemsToDump = new ArrayList<>();
        if(entityHotbar != null)
            itemsToDump.addAll(entityHotbar.getInventory().dropAllItemStacks());
        if(entityStorage != null)
            itemsToDump.addAll(entityStorage.getInventory().dropAllItemStacks());

        //Taken from:
        //com.hypixel.hytale.server.npc.systems.NPCDamageSystems: line 253
        if(!itemsToDump.isEmpty())
        {
            Holder<EntityStore>[] drops = ItemComponent.generateItemDrops(
                    store,
                    itemsToDump,
                    entityTransform.getPosition().add(0d , 1d, 0d),
                    headRotationComponent.getRotation());
            store.addEntities(drops, AddReason.SPAWN);
        }

        return true;
    }
}