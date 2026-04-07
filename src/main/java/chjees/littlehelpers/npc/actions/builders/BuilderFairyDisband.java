package chjees.littlehelpers.npc.actions.builders;

import chjees.littlehelpers.npc.actions.FairyDisband;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.AssetHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.asset.ModelExistsValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;

/**
* <p>Builder for {@link FairyDisband}.</p>
* <p>TODO: Add JavaDoc documentation here.</p>
*/
public class BuilderFairyDisband extends BuilderActionBase {
    private final AssetHolder originalAppearance = new AssetHolder();

    @Override
    public String getShortDescription() {
        return "Short description for FairyDisband.";
    }

    @Override
    public String getLongDescription() {
        return "Long description for FairyDisband.";
    }

    @Override
    public Action build(BuilderSupport builderSupport) {
        return new FairyDisband(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderFairyDisband readConfig(JsonElement data) {
        this.requireAsset(data,
                "OriginalAppearance",
                originalAppearance,
                ModelExistsValidator.required(),
                BuilderDescriptorState.Stable,
                "Model to use for rendering the original state.",
                null);
        return this;
    }

    public String getOriginalAppearance(BuilderSupport builderSupport) {
        return originalAppearance.get(builderSupport.getExecutionContext());
    }
}