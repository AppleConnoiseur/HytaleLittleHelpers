package chjees.littlehelpers.commands;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class LittleHelpersDebug extends AbstractTargetEntityCommand {
    @Nonnull
    private final OptionalArg<String> componentArg = this.withOptionalArg("component", "Little_Helpers.commands.debug.component.desc", ArgTypes.STRING);

    public LittleHelpersDebug(String name, String description) {
        super(name, description);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl ObjectList<Ref<EntityStore>> objectList, @NonNullDecl World world, @NonNullDecl Store<EntityStore> store) {
        commandContext.sendMessage(Message.raw("Hello from LittleHelpersDebug."));
        if(!objectList.isEmpty())
        {
            Ref<EntityStore> firstObject = objectList.getFirst();
            FairyComponent fairyComp = store.getComponent(firstObject, LittleHelpersPlugin.Instance().getFairyComponent());
            if(fairyComp != null)
            {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Recruiter=").append(fairyComp.getPlayerRecruiter()).append('\n');
                stringBuilder.append("Need:Food=").append(fairyComp.getFoodNeed()).append('\n');
                stringBuilder.append("Need:Essence=").append(fairyComp.getEssenceNeed()).append('\n');
                stringBuilder.append("Need:Happiness=").append(fairyComp.getHappinessNeed()).append('\n');
                stringBuilder.append("Home=").append(fairyComp.getHomeCoordinatesAsPoint()).append('\n');
                commandContext.sendMessage(Message.raw(stringBuilder.toString()));
            }
        }
    }
}
