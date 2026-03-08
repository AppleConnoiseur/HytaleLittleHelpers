package chjees.tools.npc.actions.builders;

import chjees.tools.npc.actions.ClearMessage;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.StringHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.StringNotEmptyValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class BuilderClearMessage extends BuilderActionBase {
    private final StringHolder messageKey = new StringHolder();

    @NullableDecl
    @Override
    public String getShortDescription() {
        return "Clears a message if it exists.";
    }

    @NullableDecl
    @Override
    public String getLongDescription() {
        return "Clears a message if it exists.";
    }

    @NullableDecl
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new ClearMessage(this, builderSupport);
    }

    @NullableDecl
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Override
    public BuilderClearMessage readConfig(JsonElement data) {
        this.requireString(
                data,
                "Message",
                this.getMessageKey(),
                StringNotEmptyValidator.get(),
                BuilderDescriptorState.Stable,
                "Message to clear.",
                null
        );
        return this;
    }

    public StringHolder getMessageKey() {
        return this.messageKey;
    }
}