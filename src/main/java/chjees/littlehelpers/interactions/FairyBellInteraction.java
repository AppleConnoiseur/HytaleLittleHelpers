package chjees.littlehelpers.interactions;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.tools.npc.components.SimpleEntityMessageComponent;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.selector.Selector;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.logging.Level;

/**
 * <p>This <b>Interaction</b> broadcasts a message to all nearby fairies.</p>
 * <p>Requires a {@link SimpleEntityMessageComponent} on the entity for it to work.</p>
 */
public class FairyBellInteraction extends SimpleInstantInteraction {
    @Nonnull
    public static final BuilderCodec<FairyBellInteraction> CODEC = BuilderCodec.builder(
                    FairyBellInteraction.class, FairyBellInteraction::new, SimpleInstantInteraction.CODEC
            )
            .documentation("Interacts with fairies over a spherical range.")
            .appendInherited(
                    new KeyedCodec<>("Context", Codec.STRING),
                    (interaction, s) -> interaction.context = s,
                    interaction -> interaction.context,
                    (interaction, parent) -> interaction.context = parent.context
            )
            .documentation("The provided context for the bell action.")
            .addValidator(Validators.nonNull())
            .add()
            .appendInherited(
                    new KeyedCodec<>("Range", Codec.DOUBLE),
                    (interaction, s) -> interaction.range = s,
                    interaction -> interaction.range,
                    (interaction, parent) -> interaction.range = parent.range
            )
            .documentation("The range to search for fairies..")
            .addValidator(Validators.nonNull())
            .add()
            .build();
    protected String context;
    protected double range = 1d;

    @SuppressWarnings("unused")
    public FairyBellInteraction(String id) {
        super(id);
    }

    protected FairyBellInteraction() {
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected final void firstRun(
            @Nonnull InteractionType type,
          @Nonnull InteractionContext context,
          @Nonnull CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        Ref<EntityStore> playerRef = context.getEntity();
        Player playerComponent = commandBuffer.getComponent(playerRef, Player.getComponentType());
        if (playerComponent == null) {
            HytaleLogger.getLogger().at(Level.INFO).log("FairyBellInteraction requires a Player but was used for: %s", playerRef);
            context.getState().state = InteractionState.Failed;
        } else {
            //Send an interaction to all fairies that have the player as a recruiter.
            List<Ref<EntityStore>> potentialTargets = new ObjectArrayList<>();
            Selector.selectNearbyEntities(commandBuffer, playerRef, this.range,
                    potentialTargets::add,
                    entity -> commandBuffer.getComponent(entity, LittleHelpersPlugin.Instance().getFairyComponent()) != null);

            UUIDComponent playerUUIDComp = commandBuffer.getComponent(playerRef, UUIDComponent.getComponentType());
            if(playerUUIDComp == null)
            {
                HytaleLogger.getLogger().at(Level.INFO).log("FairyBellInteraction requires a player with UUID component.");
                return;
            }

            //Actual Fairy logic here.
            for(Ref<EntityStore> entity : potentialTargets)
            {
                NPCEntity npcComponent = commandBuffer.getComponent(entity, NPCEntity.getComponentType());

                if (npcComponent == null) {
                    HytaleLogger.getLogger().at(Level.INFO).log("FairyBellInteraction requires a target NPC");
                    context.getState().state = InteractionState.Failed;
                } else {
                    SimpleEntityMessageComponent messageComponent = commandBuffer.getComponent(entity, LittleHelpersPlugin.Instance().getMessagesComponent());
                    if( messageComponent != null)
                    {
                        messageComponent.putMessage(this.context, playerUUIDComp);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public String toString() {
        return "FairyBellInteraction{} " + super.toString();
    }
}