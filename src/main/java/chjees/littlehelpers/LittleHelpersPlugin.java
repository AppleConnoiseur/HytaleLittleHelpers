package chjees.littlehelpers;

import chjees.littlehelpers.commands.LittleHelpersDebug;
import chjees.littlehelpers.entity.systems.FairyNeedsSystem;
import chjees.littlehelpers.events.LittleHelpersDebuggingEvent;
import chjees.littlehelpers.interactions.FairyBellInteraction;
import chjees.littlehelpers.npc.actions.builders.BuilderFairySetHome;
import chjees.littlehelpers.npc.actions.builders.BuilderRecruitFairyAction;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.filters.builders.BuilderFairyRecruiter;
import chjees.littlehelpers.npc.movement.builders.BuilderBodyFlyMotionMaintainDistance;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyHome;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyIsRecruited;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyNeedsSensor;
import chjees.littlehelpers.npc.sensors.builders.BuilderFairyRecruiterSensor;
import chjees.tools.npc.actions.builders.BuilderClearMessage;import chjees.tools.npc.actions.builders.BuilderVariablesOperation;
import chjees.tools.npc.components.SimpleEntityMessageComponent;
import chjees.tools.npc.components.VariablesComponent;
import chjees.tools.npc.sensors.builders.BuilderSimpleEntityMessageSensor;
import chjees.tools.npc.sensors.builders.BuilderVariablesCompareSensor;
import chjees.tools.npc.systems.SimpleEntityMessageSystem;
import chjees.tools.npc.systems.VariablesSystem;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.instructions.BodyMotion;

import javax.annotation.Nonnull;

public class LittleHelpersPlugin extends JavaPlugin {
    private static LittleHelpersPlugin instance;
    private ComponentType<EntityStore, FairyComponent> fairyComponent;
    private ComponentType<EntityStore, VariablesComponent> variablesComponent;
    private ComponentType<EntityStore, SimpleEntityMessageComponent> messagesComponent;

    public LittleHelpersPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static LittleHelpersPlugin Instance() {
        return instance;
    }

    @Override
    protected void setup() {
        //Commands
        this.getCommandRegistry().registerCommand(new LittleHelpersDebug("lhfairy", "Little Helpers mod debug commands."));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, LittleHelpersDebuggingEvent::onPlayerReady);

        //Entity interactions
        var entityStore = this.getEntityStoreRegistry();

        //NPC interactions
        NPCPlugin NPCCore = NPCPlugin.get();

        //For hot swapping purposes
        var motionBuilder =  NPCCore.getBuilderManager().getFactory(BodyMotion.class);
        var builderNames = motionBuilder.getBuilderNames();

        //Only inject if the builder for MaintainDistanceFly does not exist.
        //Ensure injections are only done once.
        if(!builderNames.contains("LHMaintainDistanceFly"))
        {
            //Sub-mod: NPC tools
            //Components
            variablesComponent = this.getEntityStoreRegistry().registerComponent(
                    VariablesComponent.class,
                    "VariablesComponent",
                    VariablesComponent.CODEC
            );
            messagesComponent = this.getEntityStoreRegistry().registerComponent(
                    SimpleEntityMessageComponent.class,
                    "SimpleEntityMessageComponent",
                    SimpleEntityMessageComponent.CODEC
            );
            //Systems
            entityStore.registerSystem(new VariablesSystem(getVariablesComponent()));
            entityStore.registerSystem(new SimpleEntityMessageSystem(getMessagesComponent()));
            //Actions
            NPCCore.registerCoreComponentType("VariableOperation", BuilderVariablesOperation::new);
            NPCCore.registerCoreComponentType("ClearEntityMessage", BuilderClearMessage::new);
            //Sensors
            NPCCore.registerCoreComponentType("VariableCompare", BuilderVariablesCompareSensor::new);
            NPCCore.registerCoreComponentType("EntityMessage", BuilderSimpleEntityMessageSensor::new);

            //Main mod: Little Helpers
            //Interactions
            Interaction.CODEC.register("LHFairyBell", FairyBellInteraction.class, FairyBellInteraction.CODEC);

            //Motion components
            NPCCore.registerCoreComponentType("LHMaintainDistanceFly", BuilderBodyFlyMotionMaintainDistance::new);

            //Action components
            NPCCore.registerCoreComponentType("LHRecruitFairy", BuilderRecruitFairyAction::new);
            NPCCore.registerCoreComponentType("LHFairySetHome", BuilderFairySetHome::new);

            //Sensor components
            NPCCore.registerCoreComponentType("LHFairyNeedsSatiated", BuilderFairyNeedsSensor::new);
            NPCCore.registerCoreComponentType("LHFairyHome", BuilderFairyHome::new);
            NPCCore.registerCoreComponentType("LHFairyIsRecruited", BuilderFairyIsRecruited::new);
            NPCCore.registerCoreComponentType("LHIsFairyRecruiter", BuilderFairyRecruiterSensor::new);

            //Filter components
            NPCCore.registerCoreComponentType("LHFFairyRecruiter", BuilderFairyRecruiter::new);

            //Entity components
            fairyComponent = this.getEntityStoreRegistry().registerComponent(
                    FairyComponent.class,
                    "FairyComponent",
                    FairyComponent.CODEC
            );

            //Systems
            entityStore.registerSystem(new FairyNeedsSystem(fairyComponent));
        }
    }

    public ComponentType<EntityStore, FairyComponent> getFairyComponent() {
        return fairyComponent;
    }

    public ComponentType<EntityStore, VariablesComponent> getVariablesComponent() {
        return variablesComponent;
    }

    public ComponentType<EntityStore, SimpleEntityMessageComponent> getMessagesComponent() {
        return messagesComponent;
    }
}