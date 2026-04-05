package chjees.littlehelpers.utility;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.farming.FarmingData;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * A collection of functions for analyzing {@link Item}'s and {@link BlockType}'s.
 */
public class ItemUtility {
    /**
     * Identifies and returns the {@link BlockType} of a <b>harvestable</b> {@link Item}.
     * <h3>Criteria</h3>
     * <ol>
     *     <li>Checks if the item has a {@link BlockType}. Must be true.</li>
     *     <li>Checks if the item has the `Plant` tag in the `Type` category. Must be true.</li>
     *     <li>Checks if the {@link BlockType} has {@link FarmingData}. Must be true.</li>
     *     <li>Checks if the {@link BlockType} has a {@link BlockType} with the <i>Stage</i> of <b>&quot;StageFinal&quot;</b>. Must be true.</li>
     * </ol>
     * @param item Item type to check.
     * @return {@link BlockType} if one is found. <b>null</b> if none is found.
     */
    public static BlockType getHarvestableBlockTypeFromItem(Item item)
    {
        //Only care about blocks.
        var itemData = item.getData();
        if(item.hasBlockType() && itemData != null)
        {
            //Only care about blocks with the `Plant` tag.
            String[] itemTags = itemData.getRawTags().get("Type");
            if(itemTags != null && Arrays.stream(itemTags).noneMatch(s -> s.equals("Plant")))
                return null;

            //Get the BlockType sub-data type for Item.
            BlockType blockType = BlockType.getAssetMap().getAsset(item.getBlockId());
            assert blockType != null;

            //Must be a farmable block.
            FarmingData farmingConfig = blockType.getFarming();
            //boolean isFarmable = farmingConfig != null && farmingConfig.getStages() != null;
            if (farmingConfig != null && farmingConfig.getStages() != null)
            {
                //Get the final stage block type of this item.
                BlockType finalStageBlockType = blockType.getBlockForState("StageFinal");
                if(finalStageBlockType == null)
                {
                    //No final stage? Do we have a `Stage2` with a soft gatherable drop?
                    BlockType stage2BlockType = blockType.getBlockForState("Stage2");
                    if( stage2BlockType != null &&
                            stage2BlockType.getGathering() != null &&
                            stage2BlockType.getGathering().isSoft() )
                    {
                        //We do. Return this instead.
                        return  stage2BlockType;
                    }

                    //Not harvestable as a `Crop` block. Return nothing.
                    return null;
                }

                return finalStageBlockType;
            }
        }
        return null;
    }
}
