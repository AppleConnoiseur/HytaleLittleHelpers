package chjees.littlehelpers.npc.actions.builders;

import chjees.littlehelpers.npc.actions.RecruitFairyAction;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.InstructionType;
import com.hypixel.hytale.server.npc.asset.builder.holder.AssetHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.asset.ModelExistsValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.EnumSet;

/**
 * <p>Builder for {@link RecruitFairyAction}.</p>
 * <p>Recruits the fairy and initializes the {@link chjees.littlehelpers.npc.components.FairyComponent} for it with preset data.</p>
 */
public class BuilderRecruitFairyAction  extends BuilderActionBase {
    private final AssetHolder recruitedAppearance = new AssetHolder();

    @NullableDecl
    @Override
    public String getShortDescription() {
        return "Recruits the fairy to the players faction.";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "Recruits the fairy to the players faction.";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new RecruitFairyAction(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderRecruitFairyAction readConfig(JsonElement data) {
        this.requireInstructionType(EnumSet.of(InstructionType.Interaction));
        this.requireAsset(data,
                "RecruitedAppearance",
                recruitedAppearance,
                ModelExistsValidator.required(),
                BuilderDescriptorState.Stable,
                "Model to use for rendering the recruited state.",
                null);
        return this;
    }

    public String getRecruitedAppearance(BuilderSupport builderSupport) {
        return recruitedAppearance.get(builderSupport.getExecutionContext());
    }
}
