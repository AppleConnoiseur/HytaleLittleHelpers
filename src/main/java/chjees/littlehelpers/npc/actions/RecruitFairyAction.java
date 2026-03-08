package chjees.littlehelpers.npc.actions;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.actions.builders.BuilderRecruitFairyAction;
import chjees.littlehelpers.npc.components.FairyComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class RecruitFairyAction extends ActionBase {
    @SuppressWarnings("FieldMayBeFinal")
    private String recruitedAppearance;

    public RecruitFairyAction(@NonNullDecl BuilderRecruitFairyAction builderActionBase, BuilderSupport builderSupport) {
        super(builderActionBase);
        this.recruitedAppearance = builderActionBase.getRecruitedAppearance(builderSupport);
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store) && role.getStateSupport().getInteractionIterationTarget() != null;
    }

    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.execute(ref, role, sensorInfo, dt, store))
            return  false;

        Ref<EntityStore> playerReference = role.getStateSupport().getInteractionIterationTarget();
        if (playerReference == null) {
            return false;
        } else {
            PlayerRef playerRefComponent = store.getComponent(playerReference, PlayerRef.getComponentType());
            if (playerRefComponent == null) {
                return false;
            } else {
                Player playerComponent = store.getComponent(playerReference, Player.getComponentType());
                if (playerComponent == null) {
                    return false;
                } else {
                   //Recruit the fairy.
                    FairyComponent fairyComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent());
                    if(fairyComp == null)
                        fairyComp = new FairyComponent();

                    UUIDComponent playerUUID = store.getComponent(playerReference, UUIDComponent.getComponentType());
                    if(playerUUID != null)
                        fairyComp.setPlayerRecruiter(playerUUID.getUuid());
                    fairyComp.setHappinessNeed(1d);
                    fairyComp.setFoodNeed(1d);
                    fairyComp.setEssenceNeed(0.2d);
                    store.addComponent(ref, LittleHelpersPlugin.Instance().getFairyComponent(), fairyComp);

                    //Deduct one item from players hand.
                    Inventory playerInventory = playerComponent.getInventory();

                    byte activeHotbarSlot = playerInventory.getActiveHotbarSlot();
                    if(activeHotbarSlot > -1)
                    {
                        ItemStack activeHotbarItemstack = playerInventory.getHotbar().getItemStack(activeHotbarSlot);
                        playerInventory.getHotbar().removeItemStackFromSlot(activeHotbarSlot, activeHotbarItemstack, 1);
                    }

                    //Switch appearance if possible.
                    NPCEntity.setAppearance(ref, recruitedAppearance, store);
                    return true;
                }
            }
        }
    }
}
