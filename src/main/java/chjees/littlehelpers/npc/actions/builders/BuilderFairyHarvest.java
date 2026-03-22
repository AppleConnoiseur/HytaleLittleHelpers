package chjees.littlehelpers.npc.actions.builders;

import chjees.littlehelpers.npc.actions.FairyHarvest;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

/**
 * <p>Builder for {@link FairyHarvest}.</p>
 * <p>Harvests the block provided by the {@link PositionProvider}.</p>
 * <p><b>Note:</b> This is an instant action.</p>
 */
public class BuilderFairyHarvest extends BuilderActionBase {

    @NullableDecl
    @Override
    public String getShortDescription() {
        return "Harvests the targeted plant.";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "Harvests the targeted plant. Requires a valid block that is a plant.";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new FairyHarvest(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderFairyHarvest readConfig(JsonElement data) {
        return this;
    }
}