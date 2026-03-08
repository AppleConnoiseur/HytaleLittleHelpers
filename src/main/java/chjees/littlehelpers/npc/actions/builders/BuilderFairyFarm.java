package chjees.littlehelpers.npc.actions.builders;

import chjees.littlehelpers.npc.actions.FairyFarm;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class BuilderFairyFarm extends BuilderActionBase {

    @NullableDecl
    @Override
    public String getShortDescription() {
        return "";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new FairyFarm(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderFairyFarm readConfig(JsonElement data) {
        return this;
    }
}