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
import chjees.littlehelpers.npc.sensors.builders.*;
import chjees.tools.npc.actions.builders.BuilderClearMessage;import chjees.tools.npc.actions.builders.BuilderVariablesOperation;
import chjees.tools.npc.components.SimpleEntityMessageComponent;
import chjees.tools.npc.components.VariablesComponent;
import chjees.tools.npc.sensors.builders.BuilderSimpleEntityMessageSensor;
import chjees.tools.npc.sensors.builders.BuilderVariablesCompareSensor;
import chjees.tools.npc.systems.SimpleEntityMessageSystem;
import chjees.tools.npc.systems.VariablesSystem;
import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.assetstore.event.RemovedAssetsEvent;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.builtin.adventure.farming.states.FarmingBlock;
import com.hypixel.hytale.common.util.FormatUtil;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingData;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.instructions.BodyMotion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class LittleHelpersPlugin extends JavaPlugin {
    //Singleton access
    private static LittleHelpersPlugin instance;

    //Components access to this mod
    private ComponentType<EntityStore, FairyComponent> fairyComponent;
    private ComponentType<EntityStore, VariablesComponent> variablesComponent;
    private ComponentType<EntityStore, SimpleEntityMessageComponent> messagesComponent;

    //Farming data
    private final ArrayList<String> farmableBlocks = new ArrayList<>();

    public LittleHelpersPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static LittleHelpersPlugin Instance() {
        return instance;
    }

    @Override
    protected void setup() {
        EventRegistry eventRegistry = this.getEventRegistry();

        //Commands
        this.getCommandRegistry().registerCommand(new LittleHelpersDebug("lhfairy", "Little Helpers mod debug commands."));
        eventRegistry.registerGlobal(PlayerReadyEvent.class, LittleHelpersDebuggingEvent::onPlayerReady);

        //Entity interactions
        var entityStore = this.getEntityStoreRegistry();

        //NPC interactions
        NPCPlugin NPCCore = NPCPlugin.get();

        //Reacting to asset store changes
        eventRegistry.register(LoadedAssetsEvent.class, Item.class, this::onItemAssetsChanged);
        eventRegistry.register(RemovedAssetsEvent.class, Item.class, this::onItemAssetsRemoved);

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
            NPCCore.registerCoreComponentType("LHFairyCanFarm", BuilderFairyCanFarm::new);

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

    private void onItemAssetsRemoved(@Nonnull RemovedAssetsEvent<String, Item, DefaultAssetMap<String, Item>> event) {
        //Does not matter whether the asset has been removed or changed.
        for (String removedItemId : event.getRemovedAssets())
        {
            HytaleLogger.getLogger().at(Level.INFO).log("[Removed] Assets changed! %s", removedItemId);
            farmableBlocks.remove(removedItemId);
        }
    }

    private void onItemAssetsChanged(@Nonnull LoadedAssetsEvent<String, Item, DefaultAssetMap<String, Item>> event) {
        //Only care if the asset has changed, not initial loading.
        if(!event.isInitial())
            return;

        for (Item item : event.getLoadedAssets().values())
        {
            String itemBlockId = item.getBlockId();

            if(checkFarmableItem(item))
            {
                if(!farmableBlocks.contains(itemBlockId))
                {
                    farmableBlocks.add(itemBlockId);
                }
            } else {
                farmableBlocks.remove(itemBlockId);
            }
        }
    }

    @Override
    protected void start() {
        reloadFarmableBlocks();
    }

    @Override
    protected void shutdown() {
        farmableBlocks.clear();
    }

    /// Determines whether this Item is farmable.
    private boolean checkFarmableItem(Item item)
    {
        if(item.hasBlockType())
        {
            //Get the BlockType sub-data type for Item.
            BlockType blockType = BlockType.getAssetMap().getAsset(item.getBlockId());
            assert blockType != null;

            FarmingData farmingConfig = blockType.getFarming();
            boolean isFarmable = farmingConfig != null && farmingConfig.getStages() != null;
            if (isFarmable && farmingConfig.getStageSetAfterHarvest() != null)
            {
                return true;
            }

            Holder<ChunkStore> blockEntity = blockType.getBlockEntity();
            return blockEntity != null && blockEntity.getComponent(FarmingBlock.getComponentType()) != null;
        }
        return false;
    }

    private void reloadFarmableBlocks()
    {
        farmableBlocks.clear();

        //Scan for all possible crops that can be harvested.
        try {
            long start = System.nanoTime();
            var itemAssets = AssetRegistry.getAssetStore(Item.class);
            for (Map.Entry<String, Item> stringItemEntry : itemAssets.getAssetMap().getAssetMap().entrySet()) {
                Item item = stringItemEntry.getValue();
                if(checkFarmableItem(item))
                {
                    //if(!getFarmableBlocks().contains(item.getBlockId()))
                        getFarmableBlocks().add(item.getBlockId());
                } else {
                    getFarmableBlocks().remove(item.getBlockId());
                }
            }
            HytaleLogger.forEnclosingClass().at(Level.INFO).log("Scanning for farmable blocks! No. Blocks: [%s] Took: %s", String.valueOf(getFarmableBlocks().size()), FormatUtil.nanosToString(System.nanoTime() - start));
            HytaleLogger.forEnclosingClass().at(Level.INFO).log("Blocks: %s", getFarmableBlocks());
        } catch (Exception e) {
            HytaleLogger.forEnclosingClass().at(Level.WARNING).withCause(e).log("Failed to scan for farmable blocks!");
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

    public ArrayList<String> getFarmableBlocks() {
        return farmableBlocks;
    }
}