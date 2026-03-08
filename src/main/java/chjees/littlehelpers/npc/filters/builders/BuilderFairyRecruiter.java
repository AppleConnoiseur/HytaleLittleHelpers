package chjees.littlehelpers.npc.filters.builders;

import chjees.littlehelpers.npc.filters.FairyRecruiter;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.IEntityFilter;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderEntityFilterBase;
import javax.annotation.Nonnull;

public class BuilderFairyRecruiter extends BuilderEntityFilterBase {
    public BuilderFairyRecruiter() {
    }

    @Nonnull
    public FairyRecruiter build(@Nonnull BuilderSupport builderSupport) {
        return new FairyRecruiter(this, builderSupport);
    }

    @Nonnull
    @Override
    public String getShortDescription() {
        return "Matches if the entity it checks is the one which recruited it.";
    }

    @Nonnull
    @Override
    public String getLongDescription() {
        return "Matches if the entity it checks is the one which recruited it.";
    }

    @Nonnull
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Nonnull
    @Override
    public Builder<IEntityFilter> readConfig(@Nonnull JsonElement data) {
        return this;
    }
}