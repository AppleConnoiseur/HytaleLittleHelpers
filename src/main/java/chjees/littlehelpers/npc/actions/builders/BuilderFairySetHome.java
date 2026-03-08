package chjees.littlehelpers.npc.actions.builders;

import chjees.littlehelpers.npc.actions.FairySetHome;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class BuilderFairySetHome extends BuilderActionBase {
    private boolean unsetHome;

    @NullableDecl
    @Override
    public String getShortDescription() {
        return "Sets the home area center co-ordinates.";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "Sets the home area center co-ordinates.";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new FairySetHome(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderFairySetHome readConfig(JsonElement data) {
        this.getBoolean(data,
                "Unset",
                v -> unsetHome = v,
                false,
                BuilderDescriptorState.Stable,
                "If true then this unsets the home.",
                "If true then this unsets the home. Sets home position to 0 and home is no longer available.");
        return this;
    }

    public boolean isUnsetHome() {
        return unsetHome;
    }
}