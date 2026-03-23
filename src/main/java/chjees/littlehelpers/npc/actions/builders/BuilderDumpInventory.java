package chjees.littlehelpers.npc.actions.builders;

import chjees.littlehelpers.npc.actions.DumpInventory;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

/**
 * <p>Builder for {@link DumpInventory}.</p>
 */
public class BuilderDumpInventory extends BuilderActionBase {
    @NullableDecl
    @Override
    public String getShortDescription() {
        return "Dumps the inventories of the hotbar and storage of a NPC.";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "Dumps the inventories of the hotbar and storage of a NPC.";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new DumpInventory(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderDumpInventory readConfig(JsonElement data) {
        return this;
    }
}