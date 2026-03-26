package chjees.littlehelpers;

import chjees.littlehelpers.commands.LittleHelpersDebug;
import chjees.littlehelpers.entity.systems.FairyNeedsSystem;
import chjees.littlehelpers.events.LittleHelpersDebuggingEvent;
import chjees.littlehelpers.interactions.FairyBellInteraction;
import chjees.littlehelpers.npc.actions.builders.BuilderDumpInventory;
import chjees.littlehelpers.npc.actions.builders.BuilderFairyHarvest;
import chjees.littlehelpers.npc.actions.builders.BuilderFairySetHome;
import chjees.littlehelpers.npc.actions.builders.BuilderRecruitFairyAction;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.littlehelpers.npc.filters.builders.BuilderFairyRecruiter;
import chjees.littlehelpers.npc.sensors.builders.*;
import chjees.littlehelpers.utility.ItemUtility;
import chjees.tools.npc.actions.builders.BuilderClearMessage;
import chjees.tools.npc.actions.builders.BuilderVariablesOperation;
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
import com.hypixel.hytale.common.util.FormatUtil;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.instructions.Action;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * <p>Main entrance class for the <b>Little Helpers</b> mod.</p>
 * <h1>Little Helpers Plugin</h1>
 * <p>Setups the plugin for Hytale.</p>
 * <p>TODO: Add more information later.</p>
 * @author ChJees
 */
public class LittleHelpersPlugin extends JavaPlugin {
    //Singleton access
    private static LittleHelpersPlugin instance;

    //Components access to this mod
    private ComponentType<EntityStore, FairyComponent> fairyComponent;
    private ComponentType<EntityStore, VariablesComponent> variablesComponent;
    private ComponentType<EntityStore, SimpleEntityMessageComponent> messagesComponent;

    //Farming data
    private final ArrayList<String> harvestableBlocks = new ArrayList<>();
    private final IntArrayList harvestableBlockIds = new IntArrayList();
    private final HashMap<String, String> farmableItemToBlockTypeIds = new HashMap<>();
    private int blockEmptyId = Integer.MIN_VALUE;

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
        var checkBuilder =  NPCCore.getBuilderManager().getFactory(Action.class);
        var builderNames = checkBuilder.getBuilderNames();

        //Only inject if the builder for MaintainDistanceFly does not exist.
        //Ensure injections are only done once.
        if(!builderNames.contains("BuilderFairySetHome"))
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

            //Action components
            NPCCore.registerCoreComponentType("LHRecruitFairy", BuilderRecruitFairyAction::new);
            NPCCore.registerCoreComponentType("LHFairySetHome", BuilderFairySetHome::new);
            NPCCore.registerCoreComponentType("LHHarvest", BuilderFairyHarvest::new);
            NPCCore.registerCoreComponentType("LHDumpInventory", BuilderDumpInventory::new);

