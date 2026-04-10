package chjees.tools.utility;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;

import java.util.Arrays;

/**
 * Utility class for classes dealing with assets.
 */
public class AssetUtility {
    /**
     * Checks if the {@link Item} has the <b>tag</b> in the specified <b>category</b>.
     * @param item Item asset to check.
     * @param category Item tag category e.g "Type"
     * @param tag Item tag name e.g "Furniture"
     * @return True if the matching category and tag exist. False if neither exist.
     */
    public static boolean itemHasTag(Item item, String category, String tag)
    {
        AssetExtraInfo.Data itemData = item.getData();
        if(itemData == null)
            return false;

        String[] itemTags = itemData.getRawTags().get(category);
        return itemTags != null && Arrays.asList(itemTags).contains(tag);
    }
}