            //Sensor components
            NPCCore.registerCoreComponentType("LHFairyNeedsSatiated", BuilderFairyNeedsSensor::new);
            NPCCore.registerCoreComponentType("LHFairyHome", BuilderFairyHome::new);
            NPCCore.registerCoreComponentType("LHFairyIsRecruited", BuilderFairyIsRecruited::new);
            NPCCore.registerCoreComponentType("LHIsFairyRecruiter", BuilderFairyRecruiterSensor::new);
            NPCCore.registerCoreComponentType("LHCanFarm", BuilderFairyCanFarm::new);
            NPCCore.registerCoreComponentType("LHClosestHarvestable", BuilderFairyFindClosestHarvestable::new);

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
            if(farmableItemToBlockTypeIds.containsKey(removedItemId))
            {
                HytaleLogger.getLogger().at(Level.INFO).log("[Removed] Assets changed! Removing: %s", removedItemId);
                harvestableBlockIds.removeInt(BlockType.getAssetMap().getIndex(farmableItemToBlockTypeIds.get(removedItemId)));
                harvestableBlocks.remove(removedItemId);
                farmableItemToBlockTypeIds.remove(removedItemId);
            }
        }
    }

    private void onItemAssetsChanged(@Nonnull LoadedAssetsEvent<String, Item, DefaultAssetMap<String, Item>> event) {
        //Only care if the asset has changed, not initial loading.
        if(!event.isInitial())
            return;

        for (Item item : event.getLoadedAssets().values())
        {
            String itemId = item.getId();
            //If we have the asset in the system, then re-check its validity.
            BlockType finalStageBlockType = ItemUtility.getHarvestableBlockTypeFromItem(item);
            if(farmableItemToBlockTypeIds.containsKey(itemId))
            {
                //Recheck
                if(finalStageBlockType == null)
                {
                    HytaleLogger.getLogger().at(Level.INFO).log("[Changed] Assets changed! Removing: %s", itemId);
                    //Remove because it failed the check.
                    harvestableBlockIds.add(BlockType.getAssetMap().getIndex(farmableItemToBlockTypeIds.get(itemId)));
                    harvestableBlocks.remove(itemId);
                    farmableItemToBlockTypeIds.remove(itemId);
                }
            } else {
                //Check if the Item is valid for being added.
                if(finalStageBlockType != null)
                {
                    HytaleLogger.getLogger().at(Level.INFO).log("[Changed] Assets changed! Adding: %s", itemId);

                    //Add to the system.
                    String blockId = finalStageBlockType.getId();
                    harvestableBlocks.add(blockId);
                    farmableItemToBlockTypeIds.put(itemId, blockId);
                    harvestableBlockIds.add(BlockType.getAssetMap().getIndex(blockId));
                }
            }
        }
    }

    @Override
    protected void start() {
        setBlockEmptyId(BlockType.getAssetMap().getIndex(BlockType.EMPTY_KEY));
        reloadHarvestableBlocks();
    }

    @Override
    protected void shutdown() {
        harvestableBlocks.clear();
        harvestableBlockIds.clear();
        farmableItemToBlockTypeIds.clear();
    }

    /**
     * Scans for all possible harvestable blocks from <b>Items<b/>.
     */
    private void reloadHarvestableBlocks()
    {
        //Tidy up old state data.
        harvestableBlocks.clear();
        harvestableBlockIds.clear();
        farmableItemToBlockTypeIds.clear();

        try {
            //Statistics
            long start = System.nanoTime();

            //Scan the entire asset library of items.
            var itemAssets = AssetRegistry.getAssetStore(Item.class);
            for (Map.Entry<String, Item> stringItemEntry : itemAssets.getAssetMap().getAssetMap().entrySet()) {
                Item item = stringItemEntry.getValue();

                //Identify item as harvestable.
                BlockType finalStageBlockType = ItemUtility.getHarvestableBlockTypeFromItem(item);
                if(finalStageBlockType != null)
                {
                    String finalBlockId = finalStageBlockType.getId();
                    harvestableBlocks.add(finalBlockId);
                    getHarvestableBlockIds().add(BlockType.getAssetMap().getIndex(finalBlockId));
                    farmableItemToBlockTypeIds.put(item.getId(), finalBlockId);
                }
            }

            HytaleLogger.forEnclosingClass().at(Level.INFO).log("Scanning for farmable blocks! No. Blocks: [%s] Took: %s", String.valueOf(getHarvestableBlocks().size()), FormatUtil.nanosToString(System.nanoTime() - start));
            //HytaleLogger.forEnclosingClass().at(Level.INFO).log("Block IDs: %s", harvestableBlockIds);
            //HytaleLogger.forEnclosingClass().at(Level.INFO).log("Blocks: %s", harvestableBlocks);
            //HytaleLogger.forEnclosingClass().at(Level.INFO).log("Item to Blocks: %s", farmableItemToBlockTypeIds);
        } catch (Exception e) {
            HytaleLogger.forEnclosingClass().at(Level.SEVERE).withCause(e).log("Failed to scan for farmable blocks!");
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

    public ArrayList<String> getHarvestableBlocks() {
        return harvestableBlocks;
    }

    public IntArrayList getHarvestableBlockIds() {
        return harvestableBlockIds;
    }

    public int getBlockEmptyId() {
        return blockEmptyId;
    }

    public void setBlockEmptyId(int blockEmptyId) {
        this.blockEmptyId = blockEmptyId;
    }
}